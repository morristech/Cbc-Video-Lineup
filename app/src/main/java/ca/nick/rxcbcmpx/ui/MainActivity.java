package ca.nick.rxcbcmpx.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ca.nick.rxcbcmpx.R;
import dagger.android.support.DaggerAppCompatActivity;

// TODO: Full screen: https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
// TODO: Add material horizontal progressbar on bottom of screen. Use another LiveData<State> for this
// TODO: Add swiperefreshlayout
// TODO: Handle subtitles
// TODO: Handle MPX content directly from aggregate api, not just polopoly
// TODO: Fix last video not playing
// TODO: Add better FAB scrolling behaviour
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
