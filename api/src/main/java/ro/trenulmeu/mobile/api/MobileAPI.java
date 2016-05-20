package ro.trenulmeu.mobile.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.trenulmeu.mobile.api.models.PathResult;
import ro.trenulmeu.mobile.api.models.TrainDelay;
import ro.trenulmeu.mobile.api.typeadapter.DateTimeTypeAdapter;
import ro.trenulmeu.mobile.api.typeadapter.TimeSpanTypeAdapter;
import ro.trenulmeu.mobile.models.DataBaseStatus;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.timespan.TimeSpan;

/**
 * Mobile API.
 */
public class MobileAPI {
    public static final String baseURL = "http://www.trenulmeu.ro/api/";
    private MobileApiInterface service;

    private static MobileAPI ourInstance = new MobileAPI();

    public static Call<DataBaseStatus> getStatus() {
        return ourInstance.service.getStatus();
    }

    public static Call<TrainDelay> getTrainDelay(Train train) {
        return ourInstance.service.getTrainDelay(train.getId());
    }

    public static Call<PathResult> getRoutes(Station from, Station to, DateTime dateTime) {
        return ourInstance.service.getRoutes(from.getId(), to.getId(),
                dateTime.toString("yyyy-MM-dd'T'HH:mm:ss"));
    }

    private MobileAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(TimeSpan.class, new TimeSpanTypeAdapter())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(MobileApiInterface.class);
    }
}
