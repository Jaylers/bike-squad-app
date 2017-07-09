package com.jaylerrs.bikesquad.auth.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.MainActivity;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_reg_email) EditText mReg_email;
    @BindView(R.id.edt_reg_password) EditText mReg_password;
    @BindView(R.id.edt_reg_confirm_password) EditText mReg_pass_confirm;
    private FirebaseAuth mAuth;
    private String TAG;
    private Progressing progressing;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activity = this;
        mAuth = FirebaseAuth.getInstance();
        TAG = this.getTitle().toString();
    }

    @OnClick(R.id.btn_reg_submit) public void onRegister(){
        if (isFill()){
            if (isForm()){
                createAccount();
            }
        }
    }

    @OnClick(R.id.txt_register_back) public void onBack(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        this.finish();
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

    private void createAccount() {
        String email = mReg_email.getText().toString();
        String password = mReg_password.getText().toString();
        Log.d(TAG, "createAccount:" + email);
        showProgressDialog(getString(R.string.reg_message_registering));

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            activity.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mReg_password.setText("");
                            mReg_pass_confirm.setText("");
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private Boolean isForm(){
        String txt_email = mReg_email.getText().toString();
        if (txt_email.contains("@")){
            String[] arr_email = txt_email.split("@");
            if (arr_email[1].contains(".")){
                return true;
            }else{
                mReg_email.setError(getString(R.string.err_message_not_email));
                return false;
            }
        }else{
            mReg_email.setError(getString(R.string.err_message_not_email));
            return false;
        }
    }

    private Boolean isFill(){
        if (mReg_email.getText().toString().isEmpty()){
            mReg_email.setError(getString(R.string.err_message_required));
            return false;
        }else if (mReg_password.getText().toString().isEmpty()){
            mReg_password.setError(getString(R.string.err_message_required));
            return false;
        }else if (mReg_pass_confirm.getText().toString().isEmpty()){
            mReg_pass_confirm.setError(getString(R.string.err_message_required));
            return false;
        }else if (!(mReg_password.getText().toString().equals(mReg_pass_confirm.getText().toString()))){
            mReg_pass_confirm.setError(getString(R.string.err_message_difference_password));
            return false;
        }else return true;
    }

    private class Progressing extends DialogLoading {
        private Progressing(Activity activity, String message) {
            super(activity, message);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
        startActivity(intent);
        activity.finish();
    }
}
