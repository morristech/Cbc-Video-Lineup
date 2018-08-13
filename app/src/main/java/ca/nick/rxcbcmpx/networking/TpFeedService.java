package ca.nick.rxcbcmpx.networking;

import ca.nick.rxcbcmpx.models.TpFeedItem;
import ca.nick.rxcbcmpx.utils.Constants;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TpFeedService {

    String BASE_URL = "https://tpfeed.cbc.ca/f/ExhSPC/vms_dHMhf1eENFvm/";

    @Headers(Constants.USER_AGENT)
    @GET("?")
    Flowable<TpFeedItem> tpFeedItems(@Query("byGuid") String guid);
}
