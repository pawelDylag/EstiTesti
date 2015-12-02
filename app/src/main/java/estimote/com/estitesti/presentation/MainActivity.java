package estimote.com.estitesti.presentation;

import android.content.IntentSender;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import estimote.com.estitesti.R;
import estimote.com.estitesti.adapters.VenueListAdapter;
import estimote.com.estitesti.api.FourSquareService;
import estimote.com.estitesti.data.Venue;
import estimote.com.estitesti.utils.Constants;
import estimote.com.estitesti.utils.Utils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Main application class
 * by Pawel Dylag
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Class TAG for debugging
     */
    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Request error code
     */
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    /**
     * Google Api Client for resloving connections with Location API
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Flag for displaying error
     */
    private boolean mErrorHandleInProgress;

    /**
     * Last known device location
     */
    private Location mLastLocation;

    /**
     * Retrofit + interface for http requests
     */
    private FourSquareService mFoursquareService;

    /**
     * Retrofit object
     */
    private Retrofit mRetrofit;

    /**
     * Adapter for list view
     */
    private VenueListAdapter mVenueListAdapter;


    @Bind(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind views via butterknife
        ButterKnife.bind(this);
        // build location api client
        buildGoogleApiClient();
        // buld retrofit client
        buildApiService();
        // setup recycler view and its adapter
        setupRecyclerView();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null && mFoursquareService != null) {
            Log.d(TAG, "Received location: " + mLastLocation.toString());
            searchVenues(mLastLocation);
        } else {
            Log.d(TAG, "API service not yet established");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mErrorHandleInProgress) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mErrorHandleInProgress) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mErrorHandleInProgress = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(result.getErrorCode() + "");
            mErrorHandleInProgress = true;
        }
    }

    /**
     * Displays error dialog
     * @param text
     */
    public void showErrorDialog(String text){
        // TODO: SNACKBAR HERE
    }

    /**
     * Builds Location API client
     */
    protected synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected synchronized void buildApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        mRetrofit = new Retrofit.Builder()
                                        .baseUrl(Constants.API_BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();
        mFoursquareService = mRetrofit.create(FourSquareService.class);

    }

    private void setupRecyclerView(){
        mVenueListAdapter = new VenueListAdapter(this);
        mRecyclerView.setAdapter(mVenueListAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * Launches asynchronic Venue search and gets list of Venue objects
     * @param location - current user location
     */
    public void searchVenues(final Location location) {
        Call<Object> call =
                mFoursquareService.getVenues(Constants.FOURSQUARE_CLIENT_ID,
                        Constants.FOURSQUARE_CLIENT_SECRET,
                        Utils.locationToAPIString(location),
                        Constants.API_SEARCH_RADIUS, Constants.API_VERSION);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                try {
                    JSONObject json = new JSONObject(new Gson().toJson(response));
                    Log.d(TAG, "onResponse()" + json.toString());
                    mVenueListAdapter.updateDataset(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure()" + t.getMessage());
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Call<ResponseBody> call =
//                 mFoursquareService.getVenues(Utils.locationToAPIString(location),
//                        Constants.API_SEARCH_RADIUS, Constants.API_VERSION);
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                        Log.d(TAG, "onResponse()" + response.message());
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Log.d(TAG, "onFailure()" + t.getMessage());
//                    }
//                });
//            }
//        }).start();

    }
}
