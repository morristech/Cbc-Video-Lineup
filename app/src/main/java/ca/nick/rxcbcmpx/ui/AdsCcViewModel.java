package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.data.VideoRepository;
import ca.nick.rxcbcmpx.models.ThePlatformItem;
import ca.nick.rxcbcmpx.utils.RxExtensions;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AdsCcViewModel extends ViewModel {

    private static final String TAG = AdsCcViewModel.class.getSimpleName();

    private MutableLiveData<ThePlatformItem> thePlatformItemObservable = new MutableLiveData<>();
    private final VideoRepository videoRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public AdsCcViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public LiveData<ThePlatformItem> getThePlatformItemObservable() {
        return thePlatformItemObservable;
    }

    public void fetchLatestNatlEpisode() {
        Disposable disposable = videoRepository.fetchLatestNatlEpisode()
                .compose(RxExtensions.applySchedulersFlowable())
                .subscribe(thePlatformItemObservable::setValue,
                        error -> Log.e(TAG, "Error fetching latest The National episode", error));

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
