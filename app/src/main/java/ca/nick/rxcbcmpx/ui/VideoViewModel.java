package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.data.VideoRepository;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.Resource;
import ca.nick.rxcbcmpx.utils.RxExtensions;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VideoViewModel extends ViewModel {

    private static final String TAG = "cbc";

    private MediatorLiveData<Resource<List<VideoItem>>> localVideoItems = new MediatorLiveData<>();
    private final VideoRepository videoRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public VideoViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        localVideoItems.addSource(videoRepository.getLocalVideoItems(),
                items -> localVideoItems.setValue(Resource.success(items)));
    }

    public LiveData<Resource<List<VideoItem>>> getLocalVideoItems() {
        return localVideoItems;
    }

    public void loadVideos() {
        Disposable disposable = videoRepository.nukeThenfetchThenPersistVideos()
                .compose(RxExtensions.applySchedulers())
                .startWith(startLoading())
                .subscribe(() -> Log.d(TAG, "Completed fetching and persisting remote videos"),
                        error -> localVideoItems.setValue(Resource.error(error)));

        compositeDisposable.add(disposable);
    }

    public void nuke() {
        compositeDisposable.clear();

        Disposable disposable = videoRepository.nukeDatabase()
                .compose(RxExtensions.applySchedulers())
                .startWith(startLoading())
                .subscribe(() -> Log.d(TAG, "Nuking complete"),
                        error -> localVideoItems.setValue(Resource.error(error)));

        compositeDisposable.add(disposable);
    }

    private CompletableSource startLoading() {
        return Completable.fromAction(() -> localVideoItems.setValue(Resource.loading()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
