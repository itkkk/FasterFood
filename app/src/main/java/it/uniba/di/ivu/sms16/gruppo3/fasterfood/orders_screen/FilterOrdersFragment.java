package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;


//Shows Filters from orders frag.
//Filters available are status and chain.
//You should create var for all radiobottons
public class FilterOrdersFragment extends Fragment {

    private ChainList chainList;
    private int chain_number;
    String[] filterArray;
    //back to order frag
    private Button save_btn;
    private RadioGroup dinamicRG;
    View layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         layout=inflater.inflate(R.layout.fragment_filter_orders, container, false);

        save_btn=(Button)layout.findViewById(R.id.filter_btn);
        dinamicRG=(RadioGroup) layout.findViewById(R.id.radio2);

        //dati dal db
        chainList = ScambiaDati.getChainList();
        //creazione array per filter
        filterArray = new String[(chainList.getChains().size())];
        for(int i=0; i<chainList.getChains().size(); i++){
            filterArray[i] = chainList.getChains().get(i).getNome();
        }
        chain_number=(chainList.getChains().size());
        addRadioBtn(chain_number);

                save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_orderFrag();
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void set_orderFrag(){
        Fragment fragment = new OrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addRadioBtn(int number) {
        RadioButton dinamicRB[]=new RadioButton[number];
        for (int i = 0; i < number; i++) {
            dinamicRB[i] = new RadioButton(getActivity());
            dinamicRB[i].setText(filterArray[i]);
            dinamicRG.addView(dinamicRB[i]);
        }
    }
}
