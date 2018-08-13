package ca.nick.rxcbcmpx.ui;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.GlideApp;

public class VideoAdapter extends ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>
        implements LifecycleObserver {

    private static final String TAG = VideoAdapter.class.getSimpleName();

    private final Provider<ExoPlayer> exoPlayerProvider;
    private final HlsMediaSource.Factory factory;
    private final Context activityContext;
    private final LifecycleOwner lifecycleOwner;
    private LinearLayoutManager layoutManager;
    private ExoPlayer exoPlayer;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int currentlyPlayingPosition = -1;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (layoutManager == null) {
                return;
            }

            int first = layoutManager.findFirstCompletelyVisibleItemPosition();
            int last = layoutManager.findLastCompletelyVisibleItemPosition();

            int viewPositionToPlay;
            if (first == -1 && last == -1) {
                // This can happen in landscape when the VH is taller than the screen height
                viewPositionToPlay = layoutManager.findFirstVisibleItemPosition();
            } else if (last == getItemCount() - 1) {
                // Workaround to play final item in list
                viewPositionToPlay = last;
            } else {
                // Otherwise get the item nearest to the top
                viewPositionToPlay = first;
            }

            if (currentlyPlayingPosition != viewPositionToPlay) {
                // These VHs can be null after deleting all the adapter's items
                VideoViewHolder currentViewHolder =
                        (VideoViewHolder) recyclerView.findViewHolderForLayoutPosition(currentlyPlayingPosition);
                if (currentViewHolder != null) {
                    currentViewHolder.stopPlaying();
                    Log.d(TAG, "onScrolled: stopPlaying: " + currentViewHolder);
                }

                VideoViewHolder nextViewHolder =
                        (VideoViewHolder) recyclerView.findViewHolderForLayoutPosition(viewPositionToPlay);
                if (nextViewHolder != null) {
                    nextViewHolder.startPlaying();
                    Log.d(TAG, "\tonScrolled: startPlaying: " + nextViewHolder);
                }
                currentlyPlayingPosition = viewPositionToPlay;
            }
        }
    };

    @Inject
    public VideoAdapter(Provider<ExoPlayer> exoPlayerProvider,
                        HlsMediaSource.Factory factory,
                        MainActivity mainActivity,
                        VideoDiffCallback videoDiffCallback) {
        super(videoDiffCallback);
        this.exoPlayerProvider = exoPlayerProvider;
        this.factory = factory;
        this.activityContext = mainActivity;
        this.lifecycleOwner = mainActivity;
    }

    @Override
    public void submitList(List<VideoItem> list) {
        super.submitList(list);
        Log.d(TAG, "List submitted; size: " + list.size());
        if (exoPlayer != null && list.isEmpty()) {
            // Pause any playing video when all adapter items are purged
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(getItem(position), activityContext);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        layoutManager = null;
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        lifecycleOwner.getLifecycle().addObserver(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        lifecycleOwner.getLifecycle().removeObserver(holder);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        if (Util.SDK_INT <= 23 || exoPlayer == null) {
            initializePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer == null) {
            return;
        }

        Log.d(TAG, "Releasing player");
        exoPlayer.release();
        exoPlayer = null;
    }

    private void initializePlayer() {
        if (exoPlayer != null) {
            return;
        }

        Log.d(TAG, "Initializing player");
        exoPlayer = exoPlayerProvider.get();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder
            implements LifecycleObserver, View.OnClickListener {

        private TextView title;
        private PlayerView playerView;
        private ImageView previewImage;
        private ProgressBar progressBar;
        private VideoItem videoItem;
        private ComponentListener componentListener;

        private Dialog fullScreenDialog;
        private boolean isFullScreen;
        private FrameLayout fullScreenButton;
        private ImageView fullScreenIcon;

        public VideoItem getVideoItem() {
            return videoItem;
        }

        public VideoViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            playerView = itemView.findViewById(R.id.player);
            previewImage = itemView.findViewById(R.id.preview_image);
            progressBar = itemView.findViewById(R.id.preview_progress_bar);
            fullScreenButton = playerView.findViewById(R.id.exo_fullscreen_button);
            fullScreenIcon = playerView.findViewById(R.id.exo_fullscreen_icon);

            fullScreenButton.setOnClickListener(this);
        }

        public void bind(VideoItem videoItem, Context context) {
            // Stateful views are reused in RecyclerView, so reset their mutable state
            stopPlaying();

            this.videoItem = videoItem;
            title.setText(videoItem.getTitle());

            GlideApp.with(context)
                    .load(videoItem.getThumbnailUrl())
                    .into(previewImage);
        }

        private void setPlayingUiState() {
            playerView.setAlpha(1f);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void setPreviewingUiState() {
            playerView.setAlpha(0f);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void setLoading() {
            progressBar.setVisibility(View.VISIBLE);
        }

        private boolean isLoading() {
            return progressBar.getVisibility() == View.VISIBLE;
        }

        public void startPlaying() {
            setLoading();

            componentListener = new ComponentListener();
            exoPlayer.addListener(componentListener);
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
            MediaSource mediaSource = factory.createMediaSource(Uri.parse(videoItem.getSrc()));
            exoPlayer.prepare(mediaSource);
        }

        private void stopPlaying() {
            setPreviewingUiState();

            if (componentListener != null) {
                exoPlayer.removeListener(componentListener);
                componentListener = null;
            }
            if (fullScreenDialog != null) {
                closeFullScreenDialog();
            }
            fullScreenDialog = null;
            isFullScreen = false;
            playerView.setPlayer(null);
        }

        // TODO: Add start/resume for videos after a pause/stop lifecycle change
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void pause() {
            if (Util.SDK_INT <= 23) {
                stopPlaying();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void stop() {
            if (Util.SDK_INT > 23) {
                stopPlaying();
            }
        }

        @Override
        public void onClick(View v) {
            if (!hasPlayer()) {
                return;
            }

            if (fullScreenDialog == null) {
                initFullScreenDialog();
            }

            if (!isFullScreen) {
                openFullScreenDialog();
            } else {
                closeFullScreenDialog();
            }
        }

        private boolean hasPlayer() {
            return playerView.getPlayer() != null;
        }

        private void initFullScreenDialog() {
            fullScreenDialog = new Dialog(activityContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                @Override
                public void onBackPressed() {
                    if (isFullScreen) {
                        closeFullScreenDialog();
                    }
                    super.onBackPressed();
                }
            };
        }

        private void openFullScreenDialog() {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            fullScreenDialog.addContentView(playerView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            isFullScreen = true;
            fullScreenDialog.show();
            fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_fullscreen_exit_24dp));
        }

        private void closeFullScreenDialog() {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            ((FrameLayout) itemView.findViewById(R.id.player_container)).addView(playerView);
            isFullScreen = false;
            fullScreenDialog.dismiss();
            fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_fullscreen_24dp));
        }

        private class ComponentListener extends Player.DefaultEventListener {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_IDLE:
                        Log.d(TAG, "idle");
                        break;
                    case Player.STATE_BUFFERING:
                        if (hasPlayer()) {
                            Log.d(TAG, "buffering");
                            setLoading();
                        }
                        break;
                    case Player.STATE_READY:
                        if (hasPlayer() && playWhenReady && isLoading()) {
                            Log.d(TAG, "ready for: " + VideoViewHolder.this);
                            setPlayingUiState();
                        }
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "ended");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
