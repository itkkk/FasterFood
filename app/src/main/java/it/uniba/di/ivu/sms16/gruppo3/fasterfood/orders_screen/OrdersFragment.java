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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

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
            Snackbar.make(getView(), getResources().getString(R.string.sort_message_order),Snackbar.LENGTH_SHORT).show();
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
        Calendar new_date=new GregorianCalendar();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        ArrayList<Calendar> lista_ord_date = new ArrayList<>();
        filteredOrderList = new OrderList();
        OrderList filteredOrderListTemp = new OrderList();
        for(Order i : orderList.getOrders()){
            //aggiustare con strings
            if(first.equals("Open")){
                if(i.getStato().equals("aperto")){
                    if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                        filteredOrderListTemp.addOrder(i);
                        new_date = set_newDate(i.getData());
                        lista_ord_date.add(new_date);
                    }else{
                        if(second.equals(i.getCatena())){
                            filteredOrderListTemp.addOrder(i);
                            new_date = set_newDate(i.getData());
                            lista_ord_date.add(new_date);
                        }
                    }
                }
            }else if(first.equals("Closed")){
                if(i.getStato().equals("chiuso")){
                    if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                        filteredOrderListTemp.addOrder(i);
                        new_date = set_newDate(i.getData());
                        lista_ord_date.add(new_date);
                    }else{
                        if(second.equals(i.getCatena())){
                            filteredOrderListTemp.addOrder(i);
                            new_date = set_newDate(i.getData());
                            lista_ord_date.add(new_date);
                        }
                    }
                }
            }else{
                if(second.equals(getActivity().getResources().getString(R.string.no_filter))){
                    filteredOrderListTemp.addOrder(i);
                    new_date = set_newDate(i.getData());
                    lista_ord_date.add(new_date);
                }else{
                    if(second.equals(i.getCatena())){
                        filteredOrderListTemp.addOrder(i);
                        new_date = set_newDate(i.getData());
                        lista_ord_date.add(new_date);
                    }
                }
            }
        }
        Collections.sort(lista_ord_date);
        Collections.reverse(lista_ord_date);
        for (Calendar i : lista_ord_date){
            String temp=build_old_date(i);
            int j = 0;
            while(!temp.equals(filteredOrderListTemp.getOrders().get(j).getData()) && j<filteredOrderListTemp.getOrders().size()-1){
                j++;
            }
            if(temp.equals(filteredOrderListTemp.getOrders().get(j).getData())){
                filteredOrderList.addOrder(filteredOrderListTemp.getOrders().get(j));
            }
        }
    }

    private Calendar set_newDate(String old_date){
        String temp=new String();
        Calendar newD = new GregorianCalendar();
        int ora;
        int i=0;
        //giorno
        while(old_date.charAt(i)!='-'){
            temp=temp + old_date.charAt(i);
            i++;
        }
        newD.set(Calendar.DAY_OF_MONTH,Integer.parseInt(temp));
        temp="";
        i++;
        //mese
        while(old_date.charAt(i)!='-'){
            temp=temp+old_date.charAt(i);
            i++;
        }
        newD.set(Calendar.MONTH,Integer.parseInt(temp)-1);
        temp="";
        i++;
        //anno
        while(old_date.charAt(i)!='_'){
            temp=temp+old_date.charAt(i);
            i++;
        }
        newD.set(Calendar.YEAR,Integer.parseInt(temp));
        temp="";
        i++;
        //ora
        while(old_date.charAt(i)!=':'){
            temp=temp+old_date.charAt(i);
            i++;
        }
        ora=Integer.parseInt(temp);
        temp="";
        i++;
        //minuti
        while(old_date.charAt(i)!=':'){
            temp=temp+old_date.charAt(i);
            i++;
        }
        newD.set(Calendar.MINUTE,Integer.parseInt(temp));
        temp="";
        i++;
        //secondi
        while(old_date.charAt(i)!='_'){
            temp=temp+old_date.charAt(i);
            i++;
        }
        newD.set(Calendar.SECOND,Integer.parseInt(temp));
        i++;
        if(old_date.charAt(i)=='1'){
            newD.set(Calendar.HOUR_OF_DAY,ora+12);
        }else{
            newD.set(Calendar.HOUR_OF_DAY,ora);
        }

        return newD;
    }

    private String build_old_date(Calendar date){
        String old_date=new String();
        old_date=Integer.toString(date.get(Calendar.DAY_OF_MONTH))+
                '-'+
                Integer.toString(date.get(Calendar.MONTH)+1)+
                '-'+
                Integer.toString(date.get(Calendar.YEAR))+
                '_';
        if(date.get(Calendar.HOUR_OF_DAY)>12 && date.get(Calendar.HOUR_OF_DAY)<24){
            old_date=old_date+
                    Integer.toString(date.get(Calendar.HOUR_OF_DAY)-12)+
                    ':'+
                    Integer.toString(date.get(Calendar.MINUTE))+
                    ':'+
                    Integer.toString(date.get(Calendar.SECOND))+
                    '_'+Integer.toString(1);
        }else if(date.get(Calendar.HOUR_OF_DAY)<12 && date.get(Calendar.HOUR_OF_DAY) >=1){
            old_date=old_date+
                    Integer.toString(date.get(Calendar.HOUR_OF_DAY))+
                    ':'+
                    Integer.toString(date.get(Calendar.MINUTE))+
                    ':'+
                    Integer.toString(date.get(Calendar.SECOND))+
                    '_'+Integer.toString(0);
        }else if(date.get(Calendar.HOUR_OF_DAY)==12){
            old_date=old_date+
                    Integer.toString(date.get(Calendar.HOUR_OF_DAY)-12)+
                    ':'+
                    Integer.toString(date.get(Calendar.MINUTE))+
                    ':'+
                    Integer.toString(date.get(Calendar.SECOND))+
                    '_'+Integer.toString(1);
        }else if(date.get(Calendar.HOUR_OF_DAY)==24){
            old_date=old_date+
                    Integer.toString(date.get(Calendar.HOUR_OF_DAY)-24)+
                    ':'+
                    Integer.toString(date.get(Calendar.MINUTE))+
                    ':'+
                    Integer.toString(date.get(Calendar.SECOND))+
                    '_'+Integer.toString(0);
        }
        return old_date;
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
