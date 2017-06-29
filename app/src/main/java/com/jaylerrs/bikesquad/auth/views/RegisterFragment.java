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
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }

    View view;
    @BindView(R.id.edt_reg_email) EditText reg_email;
    @BindView(R.id.edt_reg_password) EditText reg_pass;
    @BindView(R.id.edt_reg_confirm_password) EditText reg_con_pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btn_reg_submit) public void onRegister(){
        if (isRegisterForm()){

        }
    }

    @OnClick(R.id.txt_register_back) public void onBack(){
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_down_in,
                R.anim.slide_down_out);
        ft.replace(R.id.frame_fragment_base_auth, signInFragment);
        ft.commit();
    }

    private Boolean isRegisterForm(){
        if (reg_email.getText().toString().isEmpty()){
            reg_email.setError(getActivity().getString(R.string.err_message_required));
            return false;
        }else if (reg_pass.getText().toString().isEmpty()){
            reg_pass.setError(getActivity().getString(R.string.err_message_required));
            return false;
        }else if (reg_con_pass.getText().toString().isEmpty()){
            reg_con_pass.setError(getActivity().getString(R.string.err_message_required));
            return false;
        }else if (!(reg_pass.getText().toString().equals(reg_con_pass.getText().toString()))){
            reg_con_pass.setError(getActivity().getString(R.string.err_message_difference_password));
            reg_pass.setText("");
            reg_con_pass.setText("");
            return false;
        }else return true;
    }
}
