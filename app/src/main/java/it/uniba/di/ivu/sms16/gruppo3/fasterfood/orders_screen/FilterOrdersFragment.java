package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    private RadioButton dinamicRB[];
    private RadioButton rad1no;
    private RadioButton rad1open;
    private RadioButton rad1closed;
    private RadioButton rad2no;
    View layout;
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         layout=inflater.inflate(R.layout.fragment_filter_orders, container, false);

        //INIZIALIZZAZIONE ITEM BASE
        //recupero file preferences specifico per i settings
        prefs=this.getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_filter_name)
                , Context.MODE_PRIVATE);

        save_btn=(Button)layout.findViewById(R.id.filter_btn);
        dinamicRG=(RadioGroup) layout.findViewById(R.id.radio2);
        rad1no=(RadioButton) layout.findViewById(R.id.radio1no);
        rad1open=(RadioButton) layout.findViewById(R.id.radio1open);
        rad1closed=(RadioButton) layout.findViewById(R.id.radio1closed);
        rad2no=(RadioButton) layout.findViewById(R.id.radio2no);

        //dati dal db
        chainList = ScambiaDati.getChainList();
        //creazione array per filter
        filterArray = new String[(chainList.getChains().size())];
        for(int i=0; i<chainList.getChains().size(); i++){
            filterArray[i] = chainList.getChains().get(i).getNome();
        }
        chain_number=(chainList.getChains().size());
        addRadioBtn(chain_number);

        setChecks();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getActivity().getResources().getString(R.string.shared_pref_first_check),
                        findCheck1());
                editor.putString(getActivity().getResources().getString(R.string.shared_pref_second_check),
                        findCheck2(chain_number));
                editor.apply();
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
        dinamicRB = new RadioButton[number];
        for (int i = 0; i < number; i++) {
            dinamicRB[i] = new RadioButton(getActivity());
            dinamicRB[i].setText(filterArray[i]);
            dinamicRG.addView(dinamicRB[i]);
        }
    }

    private void setChecks(){
        String first;
        String second;

        first=prefs.getString(getActivity().getResources().getString(R.string.shared_pref_first_check),
                getActivity().getResources().getString(R.string.no_filter));
        second=prefs.getString(getActivity().getResources().getString(R.string.shared_pref_second_check),
                getActivity().getResources().getString(R.string.no_filter));

        //prima ricerca
        if(rad1no.getText().equals(first)){
            rad1no.setChecked(true);
        }else if(rad1open.getText().equals(first)){
            rad1open.setChecked(true);
        }else{
            rad1closed.setChecked(true);
        }

        //seconda ricerca
        if(rad2no.getText().equals(second)){
            rad2no.setChecked(true);
        }else{
            int i = 0;
            while(dinamicRB[i].getText().equals(second)){
                i++;
            }
            dinamicRB[i].setChecked(true);
        }
    }

    private String findCheck1(){
        String first_check;

        if(rad1no.isChecked()){
            first_check=rad1no.getText().toString();
        }else if(rad1open.isChecked()){
            first_check=rad1open.getText().toString();
        }else{
            first_check=rad1closed.getText().toString();
        }
        return first_check;
    }

    private String findCheck2(int number){
        String second_check;
        if(rad2no.isChecked()){
            second_check=rad2no.getText().toString();
        }else {
            int i = 0;
            while(!dinamicRB[i].isChecked()){
                i++;
            }
            second_check=dinamicRB[i].getText().toString();
        }
        return second_check;
    }
}
