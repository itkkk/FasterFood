package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

// Creazione dell'Adapter, accedere ai dati
public class RecyclerAdapterRVMenu extends RecyclerView.Adapter<RecyclerAdapterRVMenu.MenuHolder> {

    private List<SettingsElementRVMenu> mListInformation;
    private Context context;
    private Activity act;

    public RecyclerAdapterRVMenu(Context context, List<SettingsElementRVMenu> mListInformation, Activity act) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mListInformation = mListInformation;
        this.context=context;
        this.act = act;
    }

    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylcer_row_menu,parent,false);
        return new MenuHolder(view);
    }

    public void onBindViewHolder(MenuHolder holder, int position) {
        holder.bind(mListInformation.get(position));
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


    // Classe che dovrà gestire le varie View che verranno fornite dall'Adapter
    class MenuHolder extends RecyclerView.ViewHolder {
        private ImageView  mInfo;
        private TextView mTitle, mPrice, mMinusQuantity, mPlusQuantity;
        private EditText mQuantity;
        private int mCountQuantity = 0;

        MenuHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameMenu);
            mInfo = (ImageView) itemView.findViewById(R.id.infoMenu);
            mPrice = (TextView) itemView.findViewById(R.id.priceMenu);
            mMinusQuantity = (TextView) itemView.findViewById(R.id.minusQuantity);
            mQuantity = (EditText) itemView.findViewById(R.id.quantityText);
            mPlusQuantity = (TextView) itemView.findViewById(R.id.plusQuantity);
        }

        void bind(SettingsElementRVMenu info) {
            mTitle.setText(info.getmName());
            mPrice.setText(info.getmPrice());

            mPlusQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCountQuantity++;
                    mQuantity.setText(String.valueOf(mCountQuantity));
                }
            });

            mMinusQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(mQuantity.getText().toString()) || mCountQuantity ==1) {
                        mCountQuantity = 0;
                        mQuantity.setText(null); // Eventualmente migliorare questo aspetto
                    }
                    else {
                        mQuantity.setText(String.valueOf(--mCountQuantity));
                    }
                }
            });
        }
    }
    
    public void animate(MenuHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }
}
