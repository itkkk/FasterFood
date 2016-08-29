package it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.paypal.android.sdk.payments.*;
import java.math.BigDecimal;

public class PaymentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBuy();
    }

    // PASSARE I PARAMETRI CORRETTI
    public void startBuy() {

        Intent i = getIntent();
        float p = i.getFloatExtra("totale", 0);
        BigDecimal price = new BigDecimal(Float.toString(p));

        // Price è il prezzo totale
        PayPalPayment menu = new PayPalPayment(price, "EUR", "Acquisto Menù", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, menu);

        startActivityForResult(intent, PayPalPay.REQUEST_CODE_PAYMENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayPalPay.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK);
                stopService(new Intent(this, PayPalService.class));
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                stopService(new Intent(this, PayPalService.class));
                finish(); // Quà semplicemente torno indietro e torno in summary
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                setResult(PaymentActivity.RESULT_EXTRAS_INVALID);
                stopService(new Intent(this, PayPalService.class));
                finish();
            }
        }
    }
}