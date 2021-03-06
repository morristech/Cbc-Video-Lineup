package ca.nick.rxcbcmpx.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.models.AggregateItem;
import ca.nick.rxcbcmpx.models.PolopolyItem;
import ca.nick.rxcbcmpx.models.ThePlatformItem;
import ca.nick.rxcbcmpx.models.TpFeedItem;
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

    public Completable nukeThenFetchThenPersistVideos() {
        return nukeDatabase()
                .andThen(fetchVideoContent())
                .flatMapCompletable(this::insertLocally);
    }

    public Flowable<VideoItem> fetchVideoContent() {
        return aggregateApiService.topStoriesVideos()
                .retry(NUM_RETRY_ATTEMPTS)
                .flatMap(Flowable::fromIterable)
                .flatMap(this::fetchTpFeedItemFromAggregateItem,
                        (aggregateItem, tpFeedItem) -> tpFeedItem.setAggregateItem(aggregateItem))
                .flatMap(tpFeedItem -> fetchThePlatformItem(tpFeedItem.getSmilUrlId()),
                        (tpFeedItem, thePlatformItem) -> thePlatformItem.setTpFeedItem(tpFeedItem))
                .map(VideoItem::fromRemoteData);
    }

    private Flowable<TpFeedItem> fetchTpFeedItemFromAggregateItem(AggregateItem aggregateItem) {
        Flowable<TpFeedItem> tpFeedItemFlowable;

        if (aggregateItem.isPolopolySource()) {
            tpFeedItemFlowable = fetchPolopolyItem(aggregateItem.getSourceId())
                    .flatMap(polopolyItem -> fetchTpFeedItemByGuid(polopolyItem.getMediaid()));
        } else {
            tpFeedItemFlowable = fetchTpFeedItemByGuid(aggregateItem.getMpxSourceGuid());
        }

        return tpFeedItemFlowable;
    }

    private Flowable<PolopolyItem> fetchPolopolyItem(String sourceId) {
        return polopolyService.story(sourceId)
                .doOnError(error -> Log.d(TAG, "Error getting polopolyItem using: " + sourceId, error))
                .onErrorResumeNext(Flowable.empty());
    }

    private Flowable<TpFeedItem> fetchTpFeedItemByGuid(String mediaId) {
        return tpFeedService.byGuid(mediaId)
                .filter(tpFeedItem -> !tpFeedItem.getEntries().isEmpty() && !tpFeedItem.isLive())
                .doOnError(error -> Log.d(TAG, "Error getting tpFeedItem using: " + mediaId, error))
                .onErrorResumeNext(Flowable.empty());
    }

    private Flowable<ThePlatformItem> fetchThePlatformItem(String smilUrlId) {
        return thePlatformService.thePlatformItem(smilUrlId)
                .doOnError(error -> Log.d(TAG, "Error getting thePlatformItem using: " + smilUrlId, error))
                .onErrorResumeNext(Flowable.empty());
    }

    public Completable nukeDatabase() {
        return Completable.fromAction(cbcDatabase.videoDao()::nuke);
    }

    public Completable insertLocally(VideoItem videoItem) {
        return Completable.fromAction(() -> cbcDatabase.videoDao().insertVideoItem(videoItem));
    }

    public Flowable<ThePlatformItem> fetchLatestNatlEpisode() {
        return tpFeedService.latestNatlEpisode()
                .flatMap(tpFeedItem -> fetchThePlatformItem(tpFeedItem.getSmilUrlId()));
    }
}
