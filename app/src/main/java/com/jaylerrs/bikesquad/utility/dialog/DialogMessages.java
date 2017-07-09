package com.jaylerrs.bikesquad.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 16-Feb-17.
 */

public class DialogMessages extends Dialog implements View.OnClickListener {

    public Activity activity;
    private Button yes, no;
    private String title, message;
    private TextView messageView;
    private TextView titleView;

    public DialogMessages(Activity activity, String title, String message) {
        super(activity);
        this.activity = activity;
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        titleView = (TextView) findViewById(R.id.txt_title);
        messageView = (TextView) findViewById(R.id.txt_message);
        titleView.setText(title);
        messageView.setText(message);
        yes = (Button) findViewById(R.id.btn_ok);
        yes.setOnClickListener(this);

        setButtonTheme(yes);
    }

    private void setButtonTheme(Button yes){
        yes.setBackgroundResource(R.drawable.button_bottom_round);
        GradientDrawable drawable = (GradientDrawable) yes.getBackground();
        /////////////////////////////////////////
        drawable.setCornerRadii( new float[] {0,0, 0,0, 40,40, 40,40});
        drawable.setColor(Color.GREEN);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}