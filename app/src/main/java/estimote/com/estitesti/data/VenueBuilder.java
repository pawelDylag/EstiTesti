package estimote.com.estitesti.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by paweldylag on 02/12/15.
 */
public class VenueBuilder {

    public static Venue fromJson(JSONObject json){
        // TODO: DEBUG HERE
        String name = "Null";
        String imageUrl = "Null";
        try {
            name = json.getString("name");
            imageUrl = json.getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("prefix");
            imageUrl += "88";
            imageUrl += json.getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("suffix");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return new Venue(name, imageUrl);
    }

    public static JSONArray getVenueJsonList(JSONObject json) {
        JSONArray result = null;
        try {
            result = json.getJSONObject("body").getJSONObject("response").getJSONArray("venues");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

}
