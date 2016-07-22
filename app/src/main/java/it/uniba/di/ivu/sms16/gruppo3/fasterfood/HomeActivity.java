package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;


import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen.LocalsFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen.LoginFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen.OrdersFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.SearchFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.settings_screen.AccSettingsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static  boolean STARTED = false;
    private static boolean IS_BACK_ARROW_SHOWED = false;
    private FrameLayout layout;
    public NavigationView mNavigationView;
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private Fragment fragment;
    private ArrayList<String> menuSpinnerValue;
    private OrderList orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(savedInstanceState != null){
            menuSpinnerValue = savedInstanceState.getStringArrayList("menuSpinnerValue");
        }

        layout = (FrameLayout) findViewById(R.id.fragment);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        checkLogged();
        setupToolbar();
        setupNavigationDrawer();

        Fragment currFrag = getFragmentManager().findFragmentById(R.id.fragment);
        if(!STARTED || currFrag == null) {
            setupFragment();
        }
        if(IS_BACK_ARROW_SHOWED){
            setBackArrow();
        }
    }

    private void setupFragment(){
        fragment = new SearchFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, fragment,"searchFragment").commit();
        STARTED = true;
    }

    //replace frag with order frag
    private void set_orderFrag(){
        Fragment fragment = new OrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //replace frag with locals frag
    private void set_localsFrag(){
        Fragment fragment = new LocalsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //replace frag with settings frag
    public void set_settingsFrag(){
        Fragment fragment = new AccSettingsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //inizializza toolbar
    private void setupToolbar(){
        //myToolbar.setLogo(R.mipmap.ic_launcher);
        //myToolbar.setTitleTextColor(Color.WHITE);
        //myToolbar.setLogo(R.drawable.fasterfood);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
    }

    //inizializza navigationDrawer
    private void setupNavigationDrawer() {
        //mostro il drawer e l'icona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open_drawer, R.string.closed_drawer);
        mDrawerToggle.syncState();
        //aggiungo i listener (per animazione e click su menu)
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        //nasconde il navigation drawer se è aperto
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            //altrimenti torna al fragment precedente (o chiude l'app) gestendo l'animazione della freccia nel caso in cui sia mostrata
            if(getFragmentManager().getBackStackEntryCount() == 0)
                super.onBackPressed();
            else {
                getFragmentManager().popBackStackImmediate(); //elimina il fragment corrente dal backstack
            }
            Fragment currFrag = getFragmentManager().findFragmentById(R.id.fragment);
            if((currFrag instanceof SearchFragment || currFrag instanceof OrdersFragment ||
                    currFrag instanceof LocalsFragment || currFrag instanceof AccSettingsFragment) && IS_BACK_ARROW_SHOWED)
            {
                changeDrawerIcon();
            }
        }
    }

    //shows the back Arrow
    public void setBackArrow(){
        animateDrawerIndicator(true);
        IS_BACK_ARROW_SHOWED = true;
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //disabilita swipe per aprire il drawer
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mDrawerToggle.syncState();
    }

    //animate the hamburger icon into a back arrow (
    public void animateDrawerIndicator(final boolean shouldAnimate) {
        ValueAnimator anim;
        if(shouldAnimate) {
            anim = ValueAnimator.ofFloat(0, 1);
        } else {
            anim = ValueAnimator.ofFloat(1, 0);
        }
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                // You should get the drawer layout and
                // toggler from your fragment to make the animation
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(shouldAnimate) {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }

    //Things to do when a drawer item is selected
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Home selected
        if(id == R.id.nav_home) {
            if (!item.isChecked()) {
                //pulisco backstack
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                setupFragment();
            }
        }
        // Order selected
        else if(id == R.id.nav_orders){
            if(!item.isChecked()){
                /*se l'utente è loggato visualizzo i suoi ordini, traamite un thread che blocco per 500 ms in quanto
                  l'accesso al db è asincrono quindi non ho immediatamente i risultati
                */
                if(AppConfiguration.isLogged()){
                    Thread retrieveOrder = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            orderList = new DbController().getOrders(getResources().getString(R.string.db_orders));
                            try{
                                sleep(500);
                            }catch (InterruptedException e){
                            }finally {
                                ScambiaDati.setOrderList(orderList);
                                set_orderFrag();
                            }
                        }
                    };
                    retrieveOrder.start();
                }
                //altrimenti mostro una snackbar di errore
                else{
                    Snackbar.make(layout, getResources().getString(R.string.order_error), Snackbar.LENGTH_LONG);
                }
            }
        }
        // Locals selected
        else if(id == R.id.nav_locals){
            if(!item.isChecked()){
                set_localsFrag();
            }
        }
        // Account settings selected
        else if(id == R.id.nav_account_settings){
			if(!item.isChecked())
                set_settingsFrag();
        }
        // login-logout selected
        else if(id == R.id.nav_logout){
            if(AppConfiguration.isLogged()){
                //effettuo logout
                FirebaseAuth.getInstance().signOut();
                AppConfiguration.setLogged(false);
                Snackbar.make(layout, getResources().getString(R.string.logged_out), Snackbar.LENGTH_LONG).show();
                //nascondo il menu delle impostazioni e mostro l'icona di login
                Menu mMenu = mNavigationView.getMenu();
                MenuItem accountSettings = mMenu.findItem(R.id.nav_account_settings)
                        .setVisible(false);
                MenuItem logout = mMenu.findItem(R.id.nav_logout)
                        .setTitle("Login")
                        .setIcon(R.drawable.ic_login);
                //Ripristino le textView nell'header
                View header = mNavigationView.getHeaderView(0);
                TextView txtUser = (TextView) header.findViewById(R.id.txtUsername);
                txtUser.setText(getResources().getString(R.string.guest_string));
                TextView txtEmail = (TextView) header.findViewById(R.id.txtEmail);
                txtEmail.setText(getResources().getString(R.string.guest_notification));
                //torno a searchFragment e pulisco il backstack
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                setupFragment();
            }
            else{
                setBackArrow();
                getFragmentManager().beginTransaction().replace(R.id.fragment, new LoginFragment()).addToBackStack(null).commit();
            }
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //per chiudere correttamente l'app
    @Override
    protected void onStop() {
        super.onStop();
    }

    public void checkLogged(){
        if(AppConfiguration.isLogged() != null && AppConfiguration.isLogged()){
            //mostro menu opazioni e il tasto Logout
            Menu mMenu = mNavigationView.getMenu();
            MenuItem accountSettings = mMenu.findItem(R.id.nav_account_settings)
                    .setVisible(true);
            MenuItem logout = mMenu.findItem(R.id.nav_logout)
                    .setTitle("Logout")
                    .setIcon(R.drawable.ic_logout);
            //modifico le text view nel navigaation header e mostro l'email
            View header = mNavigationView.getHeaderView(0);
            TextView txtUser = (TextView) header.findViewById(R.id.txtUsername);
            String[] user = AppConfiguration.getUser().split("@"); //divide in due stringhre quando trova @
            txtUser.setText(user[0]);
            TextView txtEmail = (TextView) header.findViewById(R.id.txtEmail);
            txtEmail.setText(AppConfiguration.getUser());
        }
    }

    public void changeDrawerIcon(){
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        IS_BACK_ARROW_SHOWED = false;
        animateDrawerIndicator(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED ); //abiita lo swipe per aprire il drawer
        mDrawerToggle.syncState();
    }

    public ArrayList<String> getMenuSpinnerValue() {
        return menuSpinnerValue;
    }

    public void setMenuSpinnerValue(ArrayList<String> menuSpinnerValue) {
        this.menuSpinnerValue = menuSpinnerValue;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("menuSpinnerValue",menuSpinnerValue);
    }
}
