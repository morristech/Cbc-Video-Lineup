package ca.nick.rxcbcmpx.di;

import android.app.Application;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import ca.nick.rxcbcmpx.ui.MainActivity;
import ca.nick.rxcbcmpx.ui.VideoAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class VideoModule {

    @ActivityScope
    @Provides
    public BandwidthMeter bandwidthMeter() {
        return new DefaultBandwidthMeter();
    }

    @ActivityScope
    @Provides
    public TrackSelection.Factory trackSelectionFactory(BandwidthMeter bandwidthMeter) {
        return new AdaptiveTrackSelection.Factory(bandwidthMeter);
    }

    @ActivityScope
    @Provides
    public TrackSelector trackSelector(TrackSelection.Factory factory) {
        return new DefaultTrackSelector(factory);
    }

    @ActivityScope
    @Provides
    public ExoPlayer exoPlayer(Application application, TrackSelector trackSelector) {
        return ExoPlayerFactory.newSimpleInstance(application, trackSelector);
    }

    @ActivityScope
    @Provides
    public VideoAdapter videoAdapter(ExoPlayer exoPlayer, MainActivity mainActivity) {
        return new VideoAdapter(exoPlayer, mainActivity);
    }
}
