package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class LocalsFragment extends Fragment {

    private RecyclerAdapterRVLocals adapter;
    private RecyclerView local_list;
    private static View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_locals, container, false);
        local_list=(RecyclerView)layout.findViewById(R.id.recyclerViewLocals);

        adapter=new RecyclerAdapterRVLocals(getActivity(),getData());
        local_list.setAdapter(adapter);
        local_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        Spinner spinner = (Spinner) layout.findViewById(R.id.spinnerLocals);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chains, R.layout.spinner_element);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static List<SettingsElementRVLocals> getData(){
        List<SettingsElementRVLocals> data=new ArrayList<>();
        int icons=R.drawable.ic_image_broken;
        String[] names={"Local 1","Local 2","Local 3","Local 4",};
        String[] addresses={"Bari,via Europa","Bari,via Italia","Foggia,via Udine","Taranto,viale Salvo"};
        int[] rate_icons={R.drawable.ic_liked,R.drawable.ic_liked,R.drawable.ic_disliked,R.drawable.ic_disliked};
        String[] rates={layout.getResources().getString(R.string.like_txt),
                layout.getResources().getString(R.string.like_txt),
                layout.getResources().getString(R.string.dislike_txt),
                layout.getResources().getString(R.string.dislike_txt)};

        for(int i=0;i<names.length;i++){
            SettingsElementRVLocals current=new SettingsElementRVLocals();
            current.icon=icons;
            current.local_name=names[i];
            current.local_address=addresses[i];
            current.rate_txt=rates[i];
            current.rate_icon=rate_icons[i];
            data.add(current);
        }
        return data;
    }
}
