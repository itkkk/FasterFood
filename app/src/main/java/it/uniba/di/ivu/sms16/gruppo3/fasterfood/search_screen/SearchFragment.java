package it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen;

import android.app.Fragment;
import android.media.audiofx.AutomaticGainControl;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;


public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private HomeActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;
    private LocalsList localsList;
    private List<File> logoList;
    private AutoCompleteTextView citySearch;
    private RecyclerView recyclerView;
    private Spinner spinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        activity = (HomeActivity) getActivity();
        activity.setTitle(R.string.app_name);

        citySearch = (AutoCompleteTextView) getView().findViewById(R.id.citySearch);



        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        spinner = (Spinner) getView().findViewById(R.id.spinner);
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
        if(localsList.getLocals().size() == 0){
            Snackbar.make(getView(),"The local db is empty. Please connect to a network and restart the app to refresh",
                    Snackbar.LENGTH_INDEFINITE).show();
        }

        //creo l'adapter passando la lista dei locali
        mAdapter = new AdapterRestaurantList(localsList.getLocals(), activity.getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        //aggiungo il listener alla recyclerview
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String name = localsList.getLocals().get(position).getNome();
                String address = localsList.getLocals().get(position).getVia();
                String city = localsList.getLocals().get(position).getCitta();
                Float rating = localsList.getLocals().get(position).getValutazione();
                Integer numberOfReviews = localsList.getLocals().get(position).getNumVal();
                String hours = localsList.getLocals().get(position).getOrari();
				String chain = localsList.getLocals().get(position).getCategoria();

                Bundle bundle = new Bundle();

                bundle.putString("restaurantName", name);
                bundle.putString("restaurantAddress", address);
                bundle.putString("restaurantCity", city);
                bundle.putFloat("restaurantRating", rating);
                bundle.putInt("restaurantReviews", numberOfReviews);
                bundle.putString("restaurantHours", hours);
				bundle.putString("restaurantChain", chain);

                activity.setMenuSpinnerValue(null);

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

        setupSpinnerAndAutoCompleteTextView();
    }

    private void setupSpinnerAndAutoCompleteTextView() {
        final List<Local> filteredLocalsList = new ArrayList<>();

        // Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int position = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                this.position = position;
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                filteredLocalsList.clear();

                int k = position;

                if (position == 3){
                    k = 0;
                } else if (position == 0){
                    if (citySearch.getText().toString().equals("")){
                        mAdapter = new AdapterRestaurantList(ScambiaDati.getLocalsList().getLocals(), activity.getApplicationContext());
                        recyclerView.setAdapter(mAdapter);
                        return;
                    } else {
                        for (int i = 0; i < localsList.getLocals().size(); i++)
                            if (localsList.getLocals().get(i).getCitta().equals(citySearch.getText().toString())) {
                                filteredLocalsList.add(localsList.getLocals().get(i));
                                mAdapter = new AdapterRestaurantList(filteredLocalsList, activity.getApplicationContext());
                                recyclerView.setAdapter(mAdapter);
                            }
                        return;
                    }
                }

                for (int i = 0; i < ScambiaDati.getLocalsList().getLocals().size(); i++) {
                    if (!citySearch.getText().toString().equals("")) {
                        if (ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(k).getNome()) &&
                                localsList.getLocals().get(i).getCitta().equals(citySearch.getText().toString()))
                            if (!filteredLocalsList.contains(ScambiaDati.getLocalsList().getLocals().get(i)))
                                filteredLocalsList.add(ScambiaDati.getLocalsList().getLocals().get(i));
                    } else {
                        if (ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(k).getNome()))
                            if (!filteredLocalsList.contains(ScambiaDati.getLocalsList().getLocals().get(i)))
                                filteredLocalsList.add(ScambiaDati.getLocalsList().getLocals().get(i));
                    }
                }
                mAdapter = new AdapterRestaurantList(filteredLocalsList, activity.getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // AutoCompleteTextView
        final ArrayList<String> restaurantNames = new ArrayList<>();

        for (int i = 0; i < localsList.getLocals().size(); i++)
            if (!restaurantNames.contains(localsList.getLocals().get(i).getCitta()))
                restaurantNames.add(localsList.getLocals().get(i).getCitta());

        ArrayAdapter<String> adapterRestaurantNames =
                new ArrayAdapter<>(getActivity(),
                        R.layout.support_simple_spinner_dropdown_item,
                        restaurantNames);
        citySearch.setAdapter(adapterRestaurantNames);
        citySearch.setThreshold(1);

        citySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < localsList.getLocals().size(); i++)
                    if (localsList.getLocals().get(i).getCitta().equals((String)parent.getItemAtPosition(position)))
                            filteredLocalsList.add(localsList.getLocals().get(i));

                mAdapter = new AdapterRestaurantList(filteredLocalsList, activity.getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
    /*
    private void setupSpinner() {
        final List<Local> filteredByChainLocalsList = new ArrayList<>();
        final ChainList chainList = ScambiaDati.getChainList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Local> backupLocalList = new ArrayList<>();

                filteredByChainLocalsList.clear();

                int k = position;

                if (position == 3){
                    k = 0;
                } else if (position == 0){
                    mAdapter = new AdapterRestaurantList(backupLocalList, activity.getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                    return;
                }

                //Toast.makeText(getActivity(), chainList.getChains().get(k).getNome(),Toast.LENGTH_SHORT).show();

                for (int i = 0; i < backupLocalList.size(); i++)
                    if (backupLocalList.get(i).getCategoria().equals(chainList.getChains().get(k).getNome()))
                        filteredByChainLocalsList.add(backupLocalList.get(i));

                mAdapter = new AdapterRestaurantList(filteredByChainLocalsList, activity.getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    /*
    private void setupAutoComplete() {
        citySearch.setThreshold(1);

        final ArrayList<String> restaurantNames = new ArrayList<>();
        final List<Local> filteredByCityLocalsList = new ArrayList<>();

        // Popolazione AutoCompleteTextView
        for (int i = 0; i < localsList.getLocals().size(); i++)
            if (!restaurantNames.contains(localsList.getLocals().get(i).getCitta()))
                restaurantNames.add(localsList.getLocals().get(i).getCitta());

        ArrayAdapter<String> adapterRestaurantNames =
                new ArrayAdapter<>(getActivity(),
                        R.layout.support_simple_spinner_dropdown_item,
                        restaurantNames);
        citySearch.setAdapter(adapterRestaurantNames);

        citySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                filteredByCityLocalsList.clear();

                for (int i = 0; i < localsList.getLocals().size(); i++)
                    if (localsList.getLocals().get(i).getCitta().equals((String)parent.getItemAtPosition(position)) &&
                            localsList.getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(k).getNome())

                            )
                        filteredByCityLocalsList.add(localsList.getLocals().get(i));

                mAdapter = new AdapterRestaurantList(filteredByCityLocalsList, activity.getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }
        });

        citySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (citySearch.getText().toString().equals("")) {
                    mAdapter = new AdapterRestaurantList(localsList.getLocals(), activity.getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
*/
    void createLogoList(){
        logoList = new ArrayList<>();
        logoList.add(ScambiaDati.getLogo(3));
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