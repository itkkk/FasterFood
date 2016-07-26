package it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen;

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
import java.util.Collections;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Order;

public class RecyclerAdapterRVLocals extends RecyclerView.Adapter<RecyclerAdapterRVLocals.MyViewHolder>{

    private LayoutInflater inflater;
    //private List<SettingsElementRVLocals> data = Collections.emptyList();
    private List<Local> localList;
    private Context context;

    /*
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterRVLocals(Context context, List<SettingsElementRVLocals> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }*/

    public RecyclerAdapterRVLocals(Context context, List<Local> localList){
        inflater=LayoutInflater.from(context);
        this.localList = localList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = inflater.inflate(R.layout.recycler_row_locals,parent,false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        /*
        SettingsElementRVLocals current=data.get(position);
        holder.icon.setImageResource(current.icon);
        holder.name.setText(current.local_name);
        holder.address.setText(current.local_address);
        animate(holder);*/
        holder.name.setText(localList.get(position).getNome());
        holder.address.setText(localList.get(position).getVia() + ", " + localList.get(position).getCitta());
        if(!AppConfiguration.isLogoDownloaded()){
            holder.icon.setImageResource(R.drawable.ic_food);
        }
        else{
            if(localList.get(position).getCategoria().equals(context.getResources().getString(R.string.mcdonalds))){
                File logo = ScambiaDati.getLogo(0);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.icon.setImageBitmap(logoBitmap);
            }
            else if(localList.get(position).getCategoria().equals(context.getResources().getString(R.string.burgerking))){
                File logo = ScambiaDati.getLogo(1);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.icon.setImageBitmap(logoBitmap);
            }
            else if(localList.get(position).getCategoria().equals(context.getResources().getString(R.string.baciodilatte))){
                File logo = ScambiaDati.getLogo(2);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.icon.setImageBitmap(logoBitmap);
            }
        }
        animate(holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        ImageView icon;
        TextView name;
        TextView address;

        public MyViewHolder(View itemView){
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.imgLocals);
            name=(TextView) itemView.findViewById(R.id.txtNameLocal);
            address=(TextView) itemView.findViewById(R.id.txtAddressLocal);
        }
    }

    public void animate(MyViewHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }

    public String getLocalName(int position){
        return localList.get(position).getNome();
    }
}
