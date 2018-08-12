package ca.nick.rxcbcmpx.di;

import android.app.Application;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ca.nick.rxcbcmpx.ui.MainActivity;
import ca.nick.rxcbcmpx.ui.VideoAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class VideoModule {

    @Provides
    public BandwidthMeter bandwidthMeter() {
        return new DefaultBandwidthMeter();
    }

    @Provides
    public TrackSelection.Factory trackSelectionFactory(BandwidthMeter bandwidthMeter) {
        return new AdaptiveTrackSelection.Factory(bandwidthMeter);
    }

    @Provides
    public TrackSelector trackSelector(TrackSelection.Factory factory) {
        return new DefaultTrackSelector(factory);
    }

    @Provides
    public ExoPlayer exoPlayer(Application application, TrackSelector trackSelector) {
        return ExoPlayerFactory.newSimpleInstance(application, trackSelector);
    }

    @Provides
    public String userAgent(MainActivity mainActivity) {
        return Util.getUserAgent(mainActivity, "cbc");
    }

    @Provides
    public DataSource.Factory dataSourceFactory(String userAgent) {
        return new DefaultHttpDataSourceFactory(userAgent);
    }

    @Provides
    public HlsDataSourceFactory defaultDataSource(DataSource.Factory dataSourceFactory) {
        return new DefaultHlsDataSourceFactory(dataSourceFactory);
    }

    @Provides
    public HlsMediaSource.Factory hlsMediaSourceFactory(HlsDataSourceFactory hlsDataSourceFactory) {
        return new HlsMediaSource.Factory(hlsDataSourceFactory);
    }
}
