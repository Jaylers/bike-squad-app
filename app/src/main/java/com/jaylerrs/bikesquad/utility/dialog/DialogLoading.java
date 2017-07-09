package com.jaylerrs.bikesquad.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 07-Jul-17.
 */

public class DialogLoading extends Dialog{
    public Activity activity;
    private String message;
    private TextView messageView;

    public DialogLoading(Activity activity, String message) {
        super(activity);
        this.activity = activity;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        messageView = (TextView) findViewById(R.id.txt_message);
        messageView.setText(message);
    }
}
