package com.jaylerrs.bikesquad.auth.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.MainActivity;
import com.jaylerrs.bikesquad.utility.manager.ConnectionsManager;
import com.jaylerrs.bikesquad.utility.manager.LanguageManager;
import com.jaylerrs.bikesquad.utility.sharedpreference.SharedSignedUser;
import com.jaylerrs.bikesquad.utility.sharedstring.SharedFlag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.txt_auth_message) TextView message;
    @BindView(R.id.btn_retry) Button btn_retry;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    ConnectionsManager connection;
    SharedSignedUser sharedSignedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        LanguageManager languageManager = new LanguageManager(AuthActivity.this);
        languageManager.setApplicationLanguage();
        connection = new ConnectionsManager(AuthActivity.this);
        sharedSignedUser = new SharedSignedUser(AuthActivity.this);
        checkConnection();
    }

    @OnClick(R.id.btn_retry) public void onRetry(){
        btn_retry.animate()
                .alpha(0.0f)
                .translationY(btn_retry.getHeight())
                .setDuration(300);
        progressBar.animate()
                .alpha(1.0f)
                .setDuration(300);
        message.setText(getString(R.string.err_message_connecting));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkConnection();
            }
        },1500);
    }

    private void checkConnection(){
        if (connection.isConnection()){
            doAuthentication();
            progressBar.animate().alpha(0.0f).setDuration(300);
            progressBar.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
        }else {
            progressBar.animate().alpha(0.0f).setDuration(300);
            message.animate().alpha(1.0f).setDuration(300);
            btn_retry.setVisibility(View.VISIBLE);
            btn_retry.animate().translationY(0).alpha(1.0f).setDuration(300);
            message.setText(getString(R.string.err_message_connection_failure));
            message.animate().alpha(1.0f).setDuration(300);
        }
    }

    private void doAuthentication(){
        if (isSigned()){
            signIn();
        }else {
            openBaseAuth();
        }
    }

    private void signIn(){
        if (sharedSignedUser.getStateSignIn().equals(SharedFlag.flag_descendant)){
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            openBaseAuth();
        }
    }

    private void openBaseAuth(){
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,
                R.anim.fade_out);
        ft.replace(R.id.frame_fragment_base_auth, signInFragment);
        ft.commit();
    }

    private Boolean isSigned(){
        if (sharedSignedUser.getStateSignIn().equals(SharedFlag.flag_unknown)){
            return false;
        }else return true;
    }

    private int confirm = 0;
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame_fragment_base_auth);

        SignInFragment signInFragment = new SignInFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (currentFragment instanceof SignInFragment){
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
        }else if (currentFragment instanceof SettingsFragment){
            ft.setCustomAnimations(R.anim.slide_down_in,
                    R.anim.slide_down_out);
            ft.replace(R.id.frame_fragment_base_auth, signInFragment);
            ft.commit();
        }else {
            ft.setCustomAnimations(R.anim.fade_in,
                    R.anim.fade_out);
            ft.replace(R.id.frame_fragment_base_auth, signInFragment);
            ft.commit();
        }
    }
}
