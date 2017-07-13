package com.jaylerrs.bikesquad.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.MainActivity;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;
import com.jaylerrs.bikesquad.utility.manager.ConnectionsManager;
import com.jaylerrs.bikesquad.utility.manager.LanguageManager;
import com.jaylerrs.bikesquad.utility.sharedstring.SharedRef;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    @BindView(R.id.txt_sign_in_message) TextView mMessage;
    @BindView(R.id.relate_auth_verify) RelativeLayout mRelate_verify;
    @BindView(R.id.relat_auth_sign_in) RelativeLayout mRelate_sign_in;
    @BindView(R.id.linear_auth_base) LinearLayout mAuth_base;
    @BindView(R.id.txt_verify_detail) TextView mVerify_detail;
    @BindView(R.id.btn_verify_log_out) Button mVerify_log_out;
    @BindView(R.id.btn_verify_submit) Button mVerify;


    private static final int RC_SIGN_IN = 9001;
    private ConnectionsManager connection;
    private static String TAG = null;
    private Activity activity;
    private Progressing progressing;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

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

    @OnClick(R.id.btn_verify_submit)public void onVerify(){
        sendEmailVerification();
    }

    @OnClick(R.id.btn_verify_log_out) public void onLogOut(){
        Log.i("User", "Sign out");
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        reStartApp();
                    }
                });
        //reStartApp();
    }

    private void reStartApp(){
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.finish();
        startActivity(intent);
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
            Boolean aa = currentUser.isEmailVerified();
            if (currentUser.isEmailVerified()){
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }else {
                mRelate_sign_in.setVisibility(View.GONE);
                mRelate_verify.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sendEmailVerification() {
        // Disable button
        mVerify.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        mVerify.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            mVerify_detail.setText(getString(R.string.ver_message_verify_send_to));
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(AuthActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
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
                mMessage.setText(getString(R.string.err_message_something_wrong));
                mMessage.setVisibility(View.VISIBLE);
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
                            addUserInformation();
                            reStartApp();
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

    private void addUserInformation(){
        currentUser = mAuth.getCurrentUser();
        final String[] username = currentUser.getEmail().toString().split("@");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(SharedRef.ref_user)
                .child(currentUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("username").getValue() == null)){
                    Toast.makeText(AuthActivity.this,
                            "Welcome back.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child(SharedRef.ref_user).setValue(currentUser.getUid());
                    databaseReference.child("username").setValue(username[0]);
                    databaseReference.child("birthDate").setValue("010101");
                    databaseReference.child("weight").setValue("0");
                    databaseReference.child("height").setValue("0");
                    databaseReference.child("privacy").setValue("false");

                    Toast.makeText(AuthActivity.this,
                            "Welcome to Bike Squad.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get USER", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

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
