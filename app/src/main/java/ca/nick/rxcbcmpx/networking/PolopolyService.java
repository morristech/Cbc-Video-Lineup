package ca.nick.rxcbcmpx.networking;

import ca.nick.rxcbcmpx.models.PolopolyItem;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PolopolyService {

    String BASE_URL = "https://www.cbc.ca/json/cmlink/";

    @GET("{sourceId}")
    Flowable<PolopolyItem> stories(@Path("sourceId") String sourceId);
}
