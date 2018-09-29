package ca.nick.rxcbcmpx.ui;

import android.support.v7.util.DiffUtil;

import ca.nick.rxcbcmpx.models.VideoItem;

public class VideoDiffCallback extends DiffUtil.ItemCallback<VideoItem> {

    private static class Holder {
        private static final VideoDiffCallback INSTANCE = new VideoDiffCallback();
    }

    public static VideoDiffCallback getInstance() {
        return Holder.INSTANCE;
    }

    private VideoDiffCallback() {
    }

    @Override
    public boolean areItemsTheSame(VideoItem oldItem, VideoItem newItem) {
        return oldItem.getGuid() == newItem.getGuid();
    }

    @Override
    public boolean areContentsTheSame(VideoItem oldItem, VideoItem newItem) {
        return oldItem.equals(newItem);
    }
}
