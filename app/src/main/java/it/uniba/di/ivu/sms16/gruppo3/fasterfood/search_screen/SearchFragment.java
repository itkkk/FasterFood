package it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;


public class SearchFragment extends Fragment{
    private HomeActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;
    private LocalsList localsList;
    private List<File> logoList;
    private ClearableAutoCompleteTextView citySearch;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HomeActivity) getActivity();
        activity.setTitle(R.string.app_name);

        citySearch = (ClearableAutoCompleteTextView) getView().findViewById(R.id.citySearch);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        menu = activity.mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(true);

        sharedPreferences = getActivity()
                .getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_name)
                    ,Context.MODE_PRIVATE);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        //ottengo la lista dei locali
        localsList = ScambiaDati.getLocalsList();
        if (localsList.getLocals().size() == 0){
            Snackbar.make(getView(),"The local db is empty. Please connect to a network and restart the app to refresh",
                    Snackbar.LENGTH_INDEFINITE).show();
        }

        /*//creo l'adapter passando la lista dei locali
        mAdapter = new AdapterRestaurantList(localsList.getLocals(), activity.getApplicationContext());
        recyclerView.setAdapter(mAdapter);*/

        //aggiungo il listener alla recyclerview
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position){

                AdapterRestaurantList a = ((AdapterRestaurantList)recyclerView.getAdapter());

                Toast.makeText(getActivity(), a.getNome(position), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < localsList.getLocals().size(); i++)
                    if (localsList.getLocals().get(i).getNome().equals(a.getNome(position)))
                        position = i;

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
                bundle.putInt("position", position);

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
        /*                                            *
        *               Spaghetti Code                *
        *                                             *
        * https://www.youtube.com/watch?v=-5wpm-gesOY *
        *                                             *
        *      In questa funzione risiede il male.    *
        *                                             *
        *                                             */

        //popolazione spinner con loghi
        createLogoList();
        AdapterLogosSpinner adapterLogosSpinner = new AdapterLogosSpinner(activity.getApplicationContext(), logoList);
        spinner.setAdapter(adapterLogosSpinner);

        final List<Local> filteredLocalsList = new ArrayList<>();

        citySearch.setText(sharedPreferences.getString(getResources().getString(R.string.shared_pref_cities), ""));

        int chain = spinnerPositionFromName(
                sharedPreferences.getString(getResources().getString(R.string.shared_pref_chains), ""));
        spinner.setSelection(chain);

        // Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filteredLocalsList.clear();

                if (position == 0){

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
                        if (ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(fixSpinnerPosition(spinner.getSelectedItemPosition())).getNome()) &&
                                localsList.getLocals().get(i).getCitta().equals(citySearch.getText().toString()))
                            if (!filteredLocalsList.contains(ScambiaDati.getLocalsList().getLocals().get(i)))
                                filteredLocalsList.add(ScambiaDati.getLocalsList().getLocals().get(i));
                    } else {
                        if (ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(fixSpinnerPosition(spinner.getSelectedItemPosition())).getNome()))
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
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                filteredLocalsList.clear();
                if (spinner.getSelectedItemPosition() == 0){
                    for (int i = 0; i < localsList.getLocals().size(); i++)
                        if (localsList.getLocals().get(i).getCitta().equals((String)parent.getItemAtPosition(position)))
                            filteredLocalsList.add(localsList.getLocals().get(i));
                } else {
                    for (int i = 0; i < localsList.getLocals().size(); i++)
                        if (localsList.getLocals().get(i).getCitta().equals((String)parent.getItemAtPosition(position)) &&
                                ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(fixSpinnerPosition(spinner.getSelectedItemPosition())).getNome()))
                            filteredLocalsList.add(localsList.getLocals().get(i));
                }

                mAdapter = new AdapterRestaurantList(filteredLocalsList, activity.getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }
        });

        citySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (citySearch.getText().toString().isEmpty()){
                    filteredLocalsList.clear();
                    if (spinner.getSelectedItemPosition() == 0){
                        mAdapter = new AdapterRestaurantList(localsList.getLocals(), activity.getApplicationContext());
                    } else {
                        for (int i = 0; i < ScambiaDati.getLocalsList().getLocals().size(); i++)
                            if (ScambiaDati.getLocalsList().getLocals().get(i).getCategoria().equals(ScambiaDati.getChainList().getChains().get(fixSpinnerPosition(spinner.getSelectedItemPosition())).getNome()))
                                if (!filteredLocalsList.contains(ScambiaDati.getLocalsList().getLocals().get(i)))
                                    filteredLocalsList.add(ScambiaDati.getLocalsList().getLocals().get(i));
                        mAdapter = new AdapterRestaurantList(filteredLocalsList, activity.getApplicationContext());

                    }
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private int spinnerPositionFromName(String string) {
        int chain = 0;

        for (int i = 0; i < ScambiaDati.getChainList().size(); i++)
            if (string.equals(ScambiaDati.getChainList().getChains().get(i).getNome()))
                chain = i;

        if (string.equals("Bacio di Latte"))
            chain = 3;

        return chain;
    }

    private int fixSpinnerPosition(int position){
        if (position == 3)
            return 0;
        return position;
    }

    void createLogoList(){
        logoList = new ArrayList<>();
        logoList.add(ScambiaDati.getLogo(3));
        logoList.add(ScambiaDati.getLogo(0));
        logoList.add(ScambiaDati.getLogo(1));
        logoList.add(ScambiaDati.getLogo(2));
    }
}