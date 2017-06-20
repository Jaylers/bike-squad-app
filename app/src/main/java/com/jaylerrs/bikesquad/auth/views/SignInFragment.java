package com.jaylerrs.bikesquad.auth.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jaylerrs.bikesquad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    View view;
    @BindView(R.id.edt_sign_in_username) EditText edt_username;
    @BindView(R.id.edt_sign_in_password) EditText edt_password;
    String password, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btn_sign_in) public void onSignIn(){
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();
        if (isForm()){

        }
    }

    @OnClick(R.id.txt_sign_in_register) public void onRegister(){
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,
                R.anim.fade_out);
        ft.replace(R.id.frame_fragment_base_auth, registerFragment);
        ft.commit();
    }

    private Boolean isForm(){
        if (username.isEmpty()){
            edt_username.setError(getString(R.string.err_message_required));
            return false;
        }else if (password.isEmpty()){
            edt_password.setError(getString(R.string.err_message_required));
            return false;
        }else if (isEmail()){
            return true;
        }else if (isUsername()){
            return true;
        }else return false;
    }

    private Boolean isEmail(){
        String user[] = username.split("@");
        if (user[0].length() >= 5 && user[1].contains(".")){
            return true;
        }else {
            edt_username.setError(getString(R.string.err_message_not_email));
            return false;
        }
    }

    private Boolean isUsername(){
        return username.length()>=5;
    }

}
