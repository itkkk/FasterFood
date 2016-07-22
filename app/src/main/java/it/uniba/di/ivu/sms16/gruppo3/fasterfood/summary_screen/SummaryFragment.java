package it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class SummaryFragment extends Fragment {
    ArrayList<String> nameList;
    ArrayList<String> priceList;
    ArrayList<String> quantityList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        nameList = bundle.getStringArrayList("nameList");
        priceList = bundle.getStringArrayList("priceList");
        quantityList = bundle.getStringArrayList("quantityList");


        TextView txtTotale = (TextView) getView().findViewById(R.id.txtTotale);
        RecyclerView summaryRV = (RecyclerView) getView().findViewById(R.id.summaryRV);
        summaryRV.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        summaryRV.setLayoutManager(mLayoutManager);

        SummaryRVAdapter adapterSummaryRV = new SummaryRVAdapter(nameList,quantityList,priceList);
        summaryRV.setAdapter(adapterSummaryRV);

        //calcolo il totale
        float tot = 0;
        for(int i=0; i < adapterSummaryRV.getItemCount(); i++) {
            tot += adapterSummaryRV.getSubTotal(i);
        }
        txtTotale.setText("€ " + String.valueOf(tot));

        Switch switchSeats = (Switch) getView().findViewById(R.id.switchSeats);
        final LinearLayout layoutSeats = (LinearLayout) getView().findViewById(R.id.layoutSeats);
        final Button btnPayNow = (Button) getView().findViewById(R.id.btnPayNow);
        final Button btnPayCassa = (Button) getView().findViewById(R.id.btnPayCassa);

        switchSeats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   layoutSeats.setVisibility(View.VISIBLE);
                   Spinner spinnerSeats = (Spinner) getView().findViewById(R.id.spinnerSeats);
                   String[] seats = {"1", "2"}; // Arriva fino a max posti disponibili
                   ArrayAdapter<String> adapterSeats =
                           new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, seats);
                   spinnerSeats.setAdapter(adapterSeats);

               } else {
                   layoutSeats.setVisibility(View.GONE);
               }
           }
       }
        );

        TextView avaiableSeats = (TextView) getView().findViewById(R.id.avaiableSeats);
        if (Integer.valueOf(avaiableSeats.getText().toString()) < 10)
            avaiableSeats.setTextColor(Color.RED);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialog payDialog = new PayDialog();
                payDialog.show(getFragmentManager(), null);
            }
        });

        btnPayCassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Qui parte qualcosa per NFC/App cassa", Toast.LENGTH_LONG).show();
            }
        });
    }




}
