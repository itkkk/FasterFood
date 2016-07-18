package it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen.SearchFragment;

/**
 * Created by Angelo on 31/05/2016.
 */
public class PayDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Message
                .setMessage(getString(R.string.dialog_question))
                // Positive button
                .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        //Toast.makeText(getActivity(), "Hai pagato", Toast.LENGTH_SHORT).show();

                        Snackbar.make(getActivity().findViewById(R.id.fragment), "Hai pagato", Snackbar.LENGTH_LONG).show();
                        getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).commit();

                    }
                })
                // Negative Button
                .setNegativeButton(getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                })
                .create();
    }
}
