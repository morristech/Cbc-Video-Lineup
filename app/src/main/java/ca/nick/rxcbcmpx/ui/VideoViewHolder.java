package ca.nick.rxcbcmpx.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;
import ca.nick.rxcbcmpx.utils.GlideApp;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class VideoViewHolder extends RecyclerView.ViewHolder
        implements ToroPlayer {

    @Nullable
    ExoPlayerViewHelper helper;
    @Nullable
    Uri mediaUri;
    @Nullable
    private EventListener eventListener;
    @Nullable
    private OnErrorListener onErrorListener;

    private TextView title;
    private PlayerView playerView;
    private ImageView previewImage;
    private ProgressBar progressBar;

    public VideoViewHolder(View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.player);
        title = itemView.findViewById(R.id.title);
        previewImage = itemView.findViewById(R.id.preview_image);
        progressBar = itemView.findViewById(R.id.preview_progress_bar);
    }

    public void bind(VideoItem videoItem) {
        mediaUri = videoItem.getSrcUri();
        title.setText(videoItem.getTitle());

        GlideApp.with(previewImage)
                .load(videoItem.getThumbnailUrl())
                .into(previewImage);
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return playerView;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null
                ? helper.getLatestPlaybackInfo()
                : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (helper == null) {
            helper = new ExoPlayerViewHelper(this, mediaUri);
        }

        if (eventListener == null) {
            eventListener = new EventListener() {
                @Override
                public void onBuffering() {
                    showProgressBar();
                }

                @Override
                public void onPlaying() {
                    hideProgressBar();
                    playerView.setAlpha(1f);
                }

                @Override
                public void onPaused() {
                    hideProgressBar();
                }

                @Override
                public void onCompleted() {}
            };
        }

        if (onErrorListener == null) {
            onErrorListener = error -> {
                Toast.makeText(container.getContext(), "Error loading video", Toast.LENGTH_LONG).show();
                hideProgressBar();
            };
        }

        helper.addErrorListener(onErrorListener);
        helper.addPlayerEventListener(eventListener);
        helper.initialize(container, playbackInfo);
    }

    @Override
    public void play() {
        if (helper != null) {
            helper.play();
            playerView.hideController();
        }
    }

    @Override
    public void pause() {
        if (helper != null) {
            helper.pause();
            playerView.hideController();
        }
    }

    @Override
    public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override
    public void release() {
        if (helper != null) {
            if (eventListener != null) {
                helper.removePlayerEventListener(eventListener);
            }
            if (onErrorListener != null) {
                helper.removeErrorListener(onErrorListener);
            }
            helper.release();
            helper = null;
            playerView.setAlpha(0f);
        }
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
