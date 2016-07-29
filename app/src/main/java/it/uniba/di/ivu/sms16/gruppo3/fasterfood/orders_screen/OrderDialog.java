package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderItem;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen.MenuFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen.AlarmNotification;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen.SummaryFragment;

public class OrderDialog extends DialogFragment {
    private Button viewOrder;
    private Button editOrder;
    private Button sendOrder;
    private boolean state;
    private OrderList orderList;
    private int position;

    private Activity activity;

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

        if(state)
            editOrder.setVisibility(View.GONE);

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
                transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_exit_right,
                        R.animator.slide_in_right,R.animator.slide_exit_left);
                transaction.replace(R.id.fragment, summaryFragment);
                transaction.addToBackStack(null);
                transaction.commit();


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
                            transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_exit_right,
                                    R.animator.slide_in_right,R.animator.slide_exit_left);
                            transaction.replace(R.id.fragment, menuFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                            getDialog().dismiss();
                        }
                    }
                };
                ((HomeActivity)getActivity()).setBackArrow();
                retriveMenu.start();
            }
        });

        NfcManager manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if (adapter == null)
            sendOrder.setVisibility(View.GONE); // Device doesn't support NFC. Button GONE.
        else if (adapter.isEnabled())
            sendOrder.setOnClickListener(new NFCEnabled());
        else
            sendOrder.setOnClickListener(new NFCDisabled());
    }

    class NFCEnabled implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            NFCDialog nfcDialog = new NFCDialog();
            nfcDialog.show(getFragmentManager(), "");
            Bundle bundle = new Bundle();
            bundle.putString("date", orderList.getOrders().get(position).getData());
            bundle.putString("price", getArguments().getString("price"));
            nfcDialog.setArguments(bundle);


            //TODO: MONDELLI AGGIUSTA LE NOTIFICHE
            LocalsList localsList = ScambiaDati.getLocalsList();
            if(localsList.getLocals() != null){
                Bundle reviewBundle = new Bundle();
                for(int i = 0; i < localsList.getLocals().size(); i++){
                    if(localsList.getLocals().get(i).getNome().equals(orderList.getOrders().get(position).getLocale())){
                        reviewBundle.putString("NameLocal", localsList.getLocals().get(i).getNome());
                        reviewBundle.putFloat("RatingLocal", localsList.getLocals().get(i).getValutazione());
                        reviewBundle.putInt("NumberRating", localsList.getLocals().get(i).getNumVal());
                    }
                }
                AlarmNotification alarmNotification = new AlarmNotification();
                alarmNotification.setAlarm(getActivity(), reviewBundle);
            }
            //QUI FINISCONO LE NOTIFICHE

            getDialog().dismiss();
        }
    }

    class NFCDisabled implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
            // Snackbar's animations won't work if any Accessibility Manager is ENABLED.
            Snackbar.make(getActivity().getCurrentFocus(), "NFC and Android Beam disabled", Snackbar.LENGTH_LONG)
                    .setAction("Enable them now", new NFCIntentSetting())
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
    }

    class NFCIntentSetting implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            activity.startActivityForResult(new Intent(Settings.ACTION_NFC_SETTINGS), -1);
        }
    }

    // Do not delete
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
