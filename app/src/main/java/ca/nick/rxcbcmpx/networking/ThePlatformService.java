package ca.nick.rxcbcmpx.networking;

import ca.nick.rxcbcmpx.models.ThePlatformItem;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ThePlatformService {

    String BASE_URL = "https://link.theplatform.com/s/ExhSPC/";

    @GET("{smilUrlId}?manifest=m3u&feed=iOS%20News%20app%20feed&mbr=true&format=smil")
    Flowable<ThePlatformItem> thePlatformItems(@Path("smilUrlId") String smilUrlId);
}
