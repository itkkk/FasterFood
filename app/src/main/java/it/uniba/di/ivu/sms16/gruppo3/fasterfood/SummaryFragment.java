package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView summaryRV = (RecyclerView) getView().findViewById(R.id.summaryRV);
        summaryRV.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        summaryRV.setLayoutManager(mLayoutManager);


        final String[] daCanc = {"The Walking Burger"};
        RecyclerView.Adapter adapterSummaryRV = new SummaryRVAdapter(daCanc, getActivity().getApplicationContext());
        summaryRV.setAdapter(adapterSummaryRV);

        Switch switchSeats = (Switch) getView().findViewById(R.id.switchSeats);
        final LinearLayout layoutSeats = (LinearLayout) getView().findViewById(R.id.layoutSeats);
        final EditText editTextSeats = (EditText) getView().findViewById(R.id.editTextSeats);
        final TextView zero_seats = (TextView) getView().findViewById(R.id.txtViewMessage);


        switchSeats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   layoutSeats.setVisibility(View.VISIBLE);
                   editTextSeats.addTextChangedListener(new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                       }

                       @Override
                       public void onTextChanged(CharSequence s, int start, int before, int count) {

                       }

                       @Override
                       public void afterTextChanged(Editable s) {
                           if (s.toString().isEmpty() || Integer.valueOf(s.toString()) < 1)
                               zero_seats.setVisibility(View.VISIBLE);
                           else
                               zero_seats.setVisibility(View.GONE);
                       }
                   });
               } else {
                   layoutSeats.setVisibility(View.GONE);
                   zero_seats.setVisibility(View.GONE);
               }

           }
       }
        );

        TextView avaiableSeats = (TextView) getView().findViewById(R.id.avaiableSeats);
        //Non sono sicuro funzioni SEMPRE, dovrebbe andare un listener. Angelo
        if (Integer.valueOf(avaiableSeats.getText().toString()) < 10)
            avaiableSeats.setTextColor(Color.RED);
    }
}
