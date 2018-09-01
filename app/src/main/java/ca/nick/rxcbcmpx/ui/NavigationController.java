package ca.nick.rxcbcmpx.ui;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

public class NavigationController {

    @IdRes
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(int containerId,
                                FragmentManager fragmentManager) {
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
    }

    public int navigateToAdsCcFragment() {
        return navigateToFragment(AdsCcVideoFragment.create(), AdsCcVideoFragment.TAG);
    }

    public int navigateToVideoLineupFragment() {
        return navigateToFragment(VideoLineupFragment.create(), VideoLineupFragment.TAG);
    }

    private int navigateToFragment(Fragment fragment, String tag) {
        return fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();
    }
}
