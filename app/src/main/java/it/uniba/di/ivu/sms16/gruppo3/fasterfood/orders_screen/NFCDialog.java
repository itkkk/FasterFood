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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.Charset;
import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;

public class NFCDialog extends DialogFragment
        implements NfcAdapter.CreateNdefMessageCallback{
    VideoView gif;
    //The array lists to hold our messages
    private ArrayList<String> messagesToSendArray = new ArrayList<>();
    private ArrayList<String> messagesReceivedArray = new ArrayList<>();
    NfcAdapter mNfcAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_nfc, null);
        // add here
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (!mNfcAdapter.isEnabled())
            Toast.makeText(getActivity(), "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();

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