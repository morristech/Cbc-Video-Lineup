package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;

public class VideoAdapter extends ListAdapter<VideoItem, VideoViewHolder>
        implements LifecycleObserver {

    private ExoPlayer exoPlayer;
    private Resources resources;

    public VideoAdapter(ExoPlayer exoPlayer, Resources resources) {
        super(VideoDiffCallback.getInstance());
        this.exoPlayer = exoPlayer;
        this.resources = resources;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view, exoPlayer);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(getItem(position), resources);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        int i = 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        int i = 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        int i = 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        int i = 0;
    }
}
