package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class SearchFragment extends Fragment {
    private HomeActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;
    private ScambiaDati scambiaDati;
    private LocalsList localsList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scambiaDati = ScambiaDati.getScambiaDati();
        activity = (HomeActivity) getActivity();
        activity.setTitle(R.string.app_name);

        menu = activity.mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(true);

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.chains, R.layout.spinner_element);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);


        //ottengo la lista dei locali
        localsList = scambiaDati.getLocalsList();

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