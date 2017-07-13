package com.jaylerrs.bikesquad.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 16-Feb-17.
 */

public class DialogNewsMessages extends Dialog implements View.OnClickListener {

    public Activity activity;
    private ImageView mClose;
    private ImageView mImage;

    public DialogNewsMessages(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_news);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mClose = (ImageView) findViewById(R.id.img_news_cancel);
        mImage = (ImageView) findViewById(R.id.img_news);

        mClose.setOnClickListener(this);
    }

    public void setNews(String url){
        Glide.with(activity)
                .load(url)
                .fitCenter()
                .animate(R.anim.fade_in)
                .placeholder(R.drawable.img_users)
                .error(R.drawable.ic_img_error)
                .into(mImage);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}