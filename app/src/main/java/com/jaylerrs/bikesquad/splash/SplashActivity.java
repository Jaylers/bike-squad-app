package com.jaylerrs.bikesquad.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.auth.AuthActivity;
import com.jaylerrs.bikesquad.main.MainActivity;
import com.jaylerrs.bikesquad.utility.manager.ConnectionsManager;
import com.jaylerrs.bikesquad.utility.manager.LanguageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private ConnectionsManager connection;
    private static String TAG = null;
    private Activity activity;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private Boolean ver;

    @BindView(R.id.txt_splash_message) TextView mMessage;
    @BindView(R.id.relate_splash_base) RelativeLayout mRelate;
    @BindView(R.id.relate_splash) RelativeLayout mRelateSplash;
    @BindView(R.id.relate_splash_verify) RelativeLayout mRelateVerify;
    @BindView(R.id.txt_splash_warning) TextView mTxtWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        activity = this;
        TAG = this.getTitle().toString();
        mAuth = FirebaseAuth.getInstance();
        mRelateSplash.setVisibility(View.VISIBLE);
        mRelateVerify.setVisibility(View.GONE);
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

        LanguageManager languageManager = new LanguageManager(SplashActivity.this);
        languageManager.setApplicationLanguage();
        connection = new ConnectionsManager(SplashActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connectionChecker();
            }
        }, 800);
    }

    private void connectionChecker(){
        if (connection.isConnection()){
            connectionProperty();
        }else {
            connectionFailure();
        }
    }

    private void connectionFailure(){
        Snackbar.make(mRelate, getString(R.string.err_message_connection_failure),
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
            ver = currentUser.isEmailVerified();
            if (ver){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }else {
                mRelateSplash.setVisibility(View.GONE);
                mRelateVerify.setVisibility(View.VISIBLE);

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }else {
            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @OnClick(R.id.verify_email_button) public void onVerify(){
        sendEmailVerification();
    }

    @OnClick(R.id.verify_log_out_button) public void onLogOut(){
        mAuth.signOut();

        Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
        startActivity(intent);
        this.finish();

    }

    private void sendEmailVerification(){
        if (currentUser.isEmailVerified()){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                mTxtWarning.setText(getString(R.string.ver_message_detail_after_send));
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connectionFailure();
    }
}
