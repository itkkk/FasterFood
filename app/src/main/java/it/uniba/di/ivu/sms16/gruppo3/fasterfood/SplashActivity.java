package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;

public class SplashActivity extends AppCompatActivity {
    private LocalsList localsList;
    private ChainList chainList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        Thread splash_screen = new Thread(){
            public void run(){
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    AppConfiguration.setLogged(true);
                    AppConfiguration.setUser(user.getEmail());
                }
                else AppConfiguration.setLogged(false);
                DbController connectionDB = new DbController();
                localsList = connectionDB.queryLocals(getResources().getText(R.string.db_locals).toString());
                chainList = connectionDB.queryChains(getResources().getText(R.string.db_chains).toString());
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                finally{
                    ScambiaDati.setLocalsList(localsList);
                    ScambiaDati.setChainList(chainList);
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }
            }
        };
        splash_screen.start();

    }
}
