package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox mCheckBoxRememberPw;
    private TextView mRegisterNow;
    private Button mEmailLogInButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).IS_LOGIN_FRAGMENT_ATTACHED = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((HomeActivity)getActivity()).IS_LOGIN_FRAGMENT_ATTACHED = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.login_name));
        setUpLogIn();

        mEmailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPasswordView.setText((CharSequence) parent.getItemAtPosition(position));
            }
        });
        /*if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Qualora dovessi cambiare la spunta della checkBox
                    if (!isChecked) {
                        // mostra password
                        mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        // rinascondi password
                        mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                }
            });
        }*/


        if (mEmailLogInButton != null) {
            mEmailLogInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }

        mRegisterNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment, new SignUpFragment()).addToBackStack(null).commit();
            }
        });
    }


    private void setUpLogIn() {
        mEmailView = (AutoCompleteTextView) getView().findViewById(R.id.email);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_dropdown_item_1line,
                new String[] {"paperina","paperone","paperino","paperoga","tat"} // E' una prova
        );
        mEmailView.setAdapter(adapter);
        mPasswordView = (EditText) getView().findViewById(R.id.password);
        mCheckBoxRememberPw   = (CheckBox) getView().findViewById(R.id.check_password);
        mEmailLogInButton = (Button) getView().findViewById(R.id.email_sign_in_button);
        mRegisterNow = (TextView) getView().findViewById(R.id.register);
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Toast.makeText(getActivity(),"Login avvenuto con successo",Toast.LENGTH_SHORT).show();
            final Intent i = new Intent(getActivity() ,SignUpFragment.class);
            startActivity(i);
        }
    }

    // Verificare se l'Email è corretta
    private boolean isEmailValid(String email) {
        //TODO
        return true;
    }

    // Verificare se il numero di Telefono è corretta
    private boolean isPhoneValid(String email) {
        //TODO
        return true;
    }

    // Verificare se la Password è corretta
    private boolean isPasswordValid(String password) {
        return true;
    }

}

