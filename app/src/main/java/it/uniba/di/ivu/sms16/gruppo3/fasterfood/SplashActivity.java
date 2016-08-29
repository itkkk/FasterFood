package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.File;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.CityList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;

public class SplashActivity extends AppCompatActivity {
    private LocalsList localsList;
    private ChainList chainList;
    private CityList cityList;
    private FirebaseAuth mAuth;
    private File logoMcDonalds;
    private File logoBurgerKing;
    private File logoBacioDiLatte;
    private File allChain;

    //Utili per animazione
    FrameLayout rvl1;
    FrameLayout rvl2;
    TextView t1;
    TextView t2;
    ImageView img_logo;
    ImageView img_final;

    Animation anim;
    Animation anim2;
    Animation anim3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        setupAnim();

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
                    sleep(3500);
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

        rvl1.post(new Runnable()
        {
            @Override
            public void run()
            {
                start_circ();
                start_anim();
            }
        });

        splash_screen.start();
    }

    private void setupAnim(){
        rvl1=(FrameLayout) findViewById(R.id.primasplash);
        rvl2=(FrameLayout) findViewById(R.id.secodasplash);
        t1=(TextView) findViewById(R.id.primat);
        t2=(TextView) findViewById(R.id.secondat);
        img_logo=(ImageView) findViewById(R.id.imageView);
        img_final=(ImageView) findViewById(R.id.imageView2);

        anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animazione_testo1);
        anim2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animazione_testo2);
        anim3=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animazione_logo);
    }

    private void start_circ(){
        if(Build.VERSION.SDK_INT>=21)
        {
            // previously invisible view
            //View myView = findViewById(R.id.my_view);

            // get the center for the clipping circle
            int cx = rvl1.getWidth() ;
            int cy = rvl1.getHeight() / 2  ;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(rvl1, cx, cy, 0, finalRadius);
            anim.setDuration(500);
            // make the view visible and start the animation
            rvl1.setVisibility(View.VISIBLE);
            anim.start();
        }else{
            //Fai un fade
            rvl1.setVisibility(View.VISIBLE);
        }
    }

    private void start_anim(){
        t1.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim){}
            public void onAnimationRepeat(Animation anim){}
            public void onAnimationEnd(Animation anim)
            {
                t2.setVisibility(View.VISIBLE);
                start_text2();
                img_logo.setVisibility(View.VISIBLE);
                start_logo();
            }
        });
    }

    private void start_text2(){
        t2.startAnimation(anim2);

        anim2.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim){}
            public void onAnimationRepeat(Animation anim){}
            public void onAnimationEnd(Animation anim){}
        });
    }

    private void start_logo(){
        img_logo.startAnimation(anim3);

        anim3.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim) {}
            public void onAnimationRepeat(Animation anim) {}
            public void onAnimationEnd(Animation anim)
            {
                start_circ2();
            }
        });
    }

    private void start_circ2(){
        if(Build.VERSION.SDK_INT>=21)
        {
            // get the center for the clipping circle
            int cx = rvl2.getWidth() ;
            int cy = rvl2.getHeight() ;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(rvl2, cx, cy, 0, finalRadius);
            anim.setDuration(500);
            // make the view visible and start the animation
            rvl2.setVisibility(View.VISIBLE);
            anim.start();
        }else{
            //Fai un fade
            rvl2.setVisibility(View.VISIBLE);
        }
    }
}
