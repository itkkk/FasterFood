package it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class SummaryRVAdapter extends RecyclerView.Adapter<SummaryRVAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> quantities;

    public SummaryRVAdapter(ArrayList<String> names, ArrayList<String> quantities, ArrayList<String> prices) {
        this.names = names;
        this.quantities = quantities;
        this.prices = prices;
    }

    public Float getSubTotal(int position){
        return (Float.valueOf(prices.get(position)) * Float.valueOf(quantities.get(position)));
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

        // Qui ci sono i dati da "stampare"
        holder.txtNomeProdotto.setText(names.get(position));
        holder.txtPrezzo.setText(prices.get(position));
        holder.txtQuantity.setText(quantities.get(position));

        holder.txtTOT.setText(
                String.valueOf(
                        Float.valueOf(holder.txtPrezzo.getText().toString()) *
                        Float.valueOf(holder.txtQuantity.getText().toString())
                )
        );
    }

    @Override
    public int getItemCount() {
        return names.size();
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
            txtTOT = (TextView) v.findViewById(R.id.txtTot);
        }
    }
}
