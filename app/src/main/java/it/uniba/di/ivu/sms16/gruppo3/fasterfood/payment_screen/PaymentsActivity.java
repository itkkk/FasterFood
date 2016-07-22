package it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paypal.android.sdk.payments.*;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBuyPressed(new BigDecimal(2));
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());
        startService(intent);
    }

    // PASSARE I PARAMETRI CORRETTI
    public void onBuyPressed(BigDecimal price) {

        // Bisogna far passare una lista di menù
        PayPalPayment menù = new PayPalPayment(price, "EUR", "Noleggio Bicicletta", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalPay.getConfig());

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, menù);

        startActivityForResult(intent, PayPalPay.REQUEST_CODE_PAYMENT);

    }

    @Override // Ho dei system sparsi qua e la
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayPalPay.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(PayPalPay.TAG, confirm.toJSONObject().toString(4));
                        Log.i(PayPalPay.TAG, confirm.getPayment().toJSONObject().toString(4));  // Mi fa vedere della roba che non mi serve
                    // QUI AVVIENE IL PAGAMENTO
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e(PayPalPay.TAG, "an extremely unlikely failure occurred: ", e); // IL CODICE QUI ARRIVA, MA L'INTENT DI PAYPAL VA PER I CAZZI SUOI
                        System.out.println("Sono qui! 1");
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Sono qui! 2");
                finish(); // Quà semplicemente torno indietro
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(PayPalPay.TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                System.out.println("Sono qui! 3");
            }
        }
    }

    @Override
    public void onDestroy() {
        // Termina il servizio di PayPal
        System.out.println("Chiudo il servizio 2");
        stopService(new Intent(this, PayPalService.class)); // Stop Service va inserito qui per forza!
        /*System.out.println("Vado di Finish");*/
        //finish();
        /*Snackbar.make(getActivity().findViewById(R.id.fragment), "Hai pagato", Snackbar.LENGTH_LONG).show();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).addToBackStack(null).commit();
                        ((HomeActivity)getActivity()).changeDrawerIcon();*/
        // Qui credo che dovrei farlo tornare in HOME ACTIVITY
        super.onDestroy();
    }
}