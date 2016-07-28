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
        startBuy(); // New BigDecimal(prezzo totale) oppure solo prezzo totale, posso gestirla a modo mio
    }

    // PASSARE I PARAMETRI CORRETTI
    public void startBuy() {

        Intent i = getIntent();
        float p = i.getFloatExtra("totale", 0);
        BigDecimal price = new BigDecimal(Float.toString(p));

        // Price dovrebbe essere il prezzo totale
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
                /*PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        // JSON è un formato molto utilizzato per la serializzazione di dati complessi in formato compatibili con i servizi di
                        // tipo REST
                        Log.i(PayPalPay.TAG, confirm.toJSONObject().toString(4));
                        Log.i(PayPalPay.TAG, confirm.getPayment().toJSONObject().toString(4));  // Mi fa vedere della roba che potrebbe servirmi
                        Log.i(PayPalPay.TAG, confirm.toJSONObject().getJSONObject("response").getString("state")); // Metodo per pijiare i singoli campi del documento JSON
                    // QUI AVVIENE IL PAGAMENTO - HO TUTTO IL MATERIALE A DISPOSIZIONE!
                        *//*Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();*//*
                        System.out.println(confirm.getPayment().toString());
                        System.out.println(confirm.getProofOfPayment().getState());

                    } catch (JSONException e) {
                        Log.e(PayPalPay.TAG, "an extremely unlikely failure occurred: ", e); // Avviene se ci sono errori sul documento JSON
                    }
                }*/
            } else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                stopService(new Intent(this, PayPalService.class));
                finish(); // Quà semplicemente torno indietro e torno in summary
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                //Log.i(PayPalPay.TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                setResult(PaymentActivity.RESULT_EXTRAS_INVALID);
                stopService(new Intent(this, PayPalService.class));
                finish();
            }
        }
    }

}