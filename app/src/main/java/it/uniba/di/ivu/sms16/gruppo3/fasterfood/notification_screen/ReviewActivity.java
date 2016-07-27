package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;

public class ReviewActivity extends AppCompatActivity {
    private RatingBar mCurrentRating;
    private Button mBtnReview;
    private TextView mTxtLocalMenu;
    private ImageView mLocalImage;
    private File mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final Intent reviewInt = getIntent();

        mBtnReview = (Button) findViewById(R.id.buttonReview);
        mTxtLocalMenu = (TextView) findViewById(R.id.reviewNameLocal);
        mCurrentRating = (RatingBar) findViewById(R.id.ratingReview);
        mLocalImage = (ImageView) findViewById(R.id.reviewMenuImage);


        mTxtLocalMenu.setText(reviewInt.getStringExtra("LocalName"));

        // CONTROLLO CHAIN PER OTTENERE IL LOGO
        if(mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.mcdonalds))){
            mLogo = ScambiaDati.getLogo(0);
        }
        else if(mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.burgerking))){
            mLogo = ScambiaDati.getLogo(1);
        }
        else if(mTxtLocalMenu.getText().toString().contains(getResources().getString(R.string.baciodilatte))){
            mLogo = ScambiaDati.getLogo(2);
        }
        Bitmap logoBitmap = BitmapFactory.decodeFile(mLogo.getAbsolutePath());
        mLocalImage.setImageBitmap(logoBitmap);

        if (mBtnReview != null) {
            mBtnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCurrentRating.getRating() != 0) {
                        // 1) PRELEVO IL VALORE DALLA RATING STAR
                        float userRating = mCurrentRating.getRating();

                        // 2) INCREMENTO DI UN VALORE IL NUMERO DELLE RECENSIONI
                        int updateNumberReviews = reviewInt.getIntExtra("NumberReview",0) + 1;

                        // 3) CALCOLO MEDIA = (VOTO UTENTE + (VOTO TOTALE * NUMERO RECENSIONI) / NUMERO RECENSIONI AGGIORNATO
                        float updateRatings = (userRating + (reviewInt.getFloatExtra("RatingReview",0.0f) * reviewInt.getIntExtra("NumberReview",0))) / updateNumberReviews;

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
}
