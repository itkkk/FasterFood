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
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.Calendar;

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
    FasterFoodMapFragment mapFragment;
    LatLng restaurantLatLng;

    ScrollView scrollView;

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
        scrollView = (ScrollView) getView().findViewById(R.id.restaurantDetailScrollView);

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

        txtHours.setText(bundle.getString("restaurantHours"));
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

        setupCalendar();

        new LoadMap().execute();
    }

    private void setupCalendar() {
        Calendar rightNow = Calendar.getInstance();
        Calendar open = Calendar.getInstance();
        Calendar close = Calendar.getInstance();

        String hours[] = bundle.getString("restaurantHours").trim().split("-");

        String hoursOpen[] = hours[0].trim().split("\\.");
        String hoursClosed[] = hours[1].trim().split("\\.");

        for (int i = 0; i < 2; i++)
            if (hoursOpen[i].startsWith("0"))
                hoursOpen[i] = hoursOpen[i].substring(1 , hoursOpen[i].length()).trim();

        for (int i = 0; i < 2; i++)
            if (hoursClosed[i].startsWith("0"))
                hoursClosed[i] = hoursClosed[i].substring(1 , hoursClosed[i].length()).trim();

        open.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hoursOpen[0]));
        open.set(Calendar.MINUTE, Integer.valueOf(hoursOpen[1]));
        close.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hoursClosed[0]));
        close.set(Calendar.MINUTE, Integer.valueOf(hoursClosed[1]));

        if (rightNow.after(open) && rightNow.before(close)){
            txtState.setText(getString(R.string.opened_now));
            txtState.setTextColor(getResources().getColor(R.color.green));
        }
        else{
            txtState.setText(getString(R.string.closed_now));
            txtState.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private class LoadMap extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            // Geodecoding
            Geocoder geocoder = new Geocoder(activity);

            Address address = null; // android.location.Address

            String locationName = bundle.getString("restaurantAddress") + " " + bundle.getString("restaurantCity");

            try {
                address = geocoder.getFromLocationName(locationName, 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Fine Geodecoding
            if (address == null){

            }
            else {
                restaurantLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mapFragment = new FasterFoodMapFragment();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap map) {
                                map.moveCamera(CameraUpdateFactory.newLatLng(restaurantLatLng));
                                map.addMarker(new MarkerOptions()
                                        .position(restaurantLatLng)
                                        .title(restaurantName));
                                map.animateCamera(CameraUpdateFactory.zoomTo(13)); // 15
                            }
                        });

                        mapFragment.setListener(new FasterFoodMapFragment.OnTouchListener() {
                            @Override
                            public void onTouch() {
                                scrollView.requestDisallowInterceptTouchEvent(true);
                            }
                        });
                        getFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
                    }
                });
            }
            return null;
        }
    }
}
