package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class RecyclerAdapterRVOrders extends RecyclerView.Adapter<RecyclerAdapterRVOrders.MyViewHolder>{

    private LayoutInflater inflater;
    private List<SettingsElementRVOrders> data= Collections.emptyList();
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterRVOrders(Context context,List<SettingsElementRVOrders> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
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
        SettingsElementRVOrders current=data.get(position);
        holder.icon.setImageResource(current.image_ordId);
        holder.name.setText(current.title_ord);
        holder.tot.setText(current.tot_ord);
        holder.state.setText(current.state_ord);
        holder.edit.setText(current.btn_txt);

        animate(holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
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
        Button edit;
        public MyViewHolder(View itemView){
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.imgOrder);
            name=(TextView) itemView.findViewById(R.id.txtNameOrder);
            state=(TextView) itemView.findViewById(R.id.txtStateOrder);
            tot=(TextView) itemView.findViewById(R.id.txtTotOrder);
            edit=(Button) itemView.findViewById(R.id.btn_edit_order);
        }
    }

    public void animate(MyViewHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }
}

