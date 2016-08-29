package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Order;

public class RecyclerAdapterRVOrders extends RecyclerView.Adapter<RecyclerAdapterRVOrders.MyViewHolder>{

    private LayoutInflater inflater;
    private List<Order> orderList;
    private Context context;

    public RecyclerAdapterRVOrders(Context context, List<Order> orderList){
        inflater=LayoutInflater.from(context);
        this.orderList = orderList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = inflater.inflate(R.layout.recycler_row_orders,parent,false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.name.setText("Ordine " + (position+1));
        holder.tot.setText(orderList.get(position).getTotale() + "€");
        holder.state.setText(orderList.get(position).getStato());
        holder.loc.setText(orderList.get(position).getLocale());
        //imposto l'immagine per l'ordine
        if(!AppConfiguration.isLogoDownloaded()){
            holder.icon.setImageResource(R.drawable.ic_food);
        }
        else{
            if(orderList.get(position).getCatena().equals(context.getResources().getString(R.string.mcdonalds))){
                File logo = ScambiaDati.getLogo(0);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.icon.setImageBitmap(logoBitmap);
            }
            else if(orderList.get(position).getCatena().equals(context.getResources().getString(R.string.burgerking))){
                File logo = ScambiaDati.getLogo(1);
                Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                holder.icon.setImageBitmap(logoBitmap);
            }
            else if(orderList.get(position).getCatena().equals(context.getResources().getString(R.string.baciodilatte))){
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
        return orderList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        ImageView icon;
        TextView name;
        TextView state;
        TextView tot;
        TextView loc;
        Button edit;
        public MyViewHolder(View itemView){
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.imgOrder);
            name=(TextView) itemView.findViewById(R.id.txtNameOrder);
            state=(TextView) itemView.findViewById(R.id.txtStateOrder);
            tot=(TextView) itemView.findViewById(R.id.txtTotOrder);
            loc=(TextView) itemView.findViewById(R.id.txtLocale);
        }
    }

    public void animate(MyViewHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }
}

