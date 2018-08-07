package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.data.VideoRepository;
import ca.nick.rxcbcmpx.models.VideoItem;
import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private MediatorLiveData<List<VideoItem>> localVideoItems = new MediatorLiveData<>();
    private final VideoRepository videoRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MainViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        localVideoItems.addSource(videoRepository.getLocalVideoItems(), localVideoItems::setValue);
    }

    public LiveData<List<VideoItem>> getLocalVideoItems() {
        return localVideoItems;
    }

    public void loadVideos() {
        compositeDisposable.add(videoRepository.fetchVideos());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
