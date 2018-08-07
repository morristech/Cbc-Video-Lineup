package ca.nick.rxcbcmpx.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import ca.nick.rxcbcmpx.data.CbcDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Singleton
    @Provides
    public CbcDatabase cbcDatabase(Application application) {
        return CbcDatabase.getInstance(application);
    }

    @Singleton
    @Provides
    public SharedPreferences sharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
