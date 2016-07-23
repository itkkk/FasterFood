package it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paypal.android.sdk.payments.*;

import org.json.JSONException;

import java.math.BigDecimal;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.SearchFragment;

public class PaymentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBuy(new BigDecimal(2)); // New BigDecimal(prezzo totale) oppure solo prezzo totale, posso gestirla a modo mio
        /*Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());
        startService(intent);*/
        System.out.println("Vado di Finish 3");
    }

    // PASSARE I PARAMETRI CORRETTI
    public void startBuy(BigDecimal price) {

        // Price dovrebbe essere il prezzo totale
        PayPalPayment menu = new PayPalPayment(price, "EUR", "Acquisto Menù", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, menu);

        startActivityForResult(intent, PayPalPay.REQUEST_CODE_PAYMENT);

        System.out.println("Vado di Finish 2");
    }

    @Override // Ho dei system sparsi qua e la
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayPalPay.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        // JSON è un formato molto utilizzato per la serializzazione di dati complessi in formato compatibili con i servizi di
                        // tipo REST
                        Log.i(PayPalPay.TAG, confirm.toJSONObject().toString(4));
                        Log.i(PayPalPay.TAG, confirm.getPayment().toJSONObject().toString(4));  // Mi fa vedere della roba che potrebbe servirmi
                        Log.i(PayPalPay.TAG, confirm.toJSONObject().getJSONObject("response").getString("state")); // Metodo per pijiare i singoli campi del documento JSON
                    // QUI AVVIENE IL PAGAMENTO - HO TUTTO IL MATERIALE A DISPOSIZIONE!
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();
                        System.out.println(confirm.getPayment().toString());
                        System.out.println(confirm.getProofOfPayment().getState());

                    } catch (JSONException e) {
                        Log.e(PayPalPay.TAG, "an extremely unlikely failure occurred: ", e); // Avviene se ci sono errori sul documento JSON
                        System.out.println("Sono qui! 1");
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Sono qui! 2");
                finish(); // Quà semplicemente torno indietro e torno in summary
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(PayPalPay.TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                System.out.println("Sono qui! 3");
            }
            System.out.println("Vado di servizio 2");
            stopService(new Intent(this, PayPalService.class));
            System.out.println("Vado di Finish");
            finish();
        }
    }

    @Override
    public void onDestroy() {
        // Termina il servizio di PayPal
        /*System.out.println("Chiudo il servizio 2");
        stopService(new Intent(this, PayPalService.class));*/ // Stop Service va inserito qui per forza!
        //Snackbar.make(this.findViewById(R.id.fragment), "Hai pagato", Snackbar.LENGTH_LONG).show();
                        /*getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).addToBackStack(null).commit();
                        ((HomeActivity)getActivity()).changeDrawerIcon();*/
        // Qui credo che dovrei farlo tornare in HOME ACTIVITY
        super.onDestroy();
    }
}