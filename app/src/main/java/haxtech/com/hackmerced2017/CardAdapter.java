package haxtech.com.hackmerced2017;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by aatifshah on 2/11/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private static final String TAG = "CardAdapter";
    private ArrayList<PostObject> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View card;
        public TextView user, body, category;
        public VideoView video;


        public ViewHolder(View c) {
            super(c);
            card = c;
            user = (TextView) c.findViewById(R.id.CardView_Username);
            category = (TextView) c.findViewById(R.id.CardView_ExerciseName);
            //video = (VideoView) c.findViewById(R.id.CardView_VideoView);
            //test = (TextView) c.findViewById(R.id.Vote_Count);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(ArrayList<PostObject> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_card_view, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.test.setText(mDataset[position]);
        PostObject post = mDataset.get(position);
        holder.category.setText(post.category);
        holder.user.setText(post.userID);
        holder.video.setVideoURI(Uri.parse(post.vidUrl));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


