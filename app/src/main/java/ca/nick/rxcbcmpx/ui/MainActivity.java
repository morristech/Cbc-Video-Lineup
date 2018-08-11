package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    VideoAdapter adapter;
    private VideoViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLifecycle().addObserver(adapter);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
        viewModel.getLocalVideoItems().observe(this, this::renderVideoItems);
    }

    private void renderVideoItems(List<VideoItem> videoItems) {
        progressBar.setVisibility(View.INVISIBLE);

        if (videoItems == null) {
            return;
        }

        adapter.submitList(videoItems);
    }

    private void loadVideos() {
        setLoading();
        viewModel.loadVideos();
    }

    private void purgeVideos() {
        setLoading();
        viewModel.nuke();
    }

    private void setLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.downloadVideos: {
                if (!isLoading()) {
                    loadVideos();
                }
                return true;
            }
            case R.id.deleteAllVideos: {
                purgeVideos();
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }
}
