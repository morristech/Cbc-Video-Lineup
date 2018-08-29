package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.data.VideoRepository;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.Resource;
import ca.nick.rxcbcmpx.utils.RxExtensions;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VideoViewModel extends ViewModel {

    private static final String TAG = VideoViewModel.class.getSimpleName();

    private MutableLiveData<Resource<Void>> state = new MutableLiveData<>();
    private MediatorLiveData<List<VideoItem>> localVideoItems = new MediatorLiveData<>();
    private final VideoRepository videoRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public VideoViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        localVideoItems.addSource(videoRepository.getLocalVideoItems(), localVideoItems::setValue);
    }

    public LiveData<List<VideoItem>> getLocalVideoItems() {
        return localVideoItems;
    }

    public MutableLiveData<Resource<Void>> getState() {
        return state;
    }

    public void loadVideos() {
        clearRequestsInFlight();
        videoRepository.nukeThenfetchThenPersistVideos()
                .compose(RxExtensions.applySchedulers())
                .subscribe(createStateManager());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    private void clearRequestsInFlight() {
        compositeDisposable.clear();
    }

    private CompletableObserver createStateManager() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                state.setValue(Resource.loading());
            }

            @Override
            public void onComplete() {
                state.setValue(Resource.success(null));
            }

            @Override
            public void onError(Throwable e) {
                state.setValue(Resource.error(e));

            }
        };
    }
}
