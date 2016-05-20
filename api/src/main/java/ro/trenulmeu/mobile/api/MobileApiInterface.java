package ro.trenulmeu.mobile.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ro.trenulmeu.mobile.api.models.PathResult;
import ro.trenulmeu.mobile.api.models.TrainDelay;
import ro.trenulmeu.mobile.models.DataBaseStatus;

/**
 * Mobile API.
 */
public interface MobileApiInterface {

    @GET("db/status")
    Call<DataBaseStatus> getStatus();

    @GET("trains/{id}/delay")
    Call<TrainDelay> getTrainDelay(@Path("id") long trainId);

    @GET("route/{fromId}/{toId}/{date}")
    Call<PathResult> getRoutes(@Path("fromId") long fromId, @Path("toId") long toId, @Path("date") String dateTime);

}
