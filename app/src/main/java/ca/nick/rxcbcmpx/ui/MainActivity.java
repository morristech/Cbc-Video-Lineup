package ca.nick.rxcbcmpx.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ca.nick.rxcbcmpx.R;
import dagger.android.support.DaggerAppCompatActivity;

// TODO: Full screen: https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
// TODO: Add material horizontal progressbar on bottom of screen. Use another LiveData<State> for this
// TODO: Add snackbar for error handling with Retry button
// TODO: Need to not start the video media source for every item scrolled thru quickly: onFling?
// TODO: custom player control layout like Tasty app, i.e. only pause/play + fullscreen
// TODO: This library: https://github.com/eneim/toro , https://eneim.github.io/2017/07/09/toro-101-how-to-1/
// TODO: Can retrofit services return a Single<T> instead of Flowable<T>?
public class MainActivity extends DaggerAppCompatActivity
        implements VideoLineupFragment.ToolbarSetterUpperCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, VideoLineupFragment.create(), VideoLineupFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
