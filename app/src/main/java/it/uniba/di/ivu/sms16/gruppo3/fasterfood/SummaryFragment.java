package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

        final Switch switchSeats = (Switch) getView().findViewById(R.id.switchSeats);
        final LinearLayout layoutSeats = (LinearLayout) getView().findViewById(R.id.layoutSeats);

        switchSeats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (layoutSeats.getVisibility() == View.GONE)
                                                           layoutSeats.setVisibility(View.VISIBLE);
                                                       else
                                                           layoutSeats.setVisibility(View.GONE);
                                                   }
                                               }
        );

        TextView avaiableSeats = (TextView) getView().findViewById(R.id.avaiableSeats);
        //Non sono sicuro funzioni SEMPRE, dovrebbe andare un listener. Angelo
        if (Integer.valueOf(avaiableSeats.getText().toString()) < 10)
            avaiableSeats.setTextColor(Color.RED);
    }
}
