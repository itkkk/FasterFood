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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen.LocalsFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen.LoginFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen.OrdersFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.SearchFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.settings_screen.AccSettingsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static  boolean STARTED = false;
    private static boolean IS_LOGIN_FRAGMENT_ATTACHED = false;
    private static boolean IS_BACK_ARROW_SHOWED = false;

    FrameLayout layout;
    public NavigationView mNavigationView;
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layout = (FrameLayout) findViewById(R.id.fragment);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        checkLogged();
        setupToolbar();
        setupNavigationDrawer();

        if(!STARTED) {
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

    //inizializza toolbar
    private void setupToolbar(){
        myToolbar.setLogo(R.mipmap.ic_launcher);
        //myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
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
        //nasconde il navigation drawer se è aperto oppure chiama onBackPressed di default
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            if(!mDrawerToggle.isDrawerIndicatorEnabled() && !IS_LOGIN_FRAGMENT_ATTACHED){
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                IS_BACK_ARROW_SHOWED = false;
                animateDrawerIndicator(false);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED ); //abiita lo swipe per aprire il drawer
                mDrawerToggle.syncState();
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
        // Orders selected
        else if(id == R.id.nav_orders){
            if(!item.isChecked()){
                set_orderFrag();
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
        Menu mMenu = mNavigationView.getMenu();
        MenuItem home = mMenu.findItem(R.id.nav_home);
        if(home.isChecked()) {
            STARTED = false;
        }
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

    //replace frag with settings frag
    public void set_settingsFrag(){
        Fragment fragment = new AccSettingsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setIsLoginFragmentAttached(boolean value){
        IS_LOGIN_FRAGMENT_ATTACHED = value;
    }

    public boolean isIsLoginFragmentAttached(){
        return IS_LOGIN_FRAGMENT_ATTACHED;
    }
}
