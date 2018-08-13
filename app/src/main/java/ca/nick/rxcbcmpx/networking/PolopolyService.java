package ca.nick.rxcbcmpx.networking;

import ca.nick.rxcbcmpx.models.PolopolyItem;
import ca.nick.rxcbcmpx.utils.Constants;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PolopolyService {

    String BASE_URL = "https://www.cbc.ca/json/cmlink/";

    @Headers(Constants.USER_AGENT)
    @GET("{sourceId}")
    Flowable<PolopolyItem> stories(@Path("sourceId") String sourceId);
}
