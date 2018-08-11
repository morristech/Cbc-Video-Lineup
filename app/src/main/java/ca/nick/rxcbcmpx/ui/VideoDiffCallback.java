package ca.nick.rxcbcmpx.ui;

import android.support.v7.util.DiffUtil;

import ca.nick.rxcbcmpx.models.VideoItem;

public class VideoDiffCallback extends DiffUtil.ItemCallback<VideoItem> {

    private static VideoDiffCallback INSTANCE;

    public static VideoDiffCallback getInstance() {
        if (INSTANCE == null) {
            synchronized (VideoDiffCallback.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VideoDiffCallback();
                }
            }
        }

        return INSTANCE;
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
