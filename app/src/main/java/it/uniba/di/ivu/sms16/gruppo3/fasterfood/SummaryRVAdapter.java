package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class SummaryRVAdapter extends RecyclerView.Adapter<SummaryRVAdapter.ViewHolder> {
    private String[] mDataset;
    private Context context;

    public SummaryRVAdapter(String[] myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public SummaryRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_summary_new, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // TODO Qui andranno messi i dati da "stampare"
        holder.txtNomeProdotto.setText(mDataset[position]);
        holder.txtPrezzo.setText("2.9");
        holder.txtQuantity.setText("5");

        holder.txtTOT.setText(
                String.valueOf(
                        Float.valueOf(holder.txtPrezzo.getText().toString()) *
                        Float.valueOf(holder.txtQuantity.getText().toString())
                )
        );
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtNomeProdotto;
        public TextView txtPrezzo;
        public TextView txtQuantity;
        public TextView txtTOT;

        public ViewHolder(View v) {
            super(v);
            txtNomeProdotto = (TextView) v.findViewById(R.id.txtNomeProdotto);
            txtPrezzo = (TextView) v.findViewById(R.id.txtPrezzo);
            txtQuantity = (TextView) v.findViewById(R.id.txtQuantity);
            txtTOT = (TextView) v.findViewById(R.id.txtTOT);
        }
    }
}
