package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.Resource;
import ca.nick.rxcbcmpx.utils.Status;
import dagger.android.support.DaggerFragment;
import im.ene.toro.widget.Container;

public class VideoLineupFragment extends DaggerFragment {

    public static final String TAG = VideoLineupFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    VideoAdapter adapter;

    private static final String KEY_LIST_POSITION = "list_position";

    private VideoViewModel viewModel;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView errorMessage;
    private Container container;
    private ToolbarSetterUpperCallback callback;

    public interface ToolbarSetterUpperCallback {
        void setToolbar(Toolbar toolbar);
    }

    public static VideoLineupFragment create() {
        return new VideoLineupFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ToolbarSetterUpperCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(KEY_LIST_POSITION);
//            container.scrollToPosition(position);
        } else {
            toolbar = view.findViewById(R.id.toolbar);
            progressBar = view.findViewById(R.id.progressBar);
            errorMessage = view.findViewById(R.id.errorMessage);
            container = view.findViewById(R.id.toroContainer);
            container.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        callback.setToolbar(toolbar);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
        viewModel.getLocalVideoItems().observe(this, this::renderVideoItems);
//        viewModel.loadVideos();
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

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private void setSuccess(@Nullable List<VideoItem> videoItems) {
        if (videoItems == null || videoItems.isEmpty()) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.downloadVideos: {
                loadVideos();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        int position = adapter.get
    }
}
