package it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen;

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

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;


public class SearchFragment extends Fragment {
    private HomeActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;
    private LocalsList localsList;
    private ChainList chainList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HomeActivity) getActivity();
        activity.setTitle(R.string.app_name);

        menu = activity.mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(true);

        //popolazione spinner
        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        //creo l'array di nomi
        chainList = ScambiaDati.getChainList();
        String[] spinnerArray = new String[(chainList.getChains().size())+1];
        spinnerArray[0] = activity.getResources().getString(R.string.all_chains);

        for(int i=0; i<chainList.getChains().size(); i++){
            spinnerArray[i+1] = chainList.getChains().get(i).getNome();
        }

        //assegno l'array all'adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity.getApplicationContext(),
                R.layout.spinner_element, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        //ottengo la lista dei locali
        localsList = ScambiaDati.getLocalsList();

        //creo l'adapter passando la lista dei locali
        mAdapter = new AdapterRestaurantList(localsList.getLocals(), activity.getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        //aggiungo il listener alla recyclerview
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String s = localsList.getLocals().get(position).getNome();
                String address = localsList.getLocals().get(position).getVia();
                String city = localsList.getLocals().get(position).getCitta();

                Bundle bundle = new Bundle();

                bundle.putString("restaurantName", s);
                bundle.putString("restaurantAddress", address);
                bundle.putString("restaurantCity", city);

                RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
                restaurantDetailFragment.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
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
//x
}