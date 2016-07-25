package it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalService;

import java.util.ArrayList;


import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen.LoginFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen.PayPalPay;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen.PaymentsActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.SearchFragment;

public class SummaryFragment extends Fragment {
    private ArrayList<String> nameList;
    private ArrayList<String> priceList;
    private ArrayList<String> quantityList;
    private ArrayList<Integer> positionList;
    private boolean open;
    private boolean connected;
    private String localName;
    private String chain;
    private boolean updating;  //se è 1 stiamo aggiornando un vecchio ordine
    private boolean state; //se è 0 l'ordine è aperto quindi mostro il layout di default, se è 1 l'ordine è chiuso quindi nascondo prenotazione posti e pulsanti di pagamento
    private String date;
    private TextView avaiableSeats;
    private Spinner spinnerSeats;
    private float tot = 0;
    private static final int PAYMENT_REQUEST_CODE = 1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

        //leggo gli elementi dal bundle
        nameList = bundle.getStringArrayList("nameList");
        priceList = bundle.getStringArrayList("priceList");
        quantityList = bundle.getStringArrayList("quantityList");
        positionList = bundle.getIntegerArrayList("positionList");
        open = bundle.getBoolean("open");
        localName = bundle.getString("name");
        chain = bundle.getString("chain");
        updating = bundle.getBoolean("updating");
        state = bundle.getBoolean("state");
        if(updating){
            date=bundle.getString("date");
        }


        final TextView txtTotale = (TextView) getView().findViewById(R.id.txtTotale);
        RecyclerView summaryRV = (RecyclerView) getView().findViewById(R.id.summaryRV);
        Switch switchSeats = (Switch) getView().findViewById(R.id.switchSeats);
        CardView cardSeats = (CardView) getView().findViewById(R.id.view2);
        final LinearLayout layoutSeats = (LinearLayout) getView().findViewById(R.id.layoutSeats);
        final Button btnPayNow = (Button) getView().findViewById(R.id.btnPayNow);
        final Button btnPayCassa = (Button) getView().findViewById(R.id.btnPayCassa);
        spinnerSeats = (Spinner) getView().findViewById(R.id.spinnerSeats);
        avaiableSeats = (TextView) getView().findViewById(R.id.avaiableSeats);

        //imposto la recyclerview
        summaryRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        summaryRV.setLayoutManager(mLayoutManager);
        SummaryRVAdapter adapterSummaryRV = new SummaryRVAdapter(nameList,quantityList,priceList);
        summaryRV.setAdapter(adapterSummaryRV);

        //calcolo il totale e lo visualizzo

        for(int i=0; i < adapterSummaryRV.getItemCount(); i++) {
            tot += adapterSummaryRV.getSubTotal(i);
        }
        txtTotale.setText("€ " + String.valueOf(tot));

        //se l'ordine è chiuso non visualizzo i pulsanti per pagare (ho già pagato)
        if(state){
            btnPayCassa.setVisibility(View.GONE);
            btnPayNow.setVisibility(View.GONE);
        }
        //se sto aggiornando un ordine non permetto la prenotazione di nuovi posti
        if(updating){
            cardSeats.setVisibility(View.GONE);
        }
        //altrimenti imposto il alyout per selezionare i posti
        else {
            avaiableSeats.setText(getArguments().getString("posti"));
            if (Integer.valueOf(avaiableSeats.getText().toString()) < 10)
                avaiableSeats.setTextColor(Color.RED);
            switchSeats.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                layoutSeats.setVisibility(View.VISIBLE);

                                String[] seats = new String[11];
                                for(int i = 0; i<11; i++){
                                    seats[i]=String.valueOf(i);
                                }
                                ArrayAdapter<String> adapterSeats =
                                        new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, seats);
                                spinnerSeats.setAdapter(adapterSeats);
                            } else {
                                layoutSeats.setVisibility(View.GONE);
                            }
                        }
                    }
            );

        }


        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payment = new Intent(getActivity(), PaymentsActivity.class);
                payment.putExtra("totale", tot);
                startActivityForResult(payment, PAYMENT_REQUEST_CODE);

                Intent intent = new Intent(getActivity(), PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());
                getActivity().startService(intent);
            }
        });
        btnPayCassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay("aperto", tot);
            }
        });
    }

    void pay(final String state, final float ptot){
        Thread payment = new Thread(){
            @Override
            public void run() {
                super.run();
                boolean conn = DbController.isConnected(getResources().getString(R.string.db_connected));
                connected = conn;
                //controllo se il locale è aperto
                if(!open){
                    Snackbar.make(getView(),getResources().getString(R.string.closed),Snackbar.LENGTH_LONG).show();
                }
                //controllo se l'utente è loggato
                else if(!AppConfiguration.isLogged()) {
                    Snackbar.make(getView(), getResources().getString(R.string.not_logged), Snackbar.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
                //controllo la connessione al db
                else if(!connected) {
                    Snackbar.make(getView(),getResources().getString(R.string.not_connected),Snackbar.LENGTH_LONG).show();
                }
                //scrivo l'ordine sul db o aggiorno l'ordine
                else{
                    DbController dbController = new DbController();
                    if(!updating) {
                        //scrivo l'ordine sul db
                        dbController.addOrder(getResources().getString(R.string.db_orders), state, String.valueOf(ptot), localName,
                                chain, nameList, quantityList, priceList, positionList);
                        //aggiorno i posti

                        String posti = getArguments().getString("posti");
                        int p = Integer.parseInt(posti);
                        if(spinnerSeats.getSelectedItem() != null){
                            p = Integer.parseInt(posti) - Integer.parseInt(spinnerSeats.getSelectedItem().toString());
                        }

                        dbController.setPosti(getResources().getString(R.string.db_locals), String.valueOf(p),
                                        getArguments().getInt("position"));
                    }
                    else{
                        //sto aggiornando un vecchio ordine
                        dbController.updateOrder(getResources().getString(R.string.db_orders), state, String.valueOf(ptot), localName,
                                chain, date, nameList, quantityList, priceList, positionList);
                    }
                    //torno a search fragment e pulisco il back stack
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((HomeActivity)getActivity()).changeDrawerIcon(); //cambio un elemento della ui quindi devo usare il thread principale
                        }
                    });
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment(),"searchFragment").commit();
                }
            }
        };
        payment.start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYMENT_REQUEST_CODE){
            if(resultCode == getActivity().RESULT_OK){
                pay("chiuso",tot);
                //Snackbar.make(getActivity().findViewById(R.id.fragment), "Hai pagato", Snackbar.LENGTH_LONG).show();
            }
            else{
                //mostrare snackbar errore pagamento
            }
        }
    }
}
