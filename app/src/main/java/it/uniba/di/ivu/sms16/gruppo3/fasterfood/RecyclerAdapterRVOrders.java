package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

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

public class RecyclerAdapterRVOrders extends RecyclerView.Adapter<RecyclerAdapterRVOrders.MyViewHolder>{

    private LayoutInflater inflater;
    private List<SettingsElementRVOrders> data= Collections.emptyList();
    private Context context;

    public RecyclerAdapterRVOrders(Context context,List<SettingsElementRVOrders> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_row_orders,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SettingsElementRVOrders current=data.get(position);
        holder.icon.setImageResource(current.image_ordId);
        holder.name.setText(current.title_ord);
        holder.tot.setText(current.tot_ord);
        holder.state.setText(current.state_ord);
        holder.edit.setText(current.btn_txt);

        animate(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
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

