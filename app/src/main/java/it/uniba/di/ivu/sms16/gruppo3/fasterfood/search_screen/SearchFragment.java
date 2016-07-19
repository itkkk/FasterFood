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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;


public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private HomeActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;
    private LocalsList localsList;
    private List<File> logoList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HomeActivity) getActivity();
        activity.setTitle(R.string.app_name);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        menu = activity.mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(true);

        //popolazione spinner con loghi
        createLogoList();
        AdapterLogosSpinner adapterLogosSpinner = new AdapterLogosSpinner(activity.getApplicationContext(), logoList);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapterLogosSpinner);


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

                Bundle bundle = new Bundle();
                bundle.putString("restaurantName", s);
                RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
                restaurantDetailFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
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

    void createLogoList(){
        logoList = new ArrayList<>();
        logoList.add(ScambiaDati.getLogo(0));
        logoList.add(ScambiaDati.getLogo(1));
        logoList.add(ScambiaDati.getLogo(2));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //fare la ricerca
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}