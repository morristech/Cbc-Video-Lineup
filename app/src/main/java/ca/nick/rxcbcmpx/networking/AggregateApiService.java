package ca.nick.rxcbcmpx.networking;

import java.util.List;

import ca.nick.rxcbcmpx.models.LineupItem;
import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface AggregateApiService {

    String BASE_URL = "https://www.cbc.ca/aggregate_api/v1/";

    @GET("items?orderLineupId=2.4941&type=video&pageSize=40")
    Flowable<List<LineupItem>> topStoriesVideos();
}
