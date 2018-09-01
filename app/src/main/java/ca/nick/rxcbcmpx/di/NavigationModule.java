package ca.nick.rxcbcmpx.di;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.ui.MainActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @Provides
    public FragmentManager fragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }

    @Provides
    @IdRes
    public int containerId() {
        return R.id.container;
    }
}
