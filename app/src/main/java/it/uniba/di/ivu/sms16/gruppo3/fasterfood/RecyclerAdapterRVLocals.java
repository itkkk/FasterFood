package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapterRVLocals extends RecyclerView.Adapter<RecyclerAdapterRVLocals.MyViewHolder>{
    private LayoutInflater inflater;
    private List<SettingsElementRVLocals> data= Collections.emptyList();
    private Context context;

    public RecyclerAdapterRVLocals(Context context,List<SettingsElementRVLocals> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_row_locals,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SettingsElementRVLocals current=data.get(position);
        holder.icon.setImageResource(current.icon);
        holder.name.setText(current.local_name);
        holder.address.setText(current.local_address);
        holder.rate_img.setImageResource(current.rate_icon);
        holder.rate.setText(current.rate_txt);
        animate(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        TextView address;
        TextView rate;
        ImageView rate_img;

        public MyViewHolder(View itemView){
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.imgLocals);
            name=(TextView) itemView.findViewById(R.id.txtNameLocal);
            address=(TextView) itemView.findViewById(R.id.txtAddressLocal);
            rate_img=(ImageView) itemView.findViewById(R.id.imgRateLocals);
            rate=(TextView) itemView.findViewById(R.id.txtRateLocal);
        }
    }

    public void animate(MyViewHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }
}
