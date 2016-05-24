package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
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
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.add(R.id.searchFragment, new SearchFragment());
        FT.commit();

    }

    private void setupToolbar(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        myToolbar.setLogo(R.mipmap.ic_launcher);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
    }

    private void setupNavigationDrawer(){
        //ottengo il riferimento al layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mostro il drawer el'icona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open_drawer, R.string.closed_drawer);
        mDrawerToggle.syncState();
        //popolazione listview
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<SettingsElement> settingsList = new ArrayList<>(); //lista delle impostazioni che la listview visualizzerà

        settingsList.add(new SettingsElement(R.drawable.ic_home_black_36dp, getResources().getString(R.string.home_settings)));
        settingsList.add(new SettingsElement(R.drawable.ic_content_paste_black_36dp, getResources().getString(R.string.orders_settings)));
        settingsList.add(new SettingsElement(R.drawable.ic_store_mall_directory_black_36dp, getResources().getString(R.string.locals_settings)));

        /*******************************************
         * Questa è la lista che rappresenta la sorgente dei dati della listview
         * ogni elemento è una mappa(chiave->valore)
         *******************************************/
        ArrayList<HashMap<String, Object>> data=new ArrayList<>();
        for(int i=0;i<settingsList.size();i++){
            SettingsElement p = settingsList.get(i);// per ogni elemento del drawer

            HashMap<String,Object> settingsMap=new HashMap<>();//creiamo una mappa di valori

            settingsMap.put("image", p.getPhotoRes()); // per la chiave image, inseriamo la risorsa dell immagine
            settingsMap.put("name", p.getName()); // per la chiave name,l'informazine sul nome
            data.add(settingsMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }
        String[] from={"image","name"}; //dai valori contenuti in queste chiavi
        int[] to={R.id.settingImage,R.id.settingName};//agli id delle view

        //costruzione dell adapter e collegamento alla listview
        mDrawerList.setAdapter(new SimpleAdapter(getApplicationContext(), data, R.layout.custom_row,from, to));
    }

    //prova prova prova
}
