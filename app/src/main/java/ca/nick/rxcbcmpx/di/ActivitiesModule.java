package ca.nick.rxcbcmpx.di;

import ca.nick.rxcbcmpx.ui.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = {FragmentsModule.class, NavigationModule.class})
    abstract MainActivity mainActivity();
}
