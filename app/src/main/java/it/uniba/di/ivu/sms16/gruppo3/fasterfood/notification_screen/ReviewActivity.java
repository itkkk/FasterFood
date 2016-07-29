package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.io.File;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;

public class ReviewActivity extends AppCompatActivity {
    private Intent mReviewInt;
    private RatingBar mWaitingTimeRating, mLocalServiceRating, mFoodQualityRating;
    private Button mBtnReview;
    private TextView mTxtLocalMenu;
    private ImageView mLocalImage;
    private File mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mReviewInt = getIntent();

        // CREAZIONE UI REVIEW
        setUpReviewAct();

        // DOVREI CONTROLLARE SE L'UTENTE E' LOGGATO
        if (mBtnReview != null) {
            mBtnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkRating()) {
                        // 1) PRELEVO IL VALORE DALLA RATING STAR
                        float userRatingFQ = mFoodQualityRating.getRating();
                        float userRatingSL = mLocalServiceRating.getRating();
                        float userRatingWT = mWaitingTimeRating.getRating();

                        float userRatingTotal = (userRatingFQ + userRatingSL + userRatingWT) / 3;

                        // 2) INCREMENTO DI UN VALORE IL NUMERO DELLE RECENSIONI
                        int updateNumberReviews = mReviewInt.getIntExtra("NumberReview",0) + 1;

                        // 3) CALCOLO MEDIA = (VOTO UTENTE + (VOTO TOTALE * NUMERO RECENSIONI) / NUMERO RECENSIONI AGGIORNATO
                        float updateRatings = (userRatingTotal + (mReviewInt.getFloatExtra("RatingReview",0.0f) * mReviewInt.getIntExtra("NumberReview",0))) / updateNumberReviews;

                        // PROVA : FACCIO VEDERE IL RISULTATO
                        System.out.println("Nuova Media : " + updateRatings);

                        // 4) AGGIORNAMENTO MEDIA E NUMERO DELLE RECENSIONI NEL DB
                        DbController dbController = new DbController();
                        dbController.changeAverage(getResources().getString(R.string.db_locals),mTxtLocalMenu.getText().toString(), String.valueOf(updateRatings));

                        // 5) RINGRAZIAMENTO ALL'UTENTE, DEVO TORNARE IN HOME ACTIVITY
                        Snackbar.make(findViewById(android.R.id.content),getResources().getText(R.string.review_done), Snackbar.LENGTH_SHORT).show();
                        final ReviewActivity reviewActivity = ReviewActivity.this;
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try{
                                    sleep(2000);
                                }
                                catch(InterruptedException e){

                                }finally {
                                    reviewActivity.finishAffinity();
                                }
                            }
                        };
                        thread.start();
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content),getResources().getText(R.string.review_error), Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void setUpReviewAct() {
        // PER OTTENERE LA STATUS BAR DEL COLORE DESIDERATO
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { 
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mBtnReview = (Button) findViewById(R.id.buttonReview);
        mTxtLocalMenu = (TextView) findViewById(R.id.reviewNameLocal);
        mFoodQualityRating = (RatingBar) findViewById(R.id.ratingFoodQuality);
        mLocalServiceRating = (RatingBar) findViewById(R.id.ratingLocalService);
        mWaitingTimeRating = (RatingBar) findViewById(R.id.ratingWaitingTime);
        mLocalImage = (ImageView) findViewById(R.id.reviewMenuImage);
        mTxtLocalMenu.setText(mReviewInt.getStringExtra("LocalName"));

        // CONTROLLO CHAIN PER OTTENERE IL LOGO
        getChainImage();
    }

    // SE L'APP VIENE CHIUSA FORZATAMENTE E SI CLICCA SULLA NOTIFICA..CRASHA!
    private void getChainImage() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                    DbController dbController = new DbController();
                    if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.mcdonalds))) {
                        mLogo = dbController.getLogoFile(getResources().getString(R.string.mc_logo),
                                getResources().getString(R.string.mc_name), getApplicationContext());//ScambiaDati.getLogo(0);
                    } else if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.burgerking))) {
                        mLogo = dbController.getLogoFile(getResources().getString(R.string.burger_logo),
                                getResources().getString(R.string.burger_name), getApplicationContext());//ScambiaDati.getLogo(1);
                    } else if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.baciodilatte))) {
                        mLogo = dbController.getLogoFile(getResources().getString(R.string.bacio_logo),
                                getResources().getString(R.string.bacio_name), getApplicationContext());//ScambiaDati.getLogo(2);
                    }
               // }

                try{
                    sleep(500);
                }catch (InterruptedException e){

                }finally {
                    if(mLogo == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLocalImage.setImageResource(R.drawable.ic_food);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                final Bitmap logoBitmap = BitmapFactory.decodeFile(mLogo.getAbsolutePath());
                                mLocalImage.setImageBitmap(logoBitmap);
                            }
                        });
                    }
                }
            }
        };
        thread.start();
    }

    private boolean checkRating() {
        return (mFoodQualityRating.getRating() != 0 && mLocalServiceRating.getRating() != 0 && mWaitingTimeRating.getRating() != 0);
    }

}
