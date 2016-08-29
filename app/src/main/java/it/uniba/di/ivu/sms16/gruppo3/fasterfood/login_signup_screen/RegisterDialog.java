package it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class RegisterDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.signed_up))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).commit();
                    }
                })
                .create();
    }
}
