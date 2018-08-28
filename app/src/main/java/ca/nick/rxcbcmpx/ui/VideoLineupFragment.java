package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
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
import dagger.android.support.DaggerFragment;
import im.ene.toro.widget.Container;

public class VideoLineupFragment extends DaggerFragment {

    public static final String TAG = VideoLineupFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    VideoAdapter adapter;

    private VideoViewModel viewModel;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView errorMessage;
    private Container container;
    private FloatingActionButton fab;

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
        return inflater.inflate(R.layout.fragment_video_lineup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        progressBar = view.findViewById(R.id.progressBar);
        errorMessage = view.findViewById(R.id.errorMessage);
        fab = view.findViewById(R.id.fab);
        container = view.findViewById(R.id.toroContainer);
        container.setAdapter(adapter);
        container.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0) fab.hide();
                else if (dy < 0) fab.show();
            }
        });
        reconcileCoordinatorLayoutForToroContainer();
    }

    private void reconcileCoordinatorLayoutForToroContainer() {
        ViewGroup.LayoutParams params = container.getLayoutParams();
        if (params instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior temp = ((CoordinatorLayout.LayoutParams) params).getBehavior();
            if (temp != null) {
                container.setBehaviorCallback(() -> container.onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE));
                ((CoordinatorLayout.LayoutParams) params).setBehavior(new Container.Behavior(temp));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callback.setToolbar(toolbar);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
        viewModel.getLocalVideoItems().observe(this, this::renderVideoItems);
        viewModel.getState().observe(this, this::monitorState);
        viewModel.loadVideos();
    }

    private void renderVideoItems(List<VideoItem> videoItems) {
        if (videoItems == null) {
            return;
        }

        adapter.submitList(videoItems);
    }

    private void monitorState(Resource<Void> state) {
        switch (state.getStatus()) {
            case LOADING:
                setLoading(true);
                clearError();
                break;
            case SUCCESS:
                setLoading(false);
                clearError();
                break;
            case ERROR:
                setLoading(false);
                setError(state.getError());
        }
    }

    private void loadVideos() {
        viewModel.loadVideos();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void clearError() {
        setError(null);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        container.scrollToPosition(adapter.getLastPlayingPosition());
    }
}
