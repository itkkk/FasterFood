package it.uniba.di.ivu.sms16.gruppo3.fasterfood;


import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SignUpFragment extends Fragment{

    private TextInputLayout mPhoneEmail;
    private EditText mUsernameView, mPasswordView, mPhoneEmailView;
    private Button mEmailSignInButton;
    private ToggleButton mChangeOptionButton;
    private CheckBox mCheckPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.sign_up_name));


        setUpRegistration();

        if (mCheckPassword != null) {
            mCheckPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        }

        if (mEmailSignInButton != null) {
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                }
            });
        }

        if (mChangeOptionButton != null) {
            mChangeOptionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    choicePhoneEMail();
                }
            });
        }
    }


    private void setUpRegistration() {
        mUsernameView = (EditText) getView().findViewById(R.id.username);
        mPhoneEmail = (TextInputLayout) getView().findViewById(R.id.emailphone_login_form);
        if (mPhoneEmail != null) {
            mPhoneEmail.setHint(getString(R.string.my_phonenumber));
        }
        mPhoneEmailView = (EditText) getView().findViewById(R.id.emailphone);
        mPasswordView = (EditText) getView().findViewById(R.id.password);
        mCheckPassword = (CheckBox) getView().findViewById(R.id.check_password);
        mEmailSignInButton = (Button) getView().findViewById(R.id.email_sign_in_button);
        mChangeOptionButton = (ToggleButton) getView().findViewById(R.id.choice_email);
    }

    private void signUp() {
        mUsernameView.setError(null);
        mPhoneEmailView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String phoneNumber = mPhoneEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (checkSignUp(username,phoneNumber,password)) {
            // Gestire questo passaggio
            Toast.makeText(getActivity(),"Non Iscritto",Toast.LENGTH_SHORT).show();
        } else {
            // E' una prova. // Una volta effettuato il Login, i dati vanno salvati nel Database
            final Intent i = new Intent(getActivity(),LoginFragment.class);
            Toast.makeText(getActivity(),"Iscritto",Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    }

    private boolean checkSignUp(String username, String phoneNumber, String password) {
        boolean cancel = false;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneEmailView.setError(getString(R.string.error_field_required));
            mPhoneEmailView.requestFocus();
            cancel = true;
        } else if (mChangeOptionButton.getText().toString().equals(getString(R.string.action_choice_email))) {
            if (!isPhoneNumberValid(phoneNumber)) {
                mPhoneEmailView.setError(getString(R.string.error_incorrect_phonenumber));
                mPhoneEmailView.requestFocus();
                cancel = true;
            }
        } else {
            if (!isEmailValid(phoneNumber)) {
                mPhoneEmailView.setError(getString(R.string.error_invalid_email));
                mPhoneEmailView.requestFocus();
                cancel = true;
            }
        }


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
            cancel = true;
        }

        return cancel;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4 && password.matches(".*\\d.*");
    }

    private boolean isPhoneNumberValid(String phonenumber) {
        return phonenumber.length() == 10 && phonenumber.matches(".*\\d.*");
    }

    private void choicePhoneEMail() {
        if(mChangeOptionButton.getText().toString().equals(getString(R.string.action_chioce_phonenumber))) {
            mPhoneEmail.setHint(getString(R.string.my_email));
            mPhoneEmailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else {
            mPhoneEmail.setHint(getString(R.string.my_phonenumber));
            mPhoneEmailView.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }
}
