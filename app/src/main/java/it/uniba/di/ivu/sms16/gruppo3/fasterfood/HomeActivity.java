package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupNavigationDrawer();
        setupFragment();
    }

    private void setupFragment(){
        getFragmentManager().beginTransaction().add(R.id.searchFragment, new SearchFragment()).commit();
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
        //mostro il drawer e l'icona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open_drawer, R.string.closed_drawer);
        mDrawerToggle.syncState();

    }
}
