package ca.nick.rxcbcmpx.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import ca.nick.rxcbcmpx.R;
import ca.nick.rxcbcmpx.models.ThePlatformItem;
import ca.nick.rxcbcmpx.utils.Constants;

public class AdsCcVideoFragment extends BaseFragment {

    public static final String TAG = AdsCcVideoFragment.class.getSimpleName();

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImaAdsLoader adsLoader;
    private AdsCcViewModel viewModel;

    public static AdsCcVideoFragment create() {
        return new AdsCcVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ads_cc_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerView = view.findViewById(R.id.adsCcPlayer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AdsCcViewModel.class);
        viewModel.getThePlatformItemObservable().observe(this, this::setupVideo);
        viewModel.fetchLatestNatlEpisode();
        adsLoader = new ImaAdsLoader(requireContext(), Uri.parse(Constants.SAMPLE_AD_URL));
    }

    private void setupVideo(@Nullable ThePlatformItem thePlatformItem) {
        if (player != null || thePlatformItem == null) {
            return;
        }

        final Context context = requireContext();

        player = ExoPlayerFactory.newSimpleInstance(context,
                new DefaultTrackSelector());
        playerView.setPlayer(player);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "cbc-demo"));

        MediaSource hlsSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(thePlatformItem.getUrl()));

        // See: https://stackoverflow.com/questions/42432371/how-to-turn-on-off-closed-captions-in-hls-streaming-url-in-exoplayer
        Format format = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
        MediaSource subtitleSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(thePlatformItem.getSrtCaptions()), format, C.TIME_UNSET);

        MediaSource mergedSource = new MergingMediaSource(hlsSource, subtitleSource);

        MediaSource adsSource = new AdsMediaSource(mergedSource, dataSourceFactory,
                adsLoader, playerView.getOverlayFrameLayout());

        player.prepare(adsSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupVideo(viewModel.getThePlatformItemObservable().getValue());
    }

    @Override
    public void onStop() {
        super.onStop();

        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adsLoader.release();
    }
}
