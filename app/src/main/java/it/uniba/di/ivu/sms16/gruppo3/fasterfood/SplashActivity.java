package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class SplashActivity extends AppCompatActivity {
    private LocalsList localsList;
    private ScambiaDati scambiaDati = ScambiaDati.getScambiaDati();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Thread splash_screen = new Thread(){
            public void run(){
                ConnectionDB connectionDB = new ConnectionDB();
                localsList = connectionDB.queryLocal();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {}
                finally{
                    scambiaDati.setLocalsList(localsList);
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }
            }
        };
        splash_screen.start();

    }
}
