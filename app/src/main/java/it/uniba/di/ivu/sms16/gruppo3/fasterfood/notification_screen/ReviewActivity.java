package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;

public class ReviewActivity extends AppCompatActivity {
    private Intent mReviewInt;
    private RatingBar mCurrentRating;
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

        if (mBtnReview != null) {
            mBtnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCurrentRating.getRating() != 0) {
                        // 1) PRELEVO IL VALORE DALLA RATING STAR
                        float userRating = mCurrentRating.getRating();

                        // 2) INCREMENTO DI UN VALORE IL NUMERO DELLE RECENSIONI
                        int updateNumberReviews = mReviewInt.getIntExtra("NumberReview",0) + 1;

                        // 3) CALCOLO MEDIA = (VOTO UTENTE + (VOTO TOTALE * NUMERO RECENSIONI) / NUMERO RECENSIONI AGGIORNATO
                        float updateRatings = (userRating + (mReviewInt.getFloatExtra("RatingReview",0.0f) * mReviewInt.getIntExtra("NumberReview",0))) / updateNumberReviews;

                        // PROVA : FACCIO VEDERE IL RISULTATO NELLA RATING BAR CORRENTE (ANCHE SE NON SARA' COSI')
                        mCurrentRating.setRating(updateRatings);

                        // 4) AGGIORNAMENTO MEDIA E NUMERO DELLE RECENSIONI NEL DB
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content),"Non hai votato una sega", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setUpReviewAct() {
        mBtnReview = (Button) findViewById(R.id.buttonReview);
        mTxtLocalMenu = (TextView) findViewById(R.id.reviewNameLocal);
        mCurrentRating = (RatingBar) findViewById(R.id.ratingReview);
        mLocalImage = (ImageView) findViewById(R.id.reviewMenuImage);
        mTxtLocalMenu.setText(mReviewInt.getStringExtra("LocalName"));

        // CONTROLLO CHAIN PER OTTENERE IL LOGO
        getChainImage();
    }

    private void getChainImage() {
        if(!AppConfiguration.isLogoDownloaded()){
            mLocalImage.setImageResource(R.drawable.ic_food);
        }
        else {
            if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.mcdonalds))) {
                mLogo = ScambiaDati.getLogo(0);
            } else if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.burgerking))) {
                mLogo = ScambiaDati.getLogo(1);
            } else if (mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.baciodilatte))) {
                mLogo = ScambiaDati.getLogo(2);
            }
            Bitmap logoBitmap = BitmapFactory.decodeFile(mLogo.getAbsolutePath());
            mLocalImage.setImageBitmap(logoBitmap);
        }
    }
}
