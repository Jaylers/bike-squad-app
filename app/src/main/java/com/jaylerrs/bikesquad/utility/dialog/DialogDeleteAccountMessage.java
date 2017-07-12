package com.jaylerrs.bikesquad.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 11-Jul-17.
 */

public class DialogDeleteAccountMessage extends Dialog {

    public Activity activity;
    private String message;
    private TextView mMessage;
    private Button mButtonSubmit;
    private Button mButtonCancel;
    private EditText mPassword;

    public DialogDeleteAccountMessage(Activity activity, String message) {
        super(activity);
        this.activity = activity;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_get_single_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mMessage = (TextView) findViewById(R.id.txt_message);
        mMessage.setText(message);
        mButtonSubmit = (Button) findViewById(R.id.btn_ok);
        mButtonCancel = (Button) findViewById(R.id.btn_no);
        mPassword = (EditText) findViewById(R.id.edt_delete_password);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    public void onSubmit(){
        dismiss();
    }
    public void onCancel(){
        dismiss();
    }
    public String getPassword(){
        return mPassword.getText().toString();
    }

}
