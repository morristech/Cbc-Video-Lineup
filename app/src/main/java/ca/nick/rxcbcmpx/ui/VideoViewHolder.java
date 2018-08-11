package ca.nick.rxcbcmpx.ui;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.VideoItem;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    private ExoPlayer exoPlayer;
    private TextView guid;
    private TextView polopolyId;
    private TextView title;
    private TextView description;
    private TextView duration;
    private TextView live;
    private TextView publishedDate;
    private PlayerView player;

    public VideoViewHolder(View itemView, ExoPlayer exoPlayer) {
        super(itemView);
        this.exoPlayer = exoPlayer;
        guid = itemView.findViewById(R.id.guid);
        polopolyId = itemView.findViewById(R.id.polopolyId);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        duration = itemView.findViewById(R.id.duration);
        live = itemView.findViewById(R.id.live);
        publishedDate = itemView.findViewById(R.id.publishedDate);
        player = itemView.findViewById(R.id.player);
        player.setPlayer(exoPlayer);
    }

    public void bind(VideoItem videoItem, Resources resources) {
        guid.setText(resources.getString(R.string.item_guid, videoItem.getGuid()));
        polopolyId.setText(resources.getString(R.string.item_polopoly_id, videoItem.getPolopolyId()));
        title.setText(resources.getString(R.string.item_title, videoItem.getTitle()));
        description.setText(resources.getString(R.string.item_description, videoItem.getDescription()));
        duration.setText(resources.getString(R.string.item_duration, videoItem.getDuration()));
        live.setText(resources.getString(R.string.item_live, videoItem.getLive()));
        publishedDate.setText(resources.getString(R.string.item_published_date, videoItem.getPublishedDate()));

//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
//                itemView.getContext(),
//                Util.getUserAgent(itemView.getContext(), "cbc"),
//                (DefaultBandwidthMeter) bandwidthMeter);
//
//        Handler handler = new Handler();
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(videoItem.getSrc()));
//        mediaSource.addEventListener(handler, new MediaSourceEventListener() {
//            @Override
//            public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
//
//            }
//
//            @Override
//            public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
//
//            }
//
//            @Override
//            public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
//
//            }
//
//            @Override
//            public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
//
//            }
//
//            @Override
//            public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
//
//            }
//
//            @Override
//            public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
//
//            }
//
//            @Override
//            public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
//
//            }
//
//            @Override
//            public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
//
//            }
//
//            @Override
//            public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
//
//            }
//        });
//
//
//        exoPlayer.prepare(mediaSource);
    }
}
