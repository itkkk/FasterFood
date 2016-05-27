package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

/**
 * Created by Angelo on 26/05/2016.
 */
public class RestaurantDetailFragment extends Fragment {

    private Button btnMenu;
    private TextView txtHours, txtReview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getFragmentManager().beginTransaction().add(R.id.map, new MapFragment()).commit();
        btnMenu = (Button) getView().findViewById(R.id.btnMenu);
        txtHours = (TextView) getView().findViewById(R.id.txtHours);
        txtReview = (TextView) getView().findViewById(R.id.txtReview);
        txtHours.setText("Ristorante aperto dalle: ");
        txtReview.setText("Recensioni... ");
    }
}
