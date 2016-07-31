package it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

    private SharedPreferences prefs;
    private List<String> localsList_topref;
    private Set<String> localsSet_topref;
    private List<Frequency_localOrd> locals_freq;

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

        prefs=this.getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_locals_name)
                , Context.MODE_PRIVATE);

        load_set_locals();

        final MenuItem menu = ((HomeActivity)getActivity()).mNavigationView.getMenu().findItem(R.id.nav_locals);
        menu.setChecked(true);

        setupSpinner();

        //ottengo la lista dei locali
        localsList = ScambiaDati.getLocalsList();

        filter_chain = spinner.getSelectedItem().toString();
        setFilteredList(filter_chain);

        if(filteredlocalsList.getLocals() == null || filteredlocalsList.getLocals().size() == 0){
            Snackbar.make(getView(),getResources().getString(R.string.no_local_screen),Snackbar.LENGTH_LONG).show();
        }else{
            adapter=new RecyclerAdapterRVLocals(getActivity(),filteredlocalsList.getLocals());
            local_list.setAdapter(adapter);
            local_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            Snackbar.make(getView(),getResources().getString(R.string.sort_message_locals),Snackbar.LENGTH_SHORT).show();
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

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_exit_right,
                            R.animator.slide_in_right,R.animator.slide_exit_left);
                    transaction.replace(R.id.fragment, restaurantDetailFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    /*
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, restaurantDetailFragment)
                            .addToBackStack(null)
                            .commit();*/
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
                    Snackbar.make(getView(),getResources().getString(R.string.no_local_screen),Snackbar.LENGTH_LONG).show();
                    //vuoto
                    adapter=new RecyclerAdapterRVLocals(getActivity(),filteredlocalsList.getLocals());
                    local_list.setAdapter(adapter);
                    local_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else{
                    adapter=new RecyclerAdapterRVLocals(getActivity(),filteredlocalsList.getLocals());
                    local_list.setAdapter(adapter);
                    local_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    Snackbar.make(getView(),getResources().getString(R.string.sort_message_locals),Snackbar.LENGTH_SHORT).show();
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
        LocalsList filteredlocalsList_temp= new LocalsList();
        for(Local i : localsList.getLocals())
        {
            if(filter.equals(getActivity().getResources().getString(R.string.all_chains))){
                for(String j : localsList_topref)
                {
                    if(i.getNome().equals(j)){
                        filteredlocalsList_temp.addLocal(i);
                        break;
                    }
                }
            }else{
                if(filter.equals(i.getCategoria())){
                    for(String j : localsList_topref)
                    {
                        if(i.getNome().equals(j)){
                            filteredlocalsList_temp.addLocal(i);
                            break;
                        }
                    }
                }
            }
        }
        //riempo lista di frequenze
        locals_freq=new ArrayList<>();
        locals_freq.clear();
        for(Local i : localsList.getLocals()){
            locals_freq.add(new Frequency_localOrd(i.getNome(),prefs.getInt(i.getNome(),0)));
            //Log.i("prova",i.getNome() + " = " + Integer.toString(prefs.getInt(i.getNome(),0)));
        }
        Collections.sort(locals_freq, new Comparator<Frequency_localOrd>() {
            @Override
            public int compare(Frequency_localOrd lhs, Frequency_localOrd rhs) {
                return lhs.getFreq().compareTo(rhs.getFreq());
            }
        });
        Collections.reverse(locals_freq);
        //aggiungi locali in ordine di frequenza
        for (Frequency_localOrd i : locals_freq){
            if(i.getFreq()!=0){
                for(Local j : filteredlocalsList_temp.getLocals()){
                    if(j.getNome().equals(i.getName())){
                        filteredlocalsList.addLocal(j);
                        break;
                    }
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

    private void load_set_locals(){
        localsSet_topref = prefs.getStringSet(getActivity().getResources().getString(R.string.shared_pref_key_value),null);
        if(localsSet_topref!=null){
            localsList_topref = new ArrayList<String>(localsSet_topref);
        }else{
            localsList_topref = new ArrayList<>();
        }
    }
}
