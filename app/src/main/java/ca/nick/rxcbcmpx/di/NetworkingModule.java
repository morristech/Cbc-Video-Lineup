package ca.nick.rxcbcmpx.di;

import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

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

@Module
public class NetworkingModule {

    @Provides
    public Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    public AggregateApiService aggregateApiService(Retrofit.Builder builder) {
        return createJsonService(builder, AggregateApiService.BASE_URL, AggregateApiService.class);
    }

    @Singleton
    @Provides
    public PolopolyService polopolyService(Retrofit.Builder builder) {
        return createJsonService(builder, PolopolyService.BASE_URL, PolopolyService.class);
    }

    @Singleton
    @Provides
    public TpFeedService tpFeedService(Retrofit.Builder builder) {
        return createJsonService(builder, TpFeedService.BASE_URL, TpFeedService.class);
    }

    @Singleton
    @Provides
    public ThePlatformService thePlatformService(Retrofit.Builder builder) {
        return createXmlService(builder, ThePlatformService.BASE_URL, ThePlatformService.class);
    }

    private <T> T createJsonService(Retrofit.Builder builder, String baseUrl, Class<T> clazz) {
        return createService(builder.addConverterFactory(MoshiConverterFactory.create()),
                baseUrl,
                clazz);
    }

    private <T> T createXmlService(Retrofit.Builder builder, String baseUrl, Class<T> clazz) {
        return createService(builder.addConverterFactory(TikXmlConverterFactory.create()),
                baseUrl,
                clazz);
    }

    private <T> T createService(Retrofit.Builder builder, String baseUrl, Class<T> clazz) {
        return builder
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(clazz);
    }
}
