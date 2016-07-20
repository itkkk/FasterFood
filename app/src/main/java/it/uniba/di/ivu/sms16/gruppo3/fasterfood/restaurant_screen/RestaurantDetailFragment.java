package it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen.MenuFragment;


public class RestaurantDetailFragment extends Fragment {

    private HomeActivity activity;
    private String restaurantName;
    private Button btnMenu;
    private TextView txtHours, txtReview,txtNumReview, txtState, txtStreet, txtCity, txtRating;
    private RatingBar ratingBarTotal;
    //private LoadMap loadMap;

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
        final Bundle bundle = getArguments();
        restaurantName = bundle.getString("restaurantName");
        activity.setTitle(restaurantName);

        new LoadMap().execute();

        btnMenu = (Button) getView().findViewById(R.id.btnMenu);
        txtState = (TextView) getView().findViewById(R.id.txtState);
        txtHours = (TextView) getView().findViewById(R.id.txtHours);
        txtReview = (TextView) getView().findViewById(R.id.txtReview);
		txtStreet = (TextView) getView().findViewById(R.id.txtStreet);
        txtCity = (TextView) getView().findViewById(R.id.txtCiy);
        txtRating = (TextView) getView().findViewById(R.id.txtRating);
        ratingBarTotal = (RatingBar) getView().findViewById(R.id.ratingBarTotal);
        txtNumReview = (TextView) getView().findViewById(R.id.txtNumReview);
		
		txtState.setText(getString(R.string.opened_now));
        txtState.setTextColor(getResources().getColor(R.color.green));
        txtHours.setText(" - Monday 9.00 / 23.00");//esempio
        ratingBarTotal.setFocusable(false);
        ratingBarTotal.setRating(3); //esempio
        txtNumReview.setText("Based on 10 reviews");//esempio
        txtRating.setText(ratingBarTotal.getRating() + "/5");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String category = bundle.getString("restaurantChain");
                Thread retriveMenu = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DbController dbController = new DbController();
                        Menu menu =  dbController.queryMenu(getResources().getString(R.string.db_menus).toString() + category);
                        try{
                            sleep(200);
                        }catch (InterruptedException e){}
                        finally {
                            ScambiaDati.setMenu(menu);
                            MenuFragment menuFragment = new MenuFragment();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("chain", category);
                            menuFragment.setArguments(bundle1);
                            getActivity().getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, menuFragment)
                                    .addToBackStack("")
                                    .commit();
                        }
                    }
                };
                retriveMenu.start();
            }
        });




    }

    private class LoadMap extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            getChildFragmentManager().beginTransaction().replace(R.id.map, new MapFragment(),"map").commit();
            return null;
        }
    }
}
