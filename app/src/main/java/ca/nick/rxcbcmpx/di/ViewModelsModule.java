package ca.nick.rxcbcmpx.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ca.nick.rxcbcmpx.ui.MainViewModel;
import ca.nick.rxcbcmpx.utils.CbcViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(CbcViewModelFactory factory);
}
