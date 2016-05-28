package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox mCheckBoxRememberPw;
    private TextView mRegisterNow;
    private Button mEmailLogInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });

    }

    private void setUpLogIn() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line,
                new String[] {"paperina","paperone","paperino","paperoga","tat"} // E' una prova
        );
        mEmailView.setAdapter(adapter);
        mPasswordView = (EditText) findViewById(R.id.password);
        mCheckBoxRememberPw   = (CheckBox) findViewById(R.id.check_password);
        mEmailLogInButton = (Button) findViewById(R.id.email_sign_in_button);
        mRegisterNow = (TextView) findViewById(R.id.register);
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
            Toast.makeText(this,"Login avvenuto con successo",Toast.LENGTH_SHORT).show();
            final Intent i = new Intent(this,SignUpActivity.class);
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

