package ca.nick.rxcbcmpx.ui;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;

public class VideoAdapter extends ListAdapter<VideoItem, ToroViewHolder> {

    @Inject
    protected VideoAdapter(@NonNull VideoDiffCallback videoDiffCallback) {
        super(videoDiffCallback);
    }

    @NonNull
    @Override
    public ToroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_toro_video, parent, false);

        return new ToroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToroViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
