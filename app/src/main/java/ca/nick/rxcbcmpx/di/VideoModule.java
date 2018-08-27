package ca.nick.rxcbcmpx.di;

import ca.nick.rxcbcmpx.ui.VideoDiffCallback;
import dagger.Module;
import dagger.Provides;

@Module
public class VideoModule {

    @Provides
    public VideoDiffCallback videoDiffCallback() {
        return VideoDiffCallback.getInstance();
    }
}
