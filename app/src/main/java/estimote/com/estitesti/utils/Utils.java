package estimote.com.estitesti.utils;

import android.location.Location;

import estimote.com.estitesti.utils.Constants;

/**
 * Created by paweldylag on 02/12/15.
 */
public class Utils {

    /**
     * Converts raw location data to API format
     * @param l - location
     * @return - String formatted for API
     */
    public static String locationToAPIString(Location l) {
        return  l.getLatitude() + Constants.API_LOCATION_SEPARATOR + l.getLongitude();
    }

}
