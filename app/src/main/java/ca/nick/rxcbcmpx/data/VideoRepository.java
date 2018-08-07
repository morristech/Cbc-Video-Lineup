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
import ca.nick.rxcbcmpx.utils.RxExtensions;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

public class VideoRepository {

    private static final String TAG = "cbc";
    private static final int NUM_RETRY_ATTEMPTS = 3;

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

    public Disposable fetchAndPersistVideos() {
        Completable nukeDatabase = createNuke();

        Completable fetchThenPersistNewVideoContent = aggregateApiService.topStoriesVideos()
                .retry(NUM_RETRY_ATTEMPTS)
                .flatMap(Flowable::fromIterable)
                .flatMap(lineupItem -> polopolyService.stories(lineupItem.getSourceId())
                        .onErrorResumeNext(Flowable.empty())) // TODO: Make mpx.theplatform.com link work, not just polopoly sources
                .flatMap(polopolyItem -> tpFeedService.tpFeedItems(polopolyItem.getMediaid()))
                .flatMap(tpFeedItem -> thePlatformService.thePlatformItems(tpFeedItem.getSmilUrlId())
                        .onErrorResumeNext(Flowable.empty()))
                .flatMapCompletable(thePlatformItem -> insertLocally(new VideoItem(thePlatformItem.getGuid(), thePlatformItem.getUrl())));

        return nukeDatabase
                .concatWith(fetchThenPersistNewVideoContent)
                .compose(RxExtensions.applySchedulers())
                .subscribe();
    }

    public Disposable nuke() {
        return createNuke()
                .compose(RxExtensions.applySchedulers())
                .subscribe(() -> Log.d(TAG, "Nuking complete"),
                        Throwable::printStackTrace);
    }

    private Completable createNuke() {
        return Completable.fromAction(cbcDatabase.videoDao()::nuke);
    }

    private Completable insertLocally(VideoItem videoItem) {
        return Completable.fromAction(() -> cbcDatabase.videoDao().insertVideoItem(videoItem));
    }
}
