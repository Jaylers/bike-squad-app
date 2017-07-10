package com.jaylerrs.bikesquad.auth.views;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaylerrs.bikesquad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserVerifyFragment extends Fragment {


    public UserVerifyFragment() {
        // Required empty public constructor
    }

    private View view;
    private Activity activity;
    private FirebaseAuth mAuth;
    private String TAG;
    private GoogleApiClient mGoogleApiClient;
    @BindView(R.id.txt_verify_detail) TextView mDetailTxt;
    @BindView(R.id.btn_verify_submit) Button mSubmit;
    @BindView(R.id.btn_verify_log_out) Button mLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_verify, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        mAuth = FirebaseAuth.getInstance();
        TAG = getString(R.string.ver_message_title);

        return view;
    }

    @OnClick(R.id.btn_verify_log_out) public void onLogOut(){
        revokeAccess();
    }

    @OnClick(R.id.btn_verify_submit) public void onVerify(){
        sendEmailVerification();
    }

    private void sendEmailVerification() {
        // Disable button
        mSubmit.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        mSubmit.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(activity,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(activity,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(activity, AuthActivity.class);
                        startActivity(intent);
                        activity.finish();
                    }
                });
    }

}
