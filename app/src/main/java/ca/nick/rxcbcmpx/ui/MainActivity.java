package ca.nick.rxcbcmpx.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.R;
import dagger.android.support.DaggerAppCompatActivity;

// TODO: Handle subtitles
// TODO: Fix last video not playing
// TODO: Lazily load videos so there's always a fresh token for the smil file
public class MainActivity extends DaggerAppCompatActivity
        implements VideoLineupFragment.ToolbarSetterUpperCallback {

    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            navigationController.navigateToVideoLineupFragment();
        }
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
