package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.CityList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;

public class SplashActivity extends AppCompatActivity {
    private LocalsList localsList;
    private ChainList chainList;
    private CityList cityList;
    private FirebaseAuth mAuth;
    private File logoMcDonalds;
    private File logoBurgerKing;
    private File logoBacioDiLatte;
    private File allChain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        Thread splash_screen = new Thread(){
            public void run(){
                FirebaseUser user = mAuth.getCurrentUser();
                DbController connectionDB = new DbController();
                if(user != null){
                    AppConfiguration.setLogged(true);
                    AppConfiguration.setUser(user.getEmail());
                }

                else AppConfiguration.setLogged(false);
                localsList = connectionDB.queryLocals(getResources().getText(R.string.db_locals).toString());
                chainList = connectionDB.queryChains(getResources().getText(R.string.db_chains).toString());
                cityList = connectionDB.queryCities(getResources().getString(R.string.db_cities).toString());

                logoMcDonalds = connectionDB.getLogoFile(getResources().getString(R.string.mc_logo),
                        getResources().getString(R.string.mc_name),getApplicationContext());
                logoBurgerKing =  connectionDB.getLogoFile(getResources().getString(R.string.burger_logo),
                        getResources().getString(R.string.burger_name),getApplicationContext());
                allChain = connectionDB.getLogoFile(getResources().getString(R.string.all_chain_logo),
                        getResources().getString(R.string.allchain_name), getApplicationContext());
                logoBacioDiLatte = connectionDB.getLogoFile(getResources().getString(R.string.bacio_logo),
                        getResources().getString(R.string.bacio_name),getApplicationContext());

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                finally{
                    ScambiaDati.setLocalsList(localsList);
                    ScambiaDati.setChainList(chainList);
                    ScambiaDati.setCityList(cityList);

                    if (logoMcDonalds != null && logoBurgerKing != null && logoBacioDiLatte != null){
                        ScambiaDati.setFile(logoMcDonalds,0);
                        ScambiaDati.setFile(logoBurgerKing,1);
                        ScambiaDati.setFile(logoBacioDiLatte,2);
                        ScambiaDati.setFile(allChain, 3);
                        AppConfiguration.setLogoDownloaded(true);
                    }
                    else AppConfiguration.setLogoDownloaded(false);
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }
            }
        };

        splash_screen.start();
    }
}
