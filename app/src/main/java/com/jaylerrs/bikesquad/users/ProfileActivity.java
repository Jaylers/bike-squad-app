package com.jaylerrs.bikesquad.users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.auth.AuthActivity;
import com.jaylerrs.bikesquad.users.task.DeleteAccount;
import com.jaylerrs.bikesquad.utility.dialog.DialogDeleteAccountMessage;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;
import com.jaylerrs.bikesquad.utility.dialog.DialogLogOutConfirm;
import com.jaylerrs.bikesquad.utility.dialog.DialogMessages;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.img_profile_image) ImageView mProfileImage;
    @BindView(R.id.txt_profile_name) TextView mProfileName;
    @BindView(R.id.txt_profile_email) TextView mProfileEmail;
    @BindView(R.id.txt_profile_phone_number) TextView mProfilePhoneNumber;
    @BindView(R.id.btn_profile_advance_log_out) Button mLogOut;
    @BindView(R.id.btn_profile_advance_delete_account) Button mDeleteAccount;
    @BindView(R.id.btn_profile_advance_option) Button mAdvanceOption;
    @BindView(R.id.btn_profile_advance_edit_account) Button mEditAccount;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private Progressing progressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        activity = this;
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

        setUserInformation();
    }

    private void setUserInformation(){
        currentUser = mAuth.getCurrentUser();

        Glide.with(activity)
                .load(currentUser.getPhotoUrl())
                .fitCenter()
                .animate(R.anim.fade_in)
                .placeholder(R.drawable.img_users)
                .error(R.drawable.ic_img_error)
                .into(mProfileImage);

        mProfileName.setText(currentUser.getDisplayName());
        mProfileEmail.setText(currentUser.getEmail());
        mProfilePhoneNumber.setText(currentUser.getPhoneNumber());
    }

    @OnClick(R.id.btn_profile_advance_option) public void onAdvanceOption(){
        if(mAdvanceOption.getText().toString().equals(getString(R.string.app_message_cancel))){
            mAdvanceOption.setText(getString(R.string.profile_message_advance_opt));
            mDeleteAccount.animate()
                    .alpha(0.0f)
                    .setDuration(400);
            mLogOut.animate()
                    .alpha(0.0f)
                    .setDuration(600);
            mEditAccount.animate()
                    .alpha(0.0f)
                    .setDuration(800);

            mEditAccount.setVisibility(View.GONE);
            mLogOut.setVisibility(View.GONE);
//            mDeleteAccount.setVisibility(View.GONE);

        }else {
            mAdvanceOption.setText(getString(R.string.app_message_cancel));
            mDeleteAccount.animate()
                    .alpha(1.0f)
                    .setDuration(700);
            mLogOut.animate()
                    .alpha(1.0f)
                    .setDuration(600);
            mEditAccount.animate()
                    .alpha(1.0f)
                    .setDuration(400);

            mEditAccount.setVisibility(View.VISIBLE);
            mLogOut.setVisibility(View.VISIBLE);
//            mDeleteAccount.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.txt_profile_back) public void onBack(){
        this.finish();
    }

    @OnClick(R.id.btn_profile_advance_log_out) public void onLogOut(){
        DialogLogout dialogLogout = new DialogLogout(activity);
        dialogLogout.show();
    }

    @OnClick(R.id.btn_profile_advance_delete_account) public void OnDeleteAccount(){
        DialogEditor dialogEditor = new DialogEditor(activity, getString(R.string.profile_message_prompt_delete));
        dialogEditor.show();
    }

    private void revokeAccess() {
        progressing = new Progressing(activity, getString(R.string.app_message_logging_out));
        progressing.show();
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
                        progressing.dismiss();
                        startActivity(intent);
                        activity.finish();
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class Progressing extends DialogLoading {

        public Progressing(Activity activity, String message) {
            super(activity, message);
        }
    }

    private class DialogEditor extends DialogDeleteAccountMessage {

        public DialogEditor(Activity activity, String message) {
            super(activity, message);
        }

        @Override
        public void onSubmit() {
            DeleteAccount deleteAccount = new DeleteAccount(activity);
            deleteAccount.reAuth(getPassword());
        }

        @Override
        public void onCancel() {
            super.onCancel();
        }
    }

    private class DialogLogout extends DialogLogOutConfirm {

        public DialogLogout(Activity activity) {
            super(activity);
        }

        @Override
        public void onSubmit() {
            DialogMessage dialogMessage = new DialogMessage(activity,
                    activity.getString(R.string.sign_out_message_title),
                    activity.getString(R.string.sign_out_message_ask_back));
            dismiss();
            dialogMessage.show();
        }
    }

    public class DialogMessage extends DialogMessages {

        public DialogMessage(Activity activity, String title, String message) {
            super(activity, title, message);
        }

        @Override
        public void onClick(View v) {
            dismiss();
            revokeAccess();
        }
    }

//    public class DialogGetMessage
}
