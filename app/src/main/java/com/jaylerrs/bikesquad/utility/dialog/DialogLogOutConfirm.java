package com.jaylerrs.bikesquad.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 11-Jul-17.
 */

public class DialogLogOutConfirm extends Dialog {

    public Activity activity;
    private Button mButtonSubmit;
    private Button mButtonCancel;

    public DialogLogOutConfirm(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_sign_out_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mButtonSubmit = (Button) findViewById(R.id.btn_ok);
        mButtonCancel = (Button) findViewById(R.id.btn_no);

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
}
