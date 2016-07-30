package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Order;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen.AlarmNotification;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NFCDialog extends DialogFragment
        implements NfcAdapter.CreateNdefMessageCallback {

    NfcAdapter mNfcAdapter;
    GifImageView gif;
    TextView payMessage;
    private static final int MESSAGE_SENT = 1;
    private SharedPreferences prefs;
    private boolean notification_bool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_nfc, null);
        payMessage = (TextView) view.findViewById(R.id.payMessage);
        gif = (GifImageView) view.findViewById(R.id.gif);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            gif.setImageDrawable(new GifDrawable(getResources(), R.drawable.gif));
        } catch (IOException e) {
            e.printStackTrace();
        }

        prefs = this.getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_name)
                , Context.MODE_PRIVATE);
        notification_bool = prefs.getBoolean(getActivity().getResources().getString(R.string.shared_pref_notification), true);

        payMessage.append("€"); // TODO $
        payMessage.append(getArguments().getString("price"));

        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        //This will refer back to createNdefMessage for what it will send
        mNfcAdapter.setNdefPushMessageCallback(this, getActivity());
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String message = FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + getArguments().getString("date");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        mNfcAdapter.setNdefPushMessage(ndefMessage, getActivity());

        mNfcAdapter.setOnNdefPushCompleteCallback(new NfcAdapter.OnNdefPushCompleteCallback() {
            @Override
            public void onNdefPushComplete(NfcEvent event) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (notification_bool) {
                            Bundle reviewBundle = new Bundle();
                            reviewBundle.putString("NameLocal", getArguments().getString("NameLocal"));
                            reviewBundle.putFloat("RatingLocal", getArguments().getFloat("RatingLocal"));
                            reviewBundle.putInt("NumberRating", getArguments().getInt("NumberRating"));
                            AlarmNotification alarmNotification = new AlarmNotification();
                            alarmNotification.setAlarm(getActivity(), reviewBundle);

                        }
                        //aggiorno gli ordini
                        //OrderList orderList = ScambiaDati.getOrderList();
                        for(Order order : ScambiaDati.getOrderList().getOrders()){
                            if(order.getData().equals(getArguments().getString("date"))){
                                order.setStato("chiuso");
                            }
                        }
                        Fragment fragment = new OrdersFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.setCustomAnimations(R.animator.slide_in_left,android.R.animator.fade_out);
                        transaction.replace(R.id.fragment, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        }, getActivity());

        return ndefMessage;
    }
}