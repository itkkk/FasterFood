package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.RecyclerTouchListener;

public class OrdersFragment extends Fragment {

    private RecyclerAdapterRVOrders adapter;
    private RecyclerView order_list;
    private TextView filter;
    static private OrderList orderList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_orders, container, false);

        //get recyclerview with adapter to get data.
        //data are taken by a function getdata(), you should implement a method with real values
        order_list=(RecyclerView)layout.findViewById(R.id.recyclerViewOrders);


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

        orderList = ScambiaDati.getOrderList();
        if(orderList.getOrders() == null || orderList.getOrders().size() == 0){
            Snackbar.make(getView(), getResources().getString(R.string.orders_error),Snackbar.LENGTH_LONG).show();
        }
        else{
            adapter=new RecyclerAdapterRVOrders(getActivity(),orderList.getOrders());
            order_list.setAdapter(adapter);
            order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            order_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), order_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    OrderDialog orderDialog = new OrderDialog();
                    Bundle bundle = new Bundle();
                    if(orderList.getOrders().get(position).getStato().equals("aperto")){
                        bundle.putBoolean("state", false);
                    }else{
                        bundle.putBoolean("state", true);
                    }
                    bundle.putInt("position",position);
                    bundle.putString("price", orderList.getOrders().get(position).getTotale());
                    orderDialog.setArguments(bundle);
                    orderDialog.show(getFragmentManager(), null);
                }
                @Override
                public void onLongClick(View view, int position) {}
            })
            );
        }
    }

    public void open_filter_frag(){
        ((HomeActivity)getActivity()).setBackArrow();

        Fragment fragment = new FilterOrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    static OrderList getOrderList(){
        return orderList;
    }
}
