package com.jaylerrs.bikesquad.users.task;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.users.model.UserInfo;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

/**
 * Created by jaylerr on 13-Jul-17.
 */

public class UserInformation {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private ValueEventListener mUserListener;
    private Activity activity;

    private EditText mEdtBirthDate;
    private EditText mEdtWeight;
    private EditText mEdtHeight;

    private TextView mTxtUsername;
    private TextView mTxtBirthDate;
    private TextView mTxtWeight;
    private TextView mTxtHeight;

    private Switch mSwtPrivacy;
    private Switch mSwtPrivacyEdt;
    private Button mEdtSave;
    private Button mEdtCancel;

    public UserInformation(Activity activity) {
        this.activity = activity;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mEdtBirthDate = (EditText) activity.findViewById(R.id.edt_profile_birth_date_value);
        mEdtWeight = (EditText) activity.findViewById(R.id.edt_profile_weight_value);
        mEdtHeight = (EditText) activity.findViewById(R.id.edt_profile_height_value);

        mTxtUsername = (TextView) activity.findViewById(R.id.txt_profile_username_value);
        mTxtBirthDate = (TextView) activity.findViewById(R.id.txt_profile_birth_date_value);
        mTxtWeight =(TextView) activity.findViewById(R.id.txt_profile_weight_value);
        mTxtHeight = (TextView) activity.findViewById(R.id.txt_profile_height_value);

        mSwtPrivacy = (Switch) activity.findViewById(R.id.swt_profile_privacy_value);
        mSwtPrivacyEdt = (Switch) activity.findViewById(R.id.swt_profile_privacy_edt);
        mEdtSave = (Button) activity.findViewById(R.id.edt_profile_save);
        mEdtCancel = (Button) activity.findViewById(R.id.edt_profile_cancel);
    }

    public UserInformation() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void displayInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseTag.users).child(currentUser.getUid());
        databaseReference.keepSynced(false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTxtUsername.setText(dataSnapshot.child(FirebaseTag.user_username).getValue().toString());
                mTxtBirthDate.setText(dataSnapshot.child(FirebaseTag.user_birthDate).getValue().toString());
                mTxtWeight.setText(dataSnapshot.child(FirebaseTag.user_weight).getValue().toString());
                mTxtHeight.setText(dataSnapshot.child(FirebaseTag.user_height).getValue().toString());
                mSwtPrivacy.setChecked(dataSnapshot.child(FirebaseTag.user_privacy).getValue().toString().equals("true"));

                mEdtBirthDate.setText(dataSnapshot.child(FirebaseTag.user_birthDate).getValue().toString());
                mEdtWeight.setText(dataSnapshot.child(FirebaseTag.user_weight).getValue().toString());
                mEdtHeight.setText(dataSnapshot.child(FirebaseTag.user_height).getValue().toString());
                mSwtPrivacyEdt.setChecked(dataSnapshot.child(FirebaseTag.user_privacy).getValue().toString().equals("true"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get USER", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void getInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseTag.users).child(currentUser.getUid());
        databaseReference.keepSynced(false);
        final UserInfo userInfo = new UserInfo();
        final String[] username = {null};

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo.setEmail(currentUser.getEmail());
                username[0] = dataSnapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get USER", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void disableEditor(){
        mEdtBirthDate.setVisibility(View.GONE);
        mEdtHeight.setVisibility(View.GONE);
        mEdtWeight.setVisibility(View.GONE);
        mSwtPrivacyEdt.setVisibility(View.GONE);

        mEdtSave.setVisibility(View.GONE);
        mEdtCancel.setVisibility(View.GONE);

        mSwtPrivacy.setVisibility(View.VISIBLE);
        mTxtBirthDate.setVisibility(View.VISIBLE);
        mTxtWeight.setVisibility(View.VISIBLE);
        mTxtHeight.setVisibility(View.VISIBLE);
    }

    public void enableEditor(){
        mEdtBirthDate.setVisibility(View.VISIBLE);
        mEdtHeight.setVisibility(View.VISIBLE);
        mEdtWeight.setVisibility(View.VISIBLE);
        mSwtPrivacyEdt.setVisibility(View.VISIBLE);

        mEdtSave.setVisibility(View.VISIBLE);
        mEdtCancel.setVisibility(View.VISIBLE);

        mSwtPrivacy.setVisibility(View.GONE);
        mTxtBirthDate.setVisibility(View.GONE);
        mTxtWeight.setVisibility(View.GONE);
        mTxtHeight.setVisibility(View.GONE);
    }

    public void setUserInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseTag.users);

        DatabaseReference userinfo = databaseReference.child(currentUser.getUid());
        userinfo.child(FirebaseTag.users).setValue(currentUser.getEmail());
        if(!mEdtBirthDate.getText().toString().isEmpty()){
            userinfo.child(FirebaseTag.user_birthDate).setValue(mEdtBirthDate.getText().toString());
        }
        if (!mEdtWeight.getText().toString().isEmpty()){
            userinfo.child(FirebaseTag.user_weight).setValue(mEdtWeight.getText().toString());
        }
        if (!mEdtHeight.getText().toString().isEmpty()){
            userinfo.child(FirebaseTag.user_height).setValue(mEdtHeight.getText().toString());
        }
        userinfo.child(FirebaseTag.user_privacy).setValue(mSwtPrivacyEdt.isChecked());
    }

    private Boolean isBirthDate(){
        String bd = mEdtBirthDate.getText().toString();
        if (bd.length()!=6){
            String bd2 = bd.substring(1);
            return true;
        }else return false;
    }
}
