package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;


import android.animation.Animator;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderItem;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen.MenuFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen.SummaryFragment;

public class OrderDialog extends DialogFragment {
    private Button viewOrder;
    private Button editOrder;
    private Button sendOrder;
    private boolean state;
    private OrderList orderList;
    private int position;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_dialog, null);

        viewOrder = (Button) view.findViewById(R.id.btnViewOrder);
        editOrder = (Button) view.findViewById(R.id.btnEdit);
        sendOrder = (Button) view.findViewById(R.id.btnSendOrder);

        state = getArguments().getBoolean("state");
        position = getArguments().getInt("position");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(state){
            editOrder.setVisibility(View.GONE);
        }
        orderList = OrdersFragment.getOrderList();

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> nameList = new ArrayList<>();
                ArrayList<String> priceList = new ArrayList<>();
                ArrayList<String>  quantityList = new ArrayList<>();
                ArrayList<Integer> positionList = new ArrayList<>();
                for(int i = 0; i < orderList.getOrders().get(position).getItems().size(); i++){
                    OrderItem item = orderList.getOrders().get(position).getItems().get(i);
                    nameList.add(item.getNome());
                    priceList.add(item.getPrezzo());
                    quantityList.add(item.getQuantita());
                    positionList.add(item.getPosition().intValue());
                }
                SummaryFragment summaryFragment = new SummaryFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("nameList", nameList);
                bundle.putStringArrayList("priceList", priceList);
                bundle.putStringArrayList("quantityList", quantityList);
                bundle.putIntegerArrayList("positionList",positionList);
                bundle.putBoolean("open", true);
                bundle.putString("name", orderList.getOrders().get(position).getLocale());
                bundle.putString("chain", orderList.getOrders().get(position).getCatena());
                bundle.putString("date",orderList.getOrders().get(position).getData());
                bundle.putBoolean("updating", true);
                bundle.putBoolean("state",state);
                ((HomeActivity)getActivity()).setBackArrow();
                summaryFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_down,R.animator.slide_exit_up,
                        R.animator.slide_up,R.animator.slide_exit_down);
                transaction.replace(R.id.fragment, summaryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                /*
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment,summaryFragment)
                        .addToBackStack("")
                        .commit();*/

                getDialog().dismiss();
            }
        });

        editOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String category = orderList.getOrders().get(position).getCatena();
                Thread retriveMenu = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DbController dbController = new DbController();
                        Menu menu =  dbController.queryMenu(getResources().getString(R.string.db_menus).toString() + category);
                        try{
                            sleep(500);
                        }catch (InterruptedException e){}
                        finally {
                            ScambiaDati.setMenu(menu);

                            //ripristono lo spinner
                            final ArrayList<String> spinnerValue = new ArrayList<>();
                            for(int i = 0; i < menu.getMenu().size();i++){
                                spinnerValue.add("0");
                            }
                            for(int i = 0; i < orderList.getOrders().get(position).getItems().size(); i++){
                                OrderItem item = orderList.getOrders().get(position).getItems().get(i);
                                spinnerValue.add(item.getPosition().intValue(),item.getQuantita());
                            }
                            ((HomeActivity)getActivity()).setMenuSpinnerValue(spinnerValue);

                            //poasso i valori al bundle per menuFragment
                            MenuFragment menuFragment = new MenuFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("chain", category);
                            bundle.putString("name", orderList.getOrders().get(position).getLocale());
                            bundle.putBoolean("open", true);
                            bundle.putBoolean("updating",true);
                            bundle.putString("date",orderList.getOrders().get(position).getData());

                            menuFragment.setArguments(bundle);

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.animator.slide_down,R.animator.slide_exit_up,
                                    R.animator.slide_up,R.animator.slide_exit_down);
                            transaction.replace(R.id.fragment, menuFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            /*
                            getActivity().getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, menuFragment)
                                    .addToBackStack("")
                                    .commit();*/

                            getDialog().dismiss();
                        }
                    }
                };
                ((HomeActivity)getActivity()).setBackArrow();
                retriveMenu.start();
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"send order", Toast.LENGTH_LONG).show();
            }
        });
    }
}
