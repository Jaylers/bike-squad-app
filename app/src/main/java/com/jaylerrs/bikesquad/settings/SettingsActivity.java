package com.jaylerrs.bikesquad.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaylerrs.bikesquad.R;

import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }
}