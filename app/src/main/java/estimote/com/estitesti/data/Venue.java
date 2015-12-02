package estimote.com.estitesti.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by paweldylag on 02/12/15.
 */
public class Venue {

    private final String mName;
    private final String mImageUrl;

    public Venue(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
