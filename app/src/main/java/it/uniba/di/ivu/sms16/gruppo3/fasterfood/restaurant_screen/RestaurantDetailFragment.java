package it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen.MenuFragment;


public class RestaurantDetailFragment extends Fragment{

    HomeActivity activity;
    String restaurantName;
    Button btnMenu;
    TextView txtHours, txtReview,txtNumReview, txtState, txtStreet, txtCity, txtRating;
    RatingBar ratingBarTotal;
    //private LoadMap loadMap;
    MapFragment mapFragment;
    LatLng restaurantLatLng;

    Bundle bundle;

    @Override
    public void onResume() {
        super.onResume();
        MenuItem menu = activity.mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (HomeActivity)getActivity();
        bundle = getArguments();
        restaurantName = bundle.getString("restaurantName");
        activity.setTitle(restaurantName);

        btnMenu = (Button) getView().findViewById(R.id.btnMenu);
        txtState = (TextView) getView().findViewById(R.id.txtState);
        txtHours = (TextView) getView().findViewById(R.id.txtHours);
        txtReview = (TextView) getView().findViewById(R.id.txtReview);
		txtStreet = (TextView) getView().findViewById(R.id.txtStreet);
        txtCity = (TextView) getView().findViewById(R.id.txtCity);
        txtRating = (TextView) getView().findViewById(R.id.txtRating);
        ratingBarTotal = (RatingBar) getView().findViewById(R.id.ratingBarTotal);
        txtNumReview = (TextView) getView().findViewById(R.id.txtNumReview);

		txtCity.setText(bundle.getString("restaurantCity"));
        txtStreet.setText(bundle.getString("restaurantAddress"));

		txtState.setText(getString(R.string.opened_now));
        txtState.setTextColor(getResources().getColor(R.color.green));
        txtHours.setText(" - Monday 9.00 / 23.00"); //esempio
        ratingBarTotal.setFocusable(false);
        ratingBarTotal.setRating(bundle.getFloat("restaurantRating"));

        int review = bundle.getInt("restaurantReviews") ;
        txtNumReview.setText(getResources().getQuantityString(R.plurals.numberReviews, review, review));

        txtRating.setText(ratingBarTotal.getRating() + "/5");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new MenuFragment())
                        .addToBackStack("")
                        .commit();
            }
        });

        setupMap();
    }

    private void setupMap() {

        // Geodecoding
        Geocoder geocoder = new Geocoder(getActivity());

        Address address = null; // android.location.Address

        String locationName = bundle.getString("restaurantAddress") + " " + bundle.getString("restaurantCity");

        try {
            address = geocoder.getFromLocationName(locationName, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fine Geodecoding

        restaurantLatLng = new LatLng(address.getLatitude(), address.getLongitude());

        mapFragment = new MapFragment();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {

                map.moveCamera(CameraUpdateFactory.newLatLng(restaurantLatLng));

                map.addMarker(new MarkerOptions()
                        .position(restaurantLatLng)
                        .snippet("Snippet here.")
                        .title("Ruvo di Pooglia"));

                map.animateCamera(CameraUpdateFactory.zoomTo(13));
            }
        });

        getFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
    }

    private class LoadMap extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
