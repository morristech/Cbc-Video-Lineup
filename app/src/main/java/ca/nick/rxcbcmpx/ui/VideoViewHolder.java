package ca.nick.rxcbcmpx.ui;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        implements ToroPlayer, View.OnClickListener {

    @Nullable
    private ExoPlayerViewHelper helper;
    @Nullable
    private Uri mediaUri;
    @Nullable
    private EventListener eventListener;
    @Nullable
    private OnErrorListener onErrorListener;
    private PlayingPositionListener listener;
    @Nullable
    private VideoItem videoItem;

    private TextView title;
    private PlayerView playerView;
    private ImageView previewImage;
    private ProgressBar progressBar;
    @Nullable
    private Dialog fullScreenDialog;
    private FrameLayout toggleFullScreen;
    private ImageView iconFullScreen;

    public interface PlayingPositionListener {
        void onPositionPlaying(int adapterPosition);
    }

    public VideoViewHolder(View itemView, PlayingPositionListener listener) {
        super(itemView);
        playerView = itemView.findViewById(R.id.player);
        title = itemView.findViewById(R.id.title);
        previewImage = itemView.findViewById(R.id.preview_image);
        progressBar = itemView.findViewById(R.id.preview_progress_bar);
        toggleFullScreen = playerView.findViewById(R.id.toggleFullScreen);
        toggleFullScreen.setOnClickListener(this);
        iconFullScreen = playerView.findViewById(R.id.iconFullScreen);

        this.listener = listener;
    }

    public void bind(VideoItem videoItem) {
        this.videoItem = videoItem;
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
                    listener.onPositionPlaying(getAdapterPosition());
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
            hideProgressBar();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleFullScreen:
                Context activityContext = v.getContext();
                if (isFullScreen()) {
                    closeFullScreenDialog(activityContext);
                } else {
                    openFullScreenDialog(activityContext);
                }
                break;
        }
    }

    private void openFullScreenDialog(Context activityContext) {
        initFullScreenDialog(activityContext);

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        fullScreenDialog.addContentView(playerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenDialog.show();
        iconFullScreen.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_fullscreen_exit));
    }

    private void initFullScreenDialog(Context activityContext) {
        fullScreenDialog = new Dialog(activityContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                closeFullScreenDialog(activityContext);
                super.onBackPressed();
            }
        };
    }

    private void closeFullScreenDialog(Context activityContext) {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) itemView.findViewById(R.id.player_container)).addView(playerView);
        iconFullScreen.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_fullscreen));
        fullScreenDialog.dismiss();
        fullScreenDialog = null;
    }

    private boolean isFullScreen() {
        return fullScreenDialog != null;
    }
}
