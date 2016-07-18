package it.uniba.di.ivu.sms16.gruppo3.fasterfood.login_signup_screen;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.AppConfiguration;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class LoginFragment extends Fragment{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mRegisterNow;
    private TextView mForgotPass;
    private Button mEmailLogInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private HomeActivity activity;
    private boolean LOGGING;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setIsLoginFragmentAttached(true);
        //dopo la registrazione torno al login fragment, lo chiudo e mostro il fragment presente nel backstack
        if(AppConfiguration.isLogged()) {
            ((HomeActivity)getActivity()).setIsLoginFragmentAttached(false);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((HomeActivity)getActivity()).setIsLoginFragmentAttached(false);
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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.login_name));

        setUpLogIn();
        activity = (HomeActivity) getActivity();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if(LOGGING) {
                        AppConfiguration.setLogged(true);
                        AppConfiguration.setUser(user.getEmail());
                        Snackbar.make(getView(), getResources().getString(R.string.logged_in), Snackbar.LENGTH_LONG).show();
                        activity.checkLogged();
                        LOGGING = false;
                        activity.onBackPressed();
                    }
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        mEmailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPasswordView.setText((CharSequence) parent.getItemAtPosition(position));
            }
        });



        if (mEmailLogInButton != null) {
            mEmailLogInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    LOGGING = true;
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

        //recupero pass
        mForgotPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mEmailView.setError(null);
                final String email = mEmailView.getText().toString();
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                }else{
                    final Task<Void> task = mAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(getView(),getResources().getString(R.string.pass_reset) + " " + email,Snackbar.LENGTH_LONG).show();
                                }
                            }
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
            }
            }
        });
    }


    private void setUpLogIn() {
        mEmailView = (AutoCompleteTextView) getView().findViewById(R.id.email);
        mPasswordView = (EditText) getView().findViewById(R.id.password);
        mEmailLogInButton = (Button) getView().findViewById(R.id.email_sign_in_button);
        mRegisterNow = (TextView) getView().findViewById(R.id.register);
        mForgotPass = (TextView) getView().findViewById(R.id.forgotpassword);
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //effettuo il login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                LOGGING = false;
                                Snackbar.make(getView(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}

