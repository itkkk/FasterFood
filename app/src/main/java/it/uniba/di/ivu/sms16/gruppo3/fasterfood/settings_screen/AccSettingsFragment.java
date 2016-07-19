package it.uniba.di.ivu.sms16.gruppo3.fasterfood.settings_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

//Creazione Branch

//Implementa preferenze per città e chain da usare nelle interfacce come la home
//oltre al cambio password e all'attivazione notifiche (es. notifica review post consumazione)
//i salvataggi sono fatti solo tramite bottone SAVE //da implementare col db l'onclick
public class AccSettingsFragment extends android.app.Fragment {

    //Ogni oggetto setting gestisce i layout per animazione, sono usate tali classi per ridurre codice e spostare la gestione
    //dello slide/anim in altra classe
    AnimAccSettingsManagement first_setting;
    AnimAccSettingsManagement second_setting;
    AnimAccSettingsManagement third_setting;
    AnimAccSettingsManagement fourth_setting;

    Switch notification;
    Button change_psw;
    EditText psw_to_change;

    View layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_acc_settings, container, false);

        //ogni setting viene inizializzato settando i layout visibile e non, l'icon di unfold, e i listener sui layout
        first_setting=new AnimAccSettingsManagement();
        first_setting.setmLinearLayout((LinearLayout) layout.findViewById(R.id.expandable));
        first_setting.setSetIcon((ImageView) layout.findViewById(R.id.imgSettingsUnfold));
        first_setting.setmLinearLayoutHeader((LinearLayout) layout.findViewById(R.id.header));
        first_setting.setListeners();

        second_setting=new AnimAccSettingsManagement();
        second_setting.setmLinearLayout((LinearLayout) layout.findViewById(R.id.expandable2));
        second_setting.setSetIcon((ImageView) layout.findViewById(R.id.imgSettingsUnfold2));
        second_setting.setmLinearLayoutHeader((LinearLayout) layout.findViewById(R.id.header2));
        second_setting.setListeners();

        third_setting=new AnimAccSettingsManagement();
        third_setting.setmLinearLayout((LinearLayout) layout.findViewById(R.id.expandable3));
        third_setting.setSetIcon((ImageView) layout.findViewById(R.id.imgSettingsUnfold3));
        third_setting.setmLinearLayoutHeader((LinearLayout) layout.findViewById(R.id.header3));
        third_setting.setListeners();

        fourth_setting=new AnimAccSettingsManagement();
        fourth_setting.setmLinearLayout((LinearLayout) layout.findViewById(R.id.expandable4));
        fourth_setting.setSetIcon((ImageView) layout.findViewById(R.id.imgSettingsUnfold4));
        fourth_setting.setmLinearLayoutHeader((LinearLayout) layout.findViewById(R.id.header4));
        fourth_setting.setListeners();

        notification = (Switch) layout.findViewById(R.id.switch_notification);

        change_psw = (Button) layout.findViewById(R.id.psw_btn);
        psw_to_change = (EditText) layout.findViewById(R.id.password_old);

        //inizializzato come in schermata home e locals
        Spinner spinner = (Spinner) layout.findViewById(R.id.favSpinnerChain);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chains, R.layout.spinner_element);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //gestione listener dello switch di notifiche
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    notification.setText(layout.getResources().getString(R.string.notification_on));
                }else{
                    notification.setText(layout.getResources().getString(R.string.notification_off));
                }
            }

        });

        //gestione listener bottone in cambio password
        //se il bottone è settato su change allora si controlla la password //da implementare con db
        //se la password è verificata viene richiesta nuova password, il bottone è settato in save
        //quindi viene confermata
        change_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(psw_control() && change_psw.getText().toString().equals("Change"))
                    change_psw_btn();
                else
                    confirm_psw_btn();
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //per evitare sovvrascrizioni in fase di animazione
        psw_to_change.setHint(layout.getResources().getString(R.string.pass_hint));
    }

    //da implementare per controllo vecchia password (con db)
    private boolean psw_control(){
        return true;
    }

    //setta il bottone per salvare nuova password, cambia anche l'edit text per acquisizione password
    private void change_psw_btn(){
        change_psw.setText(layout.getResources().getString(R.string.pass_btn_save));
        Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_confirm), Toast.LENGTH_SHORT).show();
        psw_to_change.setText("");
        psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
    }

    //setta il bottone per confermare nuova password.
    //se la password nuova è valida allora viene salvata e il bottone è settato come inizio in change
    //altrimenti il bottone non è modificato e la password non è salvata
    private void confirm_psw_btn(){
        if(passValid(psw_to_change.getText().toString())){
            change_psw.setText(layout.getResources().getString(R.string.pass_btn_change));
            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_changed), Toast.LENGTH_SHORT).show();
            psw_to_change.setText("");
            psw_to_change.setHint(layout.getResources().getString(R.string.pass_hint));
        }else{
            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_wrong), Toast.LENGTH_SHORT).show();
            psw_to_change.setText("");
            psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
        }
    }

    //controllo base della password da modificare
    private boolean passValid(String pass){
        return pass.length() > 4;
    }
}
