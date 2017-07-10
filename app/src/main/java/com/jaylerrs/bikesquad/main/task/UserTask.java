package com.jaylerrs.bikesquad.main.task;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 10-Jul-17.
 */

public class UserTask{

    private View mHeaderLayout;
    private Activity activity;
    private ImageView mImageProfile;
    private TextView mName;
    private TextView mEmail;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public UserTask(View mHeaderLayout, Activity activity) {
        this.mHeaderLayout = mHeaderLayout;
        this.activity = activity;
        mImageProfile = (ImageView) mHeaderLayout.findViewById(R.id.img_header_profile);
        mName = (TextView) mHeaderLayout.findViewById(R.id.txt_header_name);
        mEmail = (TextView) mHeaderLayout.findViewById(R.id.txt_header_email);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void setUserProfile(){
        mName.setText(currentUser.getDisplayName());
        mEmail.setText(currentUser.getEmail());
    }
}
