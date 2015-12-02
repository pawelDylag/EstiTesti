package estimote.com.estitesti.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import estimote.com.estitesti.R;
import estimote.com.estitesti.data.Venue;
import estimote.com.estitesti.data.VenueBuilder;

/**
 * Created by paweldylag on 02/12/15.
 */
public class VenueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Dataset - all items in recycler
     */
    private List<Venue> mDataset;

    /**
     * Really bad thing, but time is more important
     */
    private Context context;

    public VenueListAdapter( Context context) {
        this.mDataset = new LinkedList<>();
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_venue_item, parent, false);
        return new VenueHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((VenueHolder)holder).mTextView.setText(mDataset.get(position).getName());
        Picasso.with(context).load(mDataset.get(position).getImageUrl()).into(((VenueHolder)holder).mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Parse json and fill data
     * @param json
     */
    public void updateDataset(JSONObject json) {
        List<Venue> data = new LinkedList<>();
        try {
            JSONArray allVenuesJson = VenueBuilder.getVenueJsonList(json);
            for (int i = 0; i < allVenuesJson.length(); i++) {
                data.add(VenueBuilder.fromJson(allVenuesJson.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mDataset.clear();
        this.mDataset.addAll(data);
        this.notifyDataSetChanged();
    }


    /**
     * =============
     * PRIVATE CLASS
     */
    private class VenueHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView mImageView;

        public VenueHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(R.id.activity_main_venue_item_image_view);
            this.mTextView = (TextView) itemView.findViewById(R.id.activity_main_venue_item_text_view);
        }
    }
}
