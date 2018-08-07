package ca.nick.rxcbcmpx.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.models.LineupItem;
import ca.nick.rxcbcmpx.models.PolopolyItem;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.MpxService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import ca.nick.rxcbcmpx.utils.RxExtensions;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

public class VideoRepository {

    private static final String TAG = "cbc";

    private final CbcDatabase cbcDatabase;
    private MediatorLiveData<List<VideoItem>> localVideoItems = new MediatorLiveData<>();
    private final AggregateApiService aggregateApiService;
    private final PolopolyService polopolyService;
    private final MpxService mpxService;

    @Inject
    public VideoRepository(CbcDatabase cbcDatabase,
                           AggregateApiService aggregateApiService,
                           PolopolyService polopolyService,
                           MpxService mpxService) {
        this.cbcDatabase = cbcDatabase;
        this.aggregateApiService = aggregateApiService;
        this.polopolyService = polopolyService;
        this.mpxService = mpxService;

        localVideoItems.addSource(cbcDatabase.videoDao().videoItems(), localVideoItems::setValue);
    }

    public LiveData<List<VideoItem>> getLocalVideoItems() {
        return localVideoItems;
    }

    public Disposable fetchAndPersistVideos() {
        Completable nukeDatabase = Completable.fromAction(cbcDatabase.videoDao()::nuke);

        Flowable<String> fetchVideoContent = aggregateApiService.topStoriesVideos()
                .flatMap(Flowable::fromIterable)
                .map(LineupItem::getSourceId)
                // TODO: Make mps.theplatform.com link work, not just polopoly id
                .flatMap(sourceId -> polopolyService.story(sourceId)
                        .onErrorResumeNext(__ -> {
                            Log.e(TAG, "Erroneous source ID: " + sourceId);
                            return Flowable.empty();
                        }))
                .map(PolopolyItem::toString);

        return nukeDatabase
                .andThen(fetchVideoContent)
                .compose(RxExtensions.applySchedulers())
                .subscribe(s -> Log.d(TAG, s), Throwable::printStackTrace);
    }
}
