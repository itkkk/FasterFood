package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Order;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen.AlarmNotification;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.RecyclerTouchListener;

public class OrdersFragment extends Fragment {

    private RecyclerAdapterRVOrders adapter;
    private RecyclerView order_list;
    private TextView filter;
    static private OrderList orderList;
    private OrderList filteredOrderList;
    private TextView subtitle_order;
    SharedPreferences prefs;
    String first;
    String second;
    View layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_orders, container, false);

        prefs=this.getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_filter_name)
                , Context.MODE_PRIVATE);

        subtitle_order=(TextView) layout.findViewById(R.id.order_subtitle);
        //get recyclerview with adapter to get data.
        //data are taken by a function getdata(), you should implement a method with real values
        order_list = (RecyclerView)layout.findViewById(R.id.recyclerViewOrders);

        first=prefs.getString(getActivity().getResources().getString(R.string.shared_pref_first_check),
                getActivity().getResources().getString(R.string.no_filter));
        second=prefs.getString(getActivity().getResources().getString(R.string.shared_pref_second_check),
                getActivity().getResources().getString(R.string.no_filter));

        setupSubtitle(first,second);

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

        setFilteredList();

        if(filteredOrderList.getOrders() == null || filteredOrderList.getOrders().size() == 0){
            Snackbar.make(getView(), getResources().getString(R.string.orders_error),Snackbar.LENGTH_LONG).show();
        }
        else{
            adapter=new RecyclerAdapterRVOrders(getActivity(),filteredOrderList.getOrders());
            order_list.setAdapter(adapter);
            order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            order_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), order_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    OrderDialog orderDialog = new OrderDialog();
                    Bundle bundle = new Bundle();

                    int new_position = setposition_list(position);

                    if(orderList.getOrders().get(new_position).getStato().equals("aperto")){
                        bundle.putBoolean("state", false);
                    }else{
                        bundle.putBoolean("state", true);
                    }

                    bundle.putInt("position",new_position);

                    //bundle.putInt("position",position);
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
        transaction.setCustomAnimations(R.animator.slide_in_right,R.animator.slide_exit_left,
        R.animator.slide_in_left,R.animator.slide_exit_right);
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    static OrderList getOrderList(){
        return orderList;
    }

    private void setupSubtitle(String first, String second){
        String before;
        if(first.equals(getActivity().getResources().getString(R.string.no_filter))&&
                second.equals(getActivity().getResources().getString(R.string.no_filter))){
            subtitle_order.setText(getActivity().getResources().getString(R.string.orders_subtitle));
        }else{
            if(!first.equals(getActivity().getResources().getString(R.string.no_filter))){
                subtitle_order.setText(getActivity().getResources().getString(R.string.order_subtitle_first_only)
                        + " " + first);
                if(!second.equals(getActivity().getResources().getString(R.string.no_filter))){
                    before=subtitle_order.getText().toString();
                    subtitle_order.setText(before + getActivity().getResources().getString(R.string.order_subtitle_both)
                            + " " + second);
                }
            }else{
                if(!second.equals(getActivity().getResources().getString(R.string.no_filter))){
                  subtitle_order.setText(getActivity().getResources().getString(R.string.order_subtitle_second_only)
                          + " " + second);
                }
            }
        }
    }

    private void setFilteredList(){
        filteredOrderList = new OrderList();
        for(Order i : orderList.getOrders()){
            //aggiustare con strings
            if(first.equals("Open")){
                if(i.getStato().equals("aperto")){
                    if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                        filteredOrderList.addOrder(i);
                    }else{
                        if(second.equals(i.getCatena())){
                            filteredOrderList.addOrder(i);
                        }
                    }
                }
            }else if(first.equals("Closed")){
                if(i.getStato().equals("chiuso")){
                    if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                        filteredOrderList.addOrder(i);
                    }else{
                        if(second.equals(i.getCatena())){
                            filteredOrderList.addOrder(i);
                        }
                    }
                }
            }else{
                if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                    filteredOrderList.addOrder(i);
                }else{
                    if(second.equals(i.getCatena())){
                        filteredOrderList.addOrder(i);
                    }
                }
            }
        }
    }

    private int setposition_list(int pos){
        int j=0;
        Order temp=filteredOrderList.getOrders().get(pos);
        String b=temp.getData()+temp.getTotale();
        for(Order i : orderList.getOrders()){
            String a = i.getData()+i.getTotale();
            if(a.equals(b)){
                pos=j;
                break;
            }
            j++;
        }
        return pos;
    }
}
