package ca.nick.rxcbcmpx.di;

import ca.nick.rxcbcmpx.ui.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = VideoModule.class)
    abstract MainActivity mainActivityInjector();
}
