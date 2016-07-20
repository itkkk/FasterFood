package it.uniba.di.ivu.sms16.gruppo3.fasterfood.settings_screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.CityList;

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

    private Switch notification;
    private Button change_psw;
    private EditText psw_to_change;
    private Spinner spinner;
    private ChainList chainList;
    private CityList cityList;
    private AutoCompleteTextView favCitytxt;
    private Button save_set;

    private int NUM_AUTOCOMPLETETXT=1;
    private boolean AUTOCOMPLETETXT_FOCUS=false;
    boolean validate=true;

    private FirebaseAuth mAuth;

    SharedPreferences prefs;

    View layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_acc_settings, container, false);

        prefs=this.getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_pref_name)
                ,Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        save_set = (Button) layout.findViewById(R.id.save_settings_btn);

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

        //gestione spinner
        spinner = (Spinner) layout.findViewById(R.id.favSpinnerChain);
        //dati dal db
        chainList = ScambiaDati.getChainList();

        //creazione array per spinner
        String[] spinnerArray = new String[(chainList.getChains().size())+1];
        spinnerArray[0] = getActivity().getResources().getString(R.string.all_chains);
        for(int i=0; i<chainList.getChains().size(); i++){
            spinnerArray[i+1] = chainList.getChains().get(i).getNome();
        }
        //adapter spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_element, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(adapter.getPosition(prefs.getString(getActivity().getResources().getString(R.string.shared_pref_chains),
                getActivity().getResources().getString(R.string.all_chains))));

        //TODO : ricerca solo l'inizale del paese
        //gestione ricerca city favorite
        favCitytxt = (AutoCompleteTextView) layout.findViewById(R.id.favCitySearch);
        cityList = ScambiaDati.getCityList();
        //set adapter
        String[] autocompletetxtArray = new String[(cityList.getCities().size())];
        for(int i=0; i<cityList.getCities().size(); i++){
            autocompletetxtArray[i] = cityList.getCities().get(i).getNome();
        }
        ArrayAdapter<String> txt_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, autocompletetxtArray);
        favCitytxt.setAdapter(txt_adapter);
        favCitytxt.setThreshold(NUM_AUTOCOMPLETETXT);

        //gestione touch su edit text
        favCitytxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    favCitytxt.setText("");
                    AUTOCOMPLETETXT_FOCUS=false;
                }
            }
        });

        favCitytxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AUTOCOMPLETETXT_FOCUS=true;
            }
        });

        favCitytxt.setText(prefs.getString(getActivity().getResources().getString(R.string.shared_pref_cities)
                ,null));

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

        notification.setChecked(prefs.getBoolean(getActivity().getResources().getString(R.string.shared_pref_notification)
                ,true));

        //gestione listener bottone in cambio password
        //se il bottone è settato su change allora si controlla la password //da implementare con db
        //se la password è verificata viene richiesta nuova password, il bottone è settato in save
        //quindi viene confermata
        change_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change_psw.getText().toString().equals("Change")) {
                   psw_control();//controlla la password re autenticando l'user e modifica botton change in save
                } else if (change_psw.getText().toString().equals("Save")) {
                        confirm_psw_btn();
                    }
            }
        });

        save_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getActivity().getResources().getString(R.string.shared_pref_chains)
                        ,spinner.getSelectedItem().toString());
                if(valid_city()){
                    editor.putString(getActivity().getResources().getString(R.string.shared_pref_cities)
                            ,favCitytxt.getText().toString());
                }
                editor.putBoolean(getActivity().getResources().getString(R.string.shared_pref_notification)
                        ,notification.isChecked());
                editor.apply();
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

    private boolean valid_city(){
        boolean valid=false;
        String a=favCitytxt.getText().toString();
        for(int i=0;i<cityList.getCities().size();i++){
            if(a.equals(cityList.getCities().get(i).getNome()) || a.equals(""))
            {
                valid=true;
            }
        }
        return valid;
    }

    //controllo password con re-autenticazione. l'email viene presa da current user profile
    //guida firebase
    //da modificare il boolean in caso
    private void psw_control(){
        FirebaseUser user = mAuth.getCurrentUser();
        String email="";
        String password=psw_to_change.getText().toString();
        validate=true;

        if (user != null) {
            email = user.getEmail();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "password vuota", Toast.LENGTH_SHORT).show();
                validate = false;
            }
        }else{
            //user non loggato ERRORE
            Toast.makeText(getActivity(), "User non loggato", Toast.LENGTH_SHORT).show();
            validate=false;
        }
        if(validate) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, password);

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                //ERRORE PASSWORD
                                Toast.makeText(getActivity(), "password errata", Toast.LENGTH_SHORT).show();
                                validate=false;
                            }else{
                                change_psw_btn();
                            }
                        }
                    });
        }
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
        String new_password=psw_to_change.getText().toString();
        if(passValid(new_password)){
            set_new_pass(new_password);
        }else{
            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_wrong), Toast.LENGTH_SHORT).show();
            psw_to_change.setText("");
            psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
        }
    }

    private void set_new_pass(String new_password){
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(new_password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //ANDATA BENE
                            change_psw.setText(layout.getResources().getString(R.string.pass_btn_change));
                            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_changed),
                                    Toast.LENGTH_SHORT).show();
                            psw_to_change.setText("");
                            psw_to_change.setHint(layout.getResources().getString(R.string.pass_hint));
                        }else{
                            //ANDATA MALE
                            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_wrong),
                                    Toast.LENGTH_SHORT).show();
                            psw_to_change.setText("");
                            psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
                        }
                    }
                });
    }

    //controllo base della password
    private boolean passValid(String pass){
        return pass.length() > 5;
    }
}
