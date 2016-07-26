package it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.RecyclerTouchListener;

//locals frag, it shows all locals user reviewed or selected in past.
//all locals have a name,address,and a image+string for past feedback.
//to be implemented:listener on locals to go to restaurant detail
public class LocalsFragment extends Fragment {

    private RecyclerAdapterRVLocals adapter;
    private RecyclerView local_list;
    private static View layout;
    private Spinner spinner;
    private ChainList chainList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_locals, container, false);

        //get recyclerview with adapter to get data.
        //data are taken by a function getdata(), you should implement a method with real values
        local_list=(RecyclerView)layout.findViewById(R.id.recyclerViewLocals);
        adapter=new RecyclerAdapterRVLocals(getActivity(),getData());
        local_list.setAdapter(adapter);
        local_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //gestione spinner
        spinner = (Spinner) layout.findViewById(R.id.spinnerLocals);
        //dati dal db
        chainList = ScambiaDati.getChainList();

        //creazione array per spinner
        String[] spinnerArray = new String[(chainList.getChains().size())+1];
        spinnerArray[0] = getActivity().getResources().getString(R.string.all_chains);
        for(int i=0; i<chainList.getChains().size(); i++){
            spinnerArray[i+1] = chainList.getChains().get(i).getNome();
        }
        //adapter spinner
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_element, spinnerArray);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MenuItem menu = ((HomeActivity)getActivity()).mNavigationView.getMenu().findItem(R.id.nav_locals);
        menu.setChecked(true);

        local_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), local_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String localName = ((RecyclerAdapterRVLocals)local_list.getAdapter()).getLocalName(position);
                Bundle bundle = new Bundle();
                bundle.putString("restaurantName", localName);
                RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
                restaurantDetailFragment.setArguments(bundle);

                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, restaurantDetailFragment)
                        .addToBackStack(null)
                        .commit();
                menu.setChecked(false);
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));    }

    //getdata() function to get all data.
    //it's a list of settingselementRVLocals : a obj with all elements in the card
    public static List<SettingsElementRVLocals> getData(){

        List<SettingsElementRVLocals> data=new ArrayList<>();
        //used a single icon to be updated with real icons (array)
        int icons=R.drawable.ic_image_broken;
        String[] names={"Local 1","Local 2","Local 3","Local 4",};
        String[] addresses={"Bari,via Europa","Bari,via Italia","Foggia,via Udine","Taranto,viale Salvo"};

        //for to fill data
        for(int i=0;i<names.length;i++){
            SettingsElementRVLocals current=new SettingsElementRVLocals();
            current.icon=icons;
            current.local_name=names[i];
            current.local_address=addresses[i];
            data.add(current);
        }
        return data;
    }
}
