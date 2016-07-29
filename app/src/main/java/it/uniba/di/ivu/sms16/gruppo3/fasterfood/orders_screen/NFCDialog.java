package it.uniba.di.ivu.sms16.gruppo3.fasterfood.orders_screen;

import android.app.DialogFragment;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
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
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NFCDialog extends DialogFragment
        implements NfcAdapter.CreateNdefMessageCallback{

    NfcAdapter mNfcAdapter;
    GifImageView gif;
    TextView payMessage;

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
        return ndefMessage;
    }
}