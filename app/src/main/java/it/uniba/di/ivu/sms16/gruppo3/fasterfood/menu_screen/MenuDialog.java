package it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.DbController;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;

public class MenuDialog extends DialogFragment {
    String immUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_dialog, null);

        TextView titolo = (TextView) view.findViewById(R.id.dialogTitle);
        TextView descrizione = (TextView) view.findViewById(R.id.menu_description);
        final ImageView immagine = (ImageView) view.findViewById(R.id.imageMenu);

        final Bundle bundle = getArguments();
        final String nome = bundle.getString("nome");
        final String dettagli = bundle.getString("descrizione");
        final String imm = bundle.getString("immagine");
        final String categoria = bundle.getString("categoria");


        immagine.setVisibility(View.INVISIBLE);
        titolo.setText(nome);
        descrizione.setText(dettagli);

        if(categoria.equals(getResources().getString(R.string.mcdonalds))){
            immUrl = getResources().getString(R.string.menu_address) + getResources().getString(R.string.mc_name)
                    + "/" + imm + ".png";
        }
        else if(categoria.equals(getResources().getString(R.string.burgerking))){
            immUrl = getResources().getString(R.string.menu_address) + getResources().getString(R.string.burger_name)
                    + "/" + imm + ".png";
        }
        else if(categoria.equals(getResources().getString(R.string.baciodilatte))){
            immUrl = getResources().getString(R.string.menu_address) + getResources().getString(R.string.bacio_name)
                    + "/" + imm + ".png";
        }


        Thread dialogThread = new Thread(){
            @Override
            public void run() {
                super.run();
                DbController dbController = new DbController();
                File file = dbController.getLogoFile(immUrl, nome, getActivity().getApplicationContext());
                try{
                    sleep(1000);
                }catch(InterruptedException e){}
                finally{
                    final Bitmap logoBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            immagine.setVisibility(View.VISIBLE);
                            immagine.setImageBitmap(logoBitmap);
                        }
                    });
                }

            }
        };
        dialogThread.start();
        Toast.makeText(getActivity().getApplicationContext(),immUrl,Toast.LENGTH_LONG).show();



        return view;
    }

    public void onResume() {/*
       /* int width = getResources().getDisplayMetrics().widthPixels;
        int height = 800;
        getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();/*
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout(size.x, size.x);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing*/
        super.onResume();
    }
}
