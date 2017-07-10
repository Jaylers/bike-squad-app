package com.jaylerrs.bikesquad.auth.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.MainActivity;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;
import com.jaylerrs.bikesquad.utility.manager.ConnectionsManager;
import com.jaylerrs.bikesquad.utility.manager.LanguageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    @BindView(R.id.txt_sign_in_message) TextView mMessage;
    @BindView(R.id.relate_auth_verify) RelativeLayout mRelat_verify;
    @BindView(R.id.relat_auth_sign_in) RelativeLayout mRelat_sign_in;
    @BindView(R.id.linear_auth_base) LinearLayout mAuth_base;

    private static final int RC_SIGN_IN = 9001;
    private ConnectionsManager connection;
    private static String TAG = null;
    private Activity activity;
    private Progressing progressing;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        activity = this;
        TAG = this.getTitle().toString();
        mAuth = FirebaseAuth.getInstance();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        currentUser = mAuth.getCurrentUser();

        LanguageManager languageManager = new LanguageManager(AuthActivity.this);
        languageManager.setApplicationLanguage();
        connection = new ConnectionsManager(AuthActivity.this);

        connectionChecker();
    }

    private void connectionChecker(){
        if (connection.isConnection()){
            connectionProperty();
        }else {
            mMessage.setText(getString(R.string.err_message_connection_failure));
            mMessage.setVisibility(View.VISIBLE);
            connectionFailure();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.img_sign_in_google) public void onGoogleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void connectionFailure(){
        Snackbar.make(mAuth_base, getString(R.string.err_message_connection_failure),
                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.message_retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectionChecker();
                    }
                }, 1000);
            }
        }).show();
    }

    private void connectionProperty(){
        if (currentUser != null){
            if (currentUser.isEmailVerified()){
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }else {
                mRelat_sign_in.setVisibility(View.GONE);
                mRelat_verify.setVisibility(View.VISIBLE);
            }
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog(getString(R.string.auth_message_signing_in));

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            activity.finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mMessage.setText(getString(R.string.auth_message_auth_failure_wrong_email_password));
                            mMessage.setVisibility(View.VISIBLE);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void showProgressDialog(String message){
        progressing = new Progressing(this, message);
        progressing.show();
    }

    private void hideProgressDialog(){
        if (progressing != null){
            if (progressing.isShowing()){
                progressing.dismiss();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog(getString(R.string.auth_message_signing_in));
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            openMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mMessage.setText(getString(R.string.auth_message_auth_failure));
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private int confirm = 0;
    @Override
    public void onBackPressed() {
        confirmToExit();
    }

    private void confirmToExit(){
        if (confirm>=1){
            confirm = 0;
            finish();
        }else {
            confirm++;
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.app_message_confirm_to_close_app),
                    Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirm = 0;
                }
            },2000);
        }
    }

    private void openMain(){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class Progressing extends DialogLoading {
        private Progressing(Activity activity, String message) {
            super(activity, message);
        }
    }
}
