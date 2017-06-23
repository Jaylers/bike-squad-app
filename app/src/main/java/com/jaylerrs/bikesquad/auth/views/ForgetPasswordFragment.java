package com.jaylerrs.bikesquad.auth.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaylerrs.bikesquad.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.txt_forget_back) public void onBack(){
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
        ft.replace(R.id.frame_fragment_base_auth, signInFragment);
        ft.commit();
    }

}
