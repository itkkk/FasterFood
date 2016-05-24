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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.drawer_item_1)));

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
