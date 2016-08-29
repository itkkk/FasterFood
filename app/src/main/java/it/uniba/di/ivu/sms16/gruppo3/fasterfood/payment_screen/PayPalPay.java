package it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen;

//import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import java.util.Locale;

public class PayPalPay {
    public static final String TAG = "paymentExample"; // TAG inutile, va cancellato
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // ID_Client ottenibile dall'App creata per recevere le REST API credentials per testare le transazioni
    private static final String CONFIG_CLIENT_ID = "Af0yo6I6MqOFmhrm7JZKodzu22xn8ZUDU7cWmOru2ceoNLjfcrL2UU0l0uK7AaY-CzF-pj9NwZaTXA9w";

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

        // Controllo se la versione dell'app è ITA o ENG
        if(Locale.getDefault().getDisplayLanguage().equals("en")) {
            config.languageOrLocale("en");
        }
        else {
            config.languageOrLocale("it");
        }
        return config;
    }
}

