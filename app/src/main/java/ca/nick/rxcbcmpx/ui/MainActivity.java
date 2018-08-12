package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.Resource;
import ca.nick.rxcbcmpx.utils.Status;
import dagger.android.support.DaggerAppCompatActivity;

// TODO: Swipe to delete + coordinator layout paraxis thing up top; CBC newsroom image
// lottie loading screen
// TODO: Full screen: https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
// TODO: Remove all controls except mute?
public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    VideoAdapter adapter;
    private VideoViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = findViewById(R.id.progressBar);
        errorMessage = findViewById(R.id.errorMessage);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
        viewModel.getLocalVideoItems().observe(this, this::renderVideoItems);
    }

    private void renderVideoItems(Resource<List<VideoItem>> resource) {
        if (resource == null) {
            return;
        }

        setLoading(resource.getStatus() == Status.LOADING);
        setSuccess(resource.getData());
        setError(resource.getError());
    }

    private void loadVideos() {
        viewModel.loadVideos();
    }

    private void purgeVideos() {
        viewModel.nuke();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    private void setSuccess(@Nullable List<VideoItem> videoItems) {
        if (videoItems == null) {
            return;
        }

        adapter.submitList(videoItems);
    }

    private void setError(@Nullable Throwable throwable) {
        if (throwable == null) {
            errorMessage.setVisibility(View.INVISIBLE);
            return;
        }
        throwable.printStackTrace();
        errorMessage.setVisibility(View.VISIBLE);
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
