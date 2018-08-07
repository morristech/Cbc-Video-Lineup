package ca.nick.rxcbcmpx.networking;

import ca.nick.rxcbcmpx.models.PolopolyItem;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PolopolyService {

    @GET("{sourceId}")
    Observable<PolopolyItem> story(@Path("sourceId") String sourceId);
}
