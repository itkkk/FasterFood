package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterRestaurantList extends RecyclerView.Adapter<AdapterRestaurantList.ViewHolder> {
    private String[] mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRestaurantList(String[] myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRestaurantList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtViewRestaurantName.setText(mDataset[position]);
        holder.txtViewDescription.setText("...");
        holder.img.setImageResource(R.drawable.ic_food);
        animate(holder);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animBounce);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;
        public TextView txtViewRestaurantName;
        public TextView txtViewDescription;

        public ViewHolder(View v) {
            super(v);
            txtViewRestaurantName = (TextView) v.findViewById(R.id.txtRestaurantName);
            txtViewDescription = (TextView) v.findViewById(R.id.txtDescription);
            img = (ImageView) v.findViewById(R.id.imageView);
        }
    }
}