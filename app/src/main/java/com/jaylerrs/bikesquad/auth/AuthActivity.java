package com.jaylerrs.bikesquad.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.events.models.User;
import com.jaylerrs.bikesquad.splash.SplashActivity;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    @BindView(R.id.txt_sign_in_message) TextView mMessage;

    private static final int RC_SIGN_IN = 9001;
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
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.img_sign_in_google) public void onGoogleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_auth_switch_sign_in) public void onSwitchSignInOption(){
        Intent intent = new Intent(AuthActivity.this, SignInActivity.class);
        startActivity(intent);
        activity.finish();
    }

    private void reStartApp(){
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.finish();
        startActivity(intent);
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseTag.users)
                .child(currentUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child(FirebaseTag.user_username).getValue() == null)){
                    Toast.makeText(AuthActivity.this,
                            getString(R.string.auth_message_welcome_back),
                            Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User(currentUser.getEmail(), username[0], "010101", "00", "00", false);
                    databaseReference.setValue(user);

                    Toast.makeText(AuthActivity.this,
                            getString(R.string.auth_message_welcome),
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
