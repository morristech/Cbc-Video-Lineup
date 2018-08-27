package ca.nick.rxcbcmpx.di;

import android.app.Application;

import javax.inject.Singleton;

import ca.nick.rxcbcmpx.CbcApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        NetworkingModule.class,
        ViewModelsModule.class,
        ActivitiesModule.class})
public interface ApplicationComponent extends AndroidInjector<CbcApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}
