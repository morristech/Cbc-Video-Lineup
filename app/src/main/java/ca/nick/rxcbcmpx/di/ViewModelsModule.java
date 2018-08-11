package ca.nick.rxcbcmpx.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ca.nick.rxcbcmpx.ui.VideoViewModel;
import ca.nick.rxcbcmpx.utils.CbcViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(VideoViewModel.class)
    public abstract ViewModel videoViewModel(VideoViewModel videoViewModel);

    @Binds
    public abstract ViewModelProvider.Factory viewModelFactory(CbcViewModelFactory factory);
}
