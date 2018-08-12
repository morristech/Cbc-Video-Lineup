package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
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

import javax.inject.Provider;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.GlideApp;

public class VideoAdapter extends ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>
        implements LifecycleObserver {

    private final Provider<ExoPlayer> exoPlayerProvider;
    private final HlsMediaSource.Factory factory;
    private final Context activityContext;

    public VideoAdapter(Provider<ExoPlayer> exoPlayerProvider,
                        HlsMediaSource.Factory factory,
                        Context activityContext) {
        super(VideoDiffCallback.getInstance());
        this.exoPlayerProvider = exoPlayerProvider;
        this.factory = factory;
        this.activityContext = activityContext;
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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        if (Util.SDK_INT > 23) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        if (Util.SDK_INT <= 23) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        if (Util.SDK_INT <= 23) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        if (Util.SDK_INT > 23) {
        }
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.releasePlayer();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setPlayer(exoPlayerProvider.get());
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ExoPlayer exoPlayer;
        private TextView title;
        private PlayerView player;
        private ImageView previewImage;
        private ProgressBar progressBar;
        private Group previewGroup;
        private VideoItem videoItem;
        private ComponentListener componentListener;

        public VideoViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            player = itemView.findViewById(R.id.player);
            previewImage = itemView.findViewById(R.id.preview_image);
            previewGroup = itemView.findViewById(R.id.preview_group);
            progressBar = itemView.findViewById(R.id.preview_progress_bar);
            itemView.setOnClickListener(this);
        }

        public void bind(VideoItem videoItem, Context context) {
            this.videoItem = videoItem;
            title.setText(videoItem.getTitle());

            GlideApp.with(context)
                    .load(videoItem.getThumbnailUrl())
                    .into(previewImage);
        }

        private void setPlaying() {
            previewGroup.setVisibility(View.INVISIBLE);
            player.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            startPlaying();
        }

        public void startPlaying() {
            componentListener = new ComponentListener();
            exoPlayer.addListener(componentListener);
            player.setPlayer(exoPlayer);
            progressBar.setVisibility(View.VISIBLE);
            MediaSource mediaSource = factory.createMediaSource(Uri.parse(videoItem.getSrc()));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }

        public void setPlayer(ExoPlayer exoPlayer) {
            if (hasExoPlayer()) {
                throw new RuntimeException("Trying to set ExoPlayer member var when it's already set");
            }
            this.exoPlayer = exoPlayer;
        }

        public void releasePlayer() {
            if (!hasExoPlayer()) {
                return;
            }

            exoPlayer.removeListener(componentListener);
            componentListener = null;
            exoPlayer.release();
            exoPlayer = null;

            previewGroup.setVisibility(View.VISIBLE);
            player.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public boolean hasExoPlayer() {
            return exoPlayer != null;
        }

        private class ComponentListener extends Player.DefaultEventListener {

            private boolean hasPlayerBeenSeen;

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_READY:
                        if (!hasPlayerBeenSeen) {
                            setPlaying();
                            hasPlayerBeenSeen = true;
                        }
                        break;
                    case Player.STATE_ENDED:
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
