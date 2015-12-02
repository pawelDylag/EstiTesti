package estimote.com.estitesti.api;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.util.List;

import estimote.com.estitesti.data.Venue;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by paweldylag on 02/12/15.
 */
public interface FourSquareService {

    @GET("v2/venues/search")
    Call<Object> getVenues(@Query("client_id") String clientId,
                                 @Query("client_secret") String clientSecret,
                                 @Query("ll") String latLon,
                                 @Query("llAcc") double accuracy,
                                 @Query("v") int version);

}
