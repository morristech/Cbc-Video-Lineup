package ca.nick.rxcbcmpx.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.ThePlatformService;
import ca.nick.rxcbcmpx.networking.TpFeedService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class VideoRepository {

    public static final String TAG = VideoRepository.class.getSimpleName();
    private static final int NUM_RETRY_ATTEMPTS = 3;  // Arbitrary

    private final CbcDatabase cbcDatabase;
    private MediatorLiveData<List<VideoItem>> localVideoItems = new MediatorLiveData<>();
    private final AggregateApiService aggregateApiService;
    private final PolopolyService polopolyService;
    private final TpFeedService tpFeedService;
    private final ThePlatformService thePlatformService;

    @Inject
    public VideoRepository(CbcDatabase cbcDatabase,
                           AggregateApiService aggregateApiService,
                           PolopolyService polopolyService,
                           TpFeedService tpFeedService,
                           ThePlatformService thePlatformService) {
        this.cbcDatabase = cbcDatabase;
        this.aggregateApiService = aggregateApiService;
        this.polopolyService = polopolyService;
        this.tpFeedService = tpFeedService;
        this.thePlatformService = thePlatformService;

        localVideoItems.addSource(cbcDatabase.videoDao().videoItems(), localVideoItems::setValue);
    }

    public LiveData<List<VideoItem>> getLocalVideoItems() {
        return localVideoItems;
    }

    public Completable nukeThenfetchThenPersistVideos() {
        return nukeDatabase()
                .andThen(fetchVideoContent())
                .flatMapCompletable(this::insertLocally);
    }

    public Flowable<VideoItem> fetchVideoContent() {
        return aggregateApiService.topStoriesVideos()
                .retry(NUM_RETRY_ATTEMPTS)
                .flatMap(Flowable::fromIterable)
                .flatMap(lineupItem -> polopolyService.stories(lineupItem.getSourceId())
                        .doOnNext(polopolyItem -> polopolyItem.setLineupItem(lineupItem))
                        .doOnError(error -> Log.d(TAG, "Error getting polopolyItem using: " + lineupItem, error))
                        .onErrorResumeNext(Flowable.empty()))
                .flatMap(polopolyItem -> tpFeedService.tpFeedItems(polopolyItem.getMediaid())
                        .doOnNext(tpFeedItem -> tpFeedItem.setPolopolyItem(polopolyItem))
                        .doOnError(error -> Log.d(TAG, "Error getting tpFeedItem using: " + polopolyItem, error))
                        .onErrorResumeNext(Flowable.empty()))
                .flatMap(tpFeedItem -> thePlatformService.thePlatformItems(tpFeedItem.getSmilUrlId())
                        .doOnNext(thePlatformItem -> thePlatformItem.setTpFeedItem(tpFeedItem))
                        .doOnError(error -> Log.d(TAG, "Error getting thePlatformItem using: " + tpFeedItem, error))
                        .onErrorResumeNext(Flowable.empty()))
                .map(VideoItem::fromRemoteData);
    }

    public Completable nukeDatabase() {
        return Completable.fromAction(cbcDatabase.videoDao()::nuke);
    }

    public Completable insertLocally(VideoItem videoItem) {
        return Completable.fromAction(() -> cbcDatabase.videoDao().insertVideoItem(videoItem));
    }

    public Completable deleteLocally(VideoItem videoItem) {
        return Completable.fromAction(() -> cbcDatabase.videoDao().deleteVideoItem(videoItem));
    }
}
