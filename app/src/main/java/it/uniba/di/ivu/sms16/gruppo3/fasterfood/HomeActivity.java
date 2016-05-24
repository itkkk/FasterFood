package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open_drawer, R.string.closed_drawer){

        };
        mDrawerToggle.syncState();
        //popolazione listview
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<SettingsElement> settingsList = new ArrayList<>(); //lista delle impostazioni che la listview visualizzerà

            settingsList.add(new SettingsElement(R.drawable.ic_silverware_variant, getResources().getString(R.string.home_settings)));
            settingsList.add(new SettingsElement(R.drawable.ic_silverware_variant, getResources().getString(R.string.orders_settings)));
            settingsList.add(new SettingsElement(R.drawable.ic_silverware_variant, getResources().getString(R.string.locals_settings)));


        /*******************************************
         * Questa è la lista che rappresenta la sorgente dei dati della listview
         * ogni elemento è una mappa(chiave->valore)
         *********************************************/
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

        //costruzione dell adapter
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,//sorgente dati
                R.layout.custom_row, //layout contenente gli id di "to"
                from,
                to);

        mDrawerList.setAdapter(new SimpleAdapter(getApplicationContext(), data, R.layout.custom_row,from, to));
    }

    private void setupToolbar(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        myToolbar.setLogo(R.mipmap.ic_launcher);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
    }

    public void funzione(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
