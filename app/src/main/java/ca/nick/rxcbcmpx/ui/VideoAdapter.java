package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;
import javax.inject.Provider;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.GlideApp;

public class VideoAdapter extends ListAdapter<VideoItem, VideoAdapter.VideoViewHolder> {

    private final Provider<ExoPlayer> exoPlayerProvider;
    private final HlsMediaSource.Factory factory;
    private final Context activityContext;
    private final LifecycleOwner lifecycleOwner;
    private LinearLayoutManager layoutManager;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int currentlyPlayingPosition = -1;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (layoutManager == null) {
                return;
            }

            int first = layoutManager.findFirstCompletelyVisibleItemPosition();
            int last = layoutManager.findFirstCompletelyVisibleItemPosition();

            // FIXME: Last item in adapter is never autoplaying
            int middleView = (first + last) / 2;
            if (currentlyPlayingPosition != middleView) {
                // These VHs can be null after deleting all the adapter's items
                VideoViewHolder currentViewHolder =
                        (VideoViewHolder) recyclerView.findViewHolderForLayoutPosition(currentlyPlayingPosition);
                if (currentViewHolder != null) {
                    currentViewHolder.releasePlayer();
                }

                VideoViewHolder nextViewHolder =
                        (VideoViewHolder) recyclerView.findViewHolderForLayoutPosition(middleView);
                if (nextViewHolder != null) {
                    nextViewHolder.startPlaying();
                }
                currentlyPlayingPosition = middleView;
            }
        }
    };

    @Inject
    public VideoAdapter(Provider<ExoPlayer> exoPlayerProvider,
                        HlsMediaSource.Factory factory,
                        MainActivity mainActivity) {
        super(VideoDiffCallback.getInstance());
        this.exoPlayerProvider = exoPlayerProvider;
        this.factory = factory;
        this.activityContext = mainActivity;
        this.lifecycleOwner = mainActivity;
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
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        lifecycleOwner.getLifecycle().removeObserver(holder);
        holder.releasePlayer();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        lifecycleOwner.getLifecycle().addObserver(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        layoutManager = null;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, LifecycleObserver{

        private ExoPlayer exoPlayer;
        private TextView title;
        private PlayerView playerView;
        private ImageView previewImage;
        private ProgressBar progressBar;
        private Group previewGroup;
        private VideoItem videoItem;
        private ComponentListener componentListener;

        public VideoViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            playerView = itemView.findViewById(R.id.player);
            previewImage = itemView.findViewById(R.id.preview_image);
            previewGroup = itemView.findViewById(R.id.preview_group);
            progressBar = itemView.findViewById(R.id.preview_progress_bar);
            setPreviewingUiState();
            itemView.setOnClickListener(this);
        }

        public void bind(VideoItem videoItem, Context context) {
            this.videoItem = videoItem;
            title.setText(videoItem.getTitle());

            GlideApp.with(context)
                    .load(videoItem.getThumbnailUrl())
                    .into(previewImage);
        }

        private void setPlayingUiState() {
            previewGroup.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void setPreviewingUiState() {
            previewGroup.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (isLoading()) {
                return;
            }
            startPlaying();
        }

        private boolean isLoading() {
            return progressBar.getVisibility() == View.VISIBLE;
        }

        public void startPlaying() {
            initPlayer();
            progressBar.setVisibility(View.VISIBLE);

            componentListener = new ComponentListener();
            exoPlayer.addListener(componentListener);
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
            MediaSource mediaSource = factory.createMediaSource(Uri.parse(videoItem.getSrc()));
            exoPlayer.prepare(mediaSource);
        }

        public void initPlayer() {
            if (hasExoPlayer()) {
                return;
            }
            this.exoPlayer = exoPlayerProvider.get();
        }

        public void releasePlayer() {
            if (!hasExoPlayer()) {
                return;
            }

            exoPlayer.removeListener(componentListener);
            componentListener = null;
            exoPlayer.release();
            exoPlayer = null;

            setPreviewingUiState();
        }

        public boolean hasExoPlayer() {
            return exoPlayer != null;
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

        private class ComponentListener extends Player.DefaultEventListener {

            private static final String TAG = "cbc";

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_IDLE:
                        Log.d(TAG, "idle");
                        break;
                    case Player.STATE_BUFFERING:
                        Log.d(TAG, "buffering");
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                        Log.d(TAG, "ready, isLoading: " + isLoading() + ", playWhenReady: " + playWhenReady);
                        if (playWhenReady && isLoading()) {
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
