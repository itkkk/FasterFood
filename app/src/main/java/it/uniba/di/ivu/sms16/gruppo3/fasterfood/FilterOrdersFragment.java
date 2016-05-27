package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FilterOrdersFragment extends Fragment {

    private Button save_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_filter_orders, container, false);
        save_btn=(Button)layout.findViewById(R.id.filter_btn);
                save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_orderFrag();
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void set_orderFrag(){
        Fragment fragment = new OrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
