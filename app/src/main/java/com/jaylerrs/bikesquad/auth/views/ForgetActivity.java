package com.jaylerrs.bikesquad.auth.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaylerrs.bikesquad.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        activity = this;
    }

    @OnClick(R.id.txt_forget_back) public void onBack(){
        Intent intent = new Intent(ForgetActivity.this, AuthActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetActivity.this, AuthActivity.class);
        startActivity(intent);
        activity.finish();
    }


}
