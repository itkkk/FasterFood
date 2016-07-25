package it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen;

//import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;

public class PayPalPay {
    public static final String TAG = "paymentExample"; // TAG inutile, va cancellato

    /*  PayPalConfiguration.ENVIRONMENT_SANDBOX per l'ambiente di prova per sviluppatori
        PayPalConfiguration.ENVIRONMENT_PRODUCTION per usare l'ambiente reale (con soldi veri)
        PayPalConfiguration.ENVIRONMENT_NO_NETWORK per l'ambiente di prova senza comunicazione col server
     */
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // ID_Client ottenibile dall'App creata per recevere le REST API credentials per testare le transazioni
    private static final String CONFIG_CLIENT_ID = "AWUFmyJtNF0LNcj5SB1uafhXEFpNzyfO9SkL_MymtPUFuCKTJFDFb_sxa_JEa1eoTWHx2cRJzVfMBUd5";

    public static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);


    // Configuro le schermate di PayPal
    public static PayPalConfiguration getConfig() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getEmail();
        config.acceptCreditCards(false);
        config.defaultUserEmail(user);
        // Per il linguaggio andrebbe fatto il controllo se la versione dell'app è ITA o ENG
        config.languageOrLocale("en");
        return config;
    }
}

