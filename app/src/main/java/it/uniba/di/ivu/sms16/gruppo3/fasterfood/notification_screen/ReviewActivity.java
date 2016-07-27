package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        final Intent reviewInt = getIntent();

        Button btnReview = (Button) findViewById(R.id.buttonReview);
        TextView txtLocalMenu = (TextView) findViewById(R.id.reviewNameLocal);
        final RatingBar currentRating = (RatingBar) findViewById(R.id.ratingReview);

        txtLocalMenu.setText(reviewInt.getStringExtra("LocalName"));
        if (btnReview != null) {
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentRating.getRating() != 0) {
                        // 1) PRELEVO IL VALORE DALLA RATING STAR
                        float userRating = currentRating.getRating();

                        // 2) INCREMENTO DI UN VALORE IL NUMERO DELLE RECENSIONI
                        int updateNumberReviews = reviewInt.getIntExtra("NumberReview",0) + 1;

                        // 3) CALCOLO MEDIA -- N.B. : COME POSSO OTTENERE IL CALCOLO ESATTO SE IL NUMERO DELLE RECENSIONI
                        //                            E' MAGGIORE DI 2?
                        float updateRatings = (userRating + reviewInt.getFloatExtra("RatingReview",0.0f)) / updateNumberReviews;

                        // PROVA : FACCIO VEDERE IL RISULTATO NELLA RATING BAR CORRENTE (ANCHE SE NON SARA' COSI')
                        currentRating.setRating(updateRatings);

                        // 4) AGGIORNAMENTO MEDIA E NUMERO DELLE RECENSIONI NEL DB
                    }
                    else {
                        // PROVA
                        Toast.makeText(getApplicationContext(),"Non hai votato una sega", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
