package ca.nick.rxcbcmpx.di;

import javax.inject.Singleton;

import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.ThePlatformService;
import ca.nick.rxcbcmpx.networking.TpFeedService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class NetworkingModule {

    @Singleton
    @Provides
    public Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Singleton
    @Provides
    public AggregateApiService aggregateApiService(Retrofit.Builder builder) {
        return createService(builder, AggregateApiService.BASE_URL, AggregateApiService.class);
    }

    @Singleton
    @Provides
    public PolopolyService polopolyService(Retrofit.Builder builder) {
        return createService(builder, PolopolyService.BASE_URL, PolopolyService.class);
    }

    @Singleton
    @Provides
    public TpFeedService tpFeedService(Retrofit.Builder builder) {
        return createService(builder, TpFeedService.BASE_URL, TpFeedService.class);
    }

    @Singleton
    @Provides
    public ThePlatformService thePlatformService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return createService(builder, ThePlatformService.BASE_URL, ThePlatformService.class);
    }

    private <T> T createService(Retrofit.Builder builder, String baseUrl, Class<T> clazz) {
        return builder
                .baseUrl(baseUrl)
                .build()
                .create(clazz);
    }
}
