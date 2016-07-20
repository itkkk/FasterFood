package it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;


public class AdapterRestaurantList extends RecyclerView.Adapter<AdapterRestaurantList.ViewHolder> {
    private List<Local> mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRestaurantList(List<Local> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRestaurantList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_search, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtViewRestaurantName.setText(mDataset.get(position).getNome());
        holder.txtViewDescription.setText(mDataset.get(position).getVia() + ", " + mDataset.get(position).getCitta());
        if(!AppConfiguration.isLogoDownloaded()){
            holder.logo.setImageResource(R.drawable.ic_food);
        }
        else{
            if(mDataset.get(position).getCategoria().equals(context.getResources().getString(R.string.mcdonalds))){
                File logo = ScambiaDati.getLogo(0);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.logo.setImageBitmap(logoBitmap);
           }
            else if(mDataset.get(position).getCategoria().equals(context.getResources().getString(R.string.burgerking))){
                File logo = ScambiaDati.getLogo(1);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.logo.setImageBitmap(logoBitmap);
            }
            else if(mDataset.get(position).getCategoria().equals(context.getResources().getString(R.string.baciodilatte))){
                File logo = ScambiaDati.getLogo(2);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.logo.setImageBitmap(logoBitmap);
            }
        }
        animate(holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animBounce);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewRestaurantName;
        public TextView txtViewDescription;
        public ImageView logo;

        public ViewHolder(View v) {
            super(v);
            txtViewRestaurantName = (TextView) v.findViewById(R.id.txtRestaurantName);
            txtViewDescription = (TextView) v.findViewById(R.id.txtDescription);
            logo = (ImageView) v.findViewById(R.id.logo);
        }
    }
}