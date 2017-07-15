package com.jaylerrs.bikesquad.settings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jaylerrs.bikesquad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    public SettingFragment() {
        // Required empty public constructor
    }

    private View view;
    private Activity activity;
    @BindView(R.id.relative_setting_language) RelativeLayout language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();

        return view;
    }

    @OnClick(R.id.relative_setting_language) public void onSetting(){
        doLanguage();
    }


    private void doLanguage(){
        LanguageListFragment languageListFragment = new LanguageListFragment();
        FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
        ft.replace(R.id.frame_fragment_control, languageListFragment);
        ft.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
