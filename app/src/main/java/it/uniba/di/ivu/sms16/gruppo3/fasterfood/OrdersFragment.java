package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerAdapterRVOrders adapter;
    private RecyclerView order_list;
    private TextView filter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_orders, container, false);

        //get recyclerview with adapter to get data.
        //data are taken by a function getdata(), you should implement a method with real values
        order_list=(RecyclerView)layout.findViewById(R.id.recyclerViewOrders);
        adapter=new RecyclerAdapterRVOrders(getActivity(),getData());
        order_list.setAdapter(adapter);
        order_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //filter text, it has a onclick to go to filter frag
        filter=(TextView)layout.findViewById(R.id.order_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_filter_frag();
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //getdata() function to get all data.
    //it's a list of settingselementRVOrders : a obj with all elements in the card
    public static List<SettingsElementRVOrders> getData(){
        List<SettingsElementRVOrders> data=new ArrayList<>();
        //used a single icon to be updated with real icons (array)
        int icons=R.drawable.ic_image_broken;
        String[] names={"Ordine 1","Ordine 2","Ordine 3","Ordine 4",};
        String[] states={"Stato:Aperto","Stato:Aperto","Stato:Aperto","Stato:Aperto",};
        String[] totals={"Totale:5$","Totale:1$","Totale:50$","Totale:5$",};

        //for to fill data
        for(int i=0;i<names.length;i++){
            SettingsElementRVOrders current=new SettingsElementRVOrders();
            current.image_ordId=icons;
            current.title_ord=names[i];
            current.tot_ord=totals[i];
            current.state_ord=states[i];
            current.btn_txt="Edit";
            data.add(current);
        }
        return data;
    }

    public void open_filter_frag(){
        ((HomeActivity)getActivity()).setBackArrow();
        Fragment fragment = new FilterOrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
