package it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

// Creazione dell'Adapter, accedere ai dati
public class RecyclerAdapterRVMenu extends RecyclerView.Adapter<RecyclerAdapterRVMenu.MenuHolder> {

    private List<SettingsElementRVMenu> mListInformation;
    private String[] mSpinnerValue;
    private Context context;
    private Activity act;

    public RecyclerAdapterRVMenu(Context context, List<SettingsElementRVMenu> mListInformation, Activity act) {
        // LayoutInflater inflater = LayoutInflater.from(context);
        this.mListInformation = mListInformation;
        this.context=context;
        this.act = act;
    }

    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylcer_row_menu,parent,false);
        return new MenuHolder(view);
    }

    public void onBindViewHolder(final MenuHolder holder, final int position) {
        holder.bind(mListInformation.get(position));

        // Creazione dello Spinner
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(context,
                R.array.numberOfQuantity, R.layout.spinner_element);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mQuantitySpinner.setAdapter(spinner_adapter);

        // Creazione di array di supporto allo Spinner
        mSpinnerValue = new String[getItemCount()];
        for(int i = 0; i < getItemCount(); i++) {
            mSpinnerValue[i] = "0";
        }

        // Gestione listener Spinner
        holder.mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Inserimento valore spinner nell array di supporto
                mSpinnerValue[holder.getAdapterPosition()] = holder.mQuantitySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Gestione listener per accedere alle Informazioni del singolo menù
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog dialog = new MenuDialog();
                dialog.show(act.getFragmentManager(),"Entro nel Dialog");
            }
        });
        animate(holder);
    }

    @Override
    public int getItemCount() {
        return mListInformation.size();
    }


    public String getSingleSpinnerValue(int position) {
        return mSpinnerValue[position];
    }

    public void animate(MenuHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }

    // Classe che dovrà gestire le varie View che verranno fornite dall'Adapter
    class MenuHolder extends RecyclerView.ViewHolder {
        private ImageView mInfo;
        private TextView mTitle, mPrice;
        private Spinner mQuantitySpinner;

        MenuHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameMenu);
            mInfo = (ImageView) itemView.findViewById(R.id.infoMenu);
            mPrice = (TextView) itemView.findViewById(R.id.priceMenu);
            mQuantitySpinner = (Spinner) itemView.findViewById(R.id.quantitySpinner);
        }

        void bind(SettingsElementRVMenu info) {
            mTitle.setText(info.getmName());
            mPrice.setText(info.getmPrice());
        }
    }
}