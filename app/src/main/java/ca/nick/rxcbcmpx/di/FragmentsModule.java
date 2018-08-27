package ca.nick.rxcbcmpx.di;

import ca.nick.rxcbcmpx.ui.VideoLineupFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsModule {

    @ContributesAndroidInjector(modules = VideoModule.class)
    abstract VideoLineupFragment videoLineupFragment();
}
