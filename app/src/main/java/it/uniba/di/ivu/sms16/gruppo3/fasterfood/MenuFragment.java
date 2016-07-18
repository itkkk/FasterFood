package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_menu,container,false);

        RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerViewMenu);
        RecyclerAdapterRVMenu adapterRVMenu = new RecyclerAdapterRVMenu(getActivity(),getData(),getActivity());
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapterRVMenu);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnPurchase = (Button) getView().findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new SummaryFragment()) // TODO Non va fatto new SummaryFragment
                        .addToBackStack("")
                        .commit();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    static List<SettingsElementRVMenu> getData(){
        List<SettingsElementRVMenu> data = new ArrayList<>();
        String[] title = {"FormaggioBurger", "Tatanka di Tonno", "Patatine Dritte"};
        String[] prezzo = {"2.5", "3.4", "5.4"};
        for(int i = 0; i < title.length; i++) {
            SettingsElementRVMenu current = new SettingsElementRVMenu();
            current.setmName(title[i]);
            current.setmPrice(prezzo[i]);
            data.add(current);
        }
        return data;
    }
}
