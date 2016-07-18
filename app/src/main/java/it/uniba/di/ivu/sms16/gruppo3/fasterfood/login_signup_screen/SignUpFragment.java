package it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen.RegisterDialog;

public class SignUpFragment extends Fragment{

    private TextInputLayout mEmail;
    private EditText mPasswordView, mEmailView;
    private Button mEmailSignInButton;
    private CheckBox mCheckPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.sign_up_name));
        activity = (HomeActivity) getActivity();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    AppConfiguration.setLogged(true);
                    AppConfiguration.setUser(user.getEmail());
                    RegisterDialog registerDialog = new RegisterDialog();
                    registerDialog.show(getFragmentManager(), null);
                    activity.checkLogged();
                    activity.onBackPressed();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
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
                    //chiude la tastiera
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    signUp();
                }
            });
        }
    }

    private void setUpRegistration() {
        mEmail = (TextInputLayout) getView().findViewById(R.id.emailphone_login_form);
        if (mEmail != null) {
            mEmail.setHint(getString(R.string.my_email));
        }
        mEmailView = (EditText) getView().findViewById(R.id.email);
        mPasswordView = (EditText) getView().findViewById(R.id.password);
        mCheckPassword = (CheckBox) getView().findViewById(R.id.check_password);
        mEmailSignInButton = (Button) getView().findViewById(R.id.email_sign_in_button);
    }

    private void signUp() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (!checkSignUp(email,password)) {
            //effettuo la registrazione
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful())
                                Snackbar.make(getView(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean checkSignUp(String email, String password) {
        boolean cancel = false;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            cancel = true;
        } else {
            if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
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
        return password.length() > 5;
    }

}