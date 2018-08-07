package ca.nick.rxcbcmpx.di;

import javax.inject.Singleton;

import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.MpxService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

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
        return createService(builder, "https://www.cbc.ca/aggregate_api/v1/", AggregateApiService.class);
    }

    @Singleton
    @Provides
    public PolopolyService polopolyService(Retrofit.Builder builder) {
        return createService(builder, "https://www.cbc.ca/json/cmlink/", PolopolyService.class);
    }

    @Singleton
    @Provides
    public MpxService mpxService(Retrofit.Builder builder) {
        return createService(builder, "https://tpfeed.cbc.ca/", MpxService.class);
    }

    private <T> T createService(Retrofit.Builder builder, String baseUrl, Class<T> clazz) {
        return builder
                .baseUrl(baseUrl)
                .build()
                .create(clazz);
    }
}
