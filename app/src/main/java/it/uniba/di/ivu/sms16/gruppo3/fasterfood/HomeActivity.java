package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView mNavigationView;
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    Fragment fragment;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupNavigationDrawer();

        if(savedInstanceState != null){
            started = savedInstanceState.getBoolean("started");
        }
        if(!started) {
            setupFragment();
        }
    }

    private void setupFragment(){
        fragment = new SearchFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
        started = true;
    }

    private void setupToolbar(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
    }

    private void setupNavigationDrawer() {
        //ottengo il riferimento al layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mostro il drawer e l'icona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open_drawer, R.string.closed_drawer);
        mDrawerToggle.syncState();

        //aggiungo i listner (per animazione e click su menu)
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    //nasconde il navigation drawer se è aperto oppure chiama onBackPressed di default
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //azioni da eseguire quando viene cliccato un menu
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
        } else if(id == R.id.nav_orders){
            set_orderFrag();
        } else if(id == R.id.nav_locals){
            set_localsFrag();
        } else if(id == R.id.nav_account_settings){

        } else if(id == R.id.nav_logout){
            item.setTitle("Logout");
            item.setIcon(R.drawable.ic_logout);
            //item.setCheckable(false);
            Menu mMenu = mNavigationView.getMenu();
            MenuItem accountSettings = mMenu.findItem(R.id.nav_account_settings);
            accountSettings.setVisible(true);
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //replace frag with order frag
    public void set_orderFrag(){
        Fragment fragment = new OrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //replace frag with locals frag
    public void set_localsFrag(){
        Fragment fragment = new LocalsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("started",started);
    }
}
