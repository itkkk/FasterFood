package it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.payment_screen.PaymentsActivity;


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

                        /*Snackbar.make(getActivity().findViewById(R.id.fragment), "Hai pagato", Snackbar.LENGTH_LONG).show();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).addToBackStack(null).commit();
                        ((HomeActivity)getActivity()).changeDrawerIcon();*/
                        Intent prova = new Intent(getActivity(), PaymentsActivity.class);
                        getActivity().startActivity(prova);
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
