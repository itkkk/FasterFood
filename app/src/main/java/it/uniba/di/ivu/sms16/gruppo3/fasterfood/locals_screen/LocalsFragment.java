package it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.RecyclerTouchListener;

//locals frag, it shows all locals user reviewed or selected in past.
//all locals have a name,address,and a image+string for past feedback.
//to be implemented:listener on locals to go to restaurant detail
public class LocalsFragment extends Fragment {

    private HomeActivity activity;
    private RecyclerAdapterRVLocals adapter;
    private RecyclerView local_list;
    private static View layout;
    private Spinner spinner;
    private ChainList chainList;
    private LocalsList localsList;
    private LocalsList filteredlocalsList;
    private String filter_chain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_locals, container, false);
        activity = (HomeActivity) getActivity();

        //get recyclerview with adapter to get data.
        //data are taken by a function getdata(), you should implement a method with real values
        local_list=(RecyclerView)layout.findViewById(R.id.recyclerViewLocals);

        //GESTIONE SPINNER--------------------------------------------------------------------------
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

        setupSpinner();

        //ottengo la lista dei locali
        localsList = ScambiaDati.getLocalsList();

        filter_chain = spinner.getSelectedItem().toString();
        setFilteredList(filter_chain);

        if(filteredlocalsList.getLocals() == null || filteredlocalsList.getLocals().size() == 0){
            Snackbar.make(getView(),"The local db is empty. Please connect to a network and restart the app to refresh",
                    Snackbar.LENGTH_INDEFINITE).show();
        }else{
            adapter=new RecyclerAdapterRVLocals(getActivity(),filteredlocalsList.getLocals());
            local_list.setAdapter(adapter);
            local_list.setLayoutManager(new LinearLayoutManager(getActivity()));

            local_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), local_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    //String name = ((RecyclerAdapterRVLocals)local_list.getAdapter()).getLocalName(position);
                    Bundle bundle = new Bundle();

                    int new_position = setposition_list(position);

                    String name = localsList.getLocals().get(new_position).getNome();
                    String address = localsList.getLocals().get(new_position).getVia();
                    String city = localsList.getLocals().get(new_position).getCitta();
                    Float rating = localsList.getLocals().get(new_position).getValutazione();
                    Integer numberOfReviews = localsList.getLocals().get(new_position).getNumVal();
                    String hours = localsList.getLocals().get(new_position).getOrari();
                    String chain = localsList.getLocals().get(new_position).getCategoria();

                    bundle.putString("restaurantName", name);
                    bundle.putString("restaurantAddress", address);
                    bundle.putString("restaurantCity", city);
                    bundle.putFloat("restaurantRating", rating);
                    bundle.putInt("restaurantReviews", numberOfReviews);
                    bundle.putString("restaurantHours", hours);
                    bundle.putString("restaurantChain", chain);
                    bundle.putInt("position", new_position);

                    RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
                    restaurantDetailFragment.setArguments(bundle);

                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, restaurantDetailFragment)
                            .addToBackStack(null)
                            .commit();
                    menu.setChecked(false);
                    activity.setBackArrow();
                }

                @Override
                public void onLongClick(View view, int position) {}
            }));
        }
    }

    private void setupSpinner(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                filter_chain = spinner.getSelectedItem().toString();
                setFilteredList(filter_chain);
                if(filteredlocalsList.getLocals() == null || filteredlocalsList.getLocals().size() == 0){
                    Snackbar.make(getView(),"The local db is empty. Please connect to a network and restart the app to refresh",
                            Snackbar.LENGTH_INDEFINITE).show();
                }else{
                    adapter=new RecyclerAdapterRVLocals(getActivity(),filteredlocalsList.getLocals());
                    local_list.setAdapter(adapter);
                    local_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setFilteredList(String filter){
        filteredlocalsList= new LocalsList();
        for(Local i : localsList.getLocals())
        {
            if(filter.equals("All chains")){
                filteredlocalsList.addLocal(i);
            }else{
                if(filter.equals(i.getCategoria())){
                    filteredlocalsList.addLocal(i);
                }
            }
        }
    }

    private int setposition_list(int pos){
        int j=0;
        Local temp=filteredlocalsList.getLocals().get(pos);
        String b=temp.getNome();
        for(Local i : localsList.getLocals()){
            String a = i.getNome();
            if(a.equals(b)){
                pos=j;
                break;
            }
            j++;
        }
        return pos;
    }
}
