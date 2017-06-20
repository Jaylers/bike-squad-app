package com.jaylerrs.bikesquad.utility.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jaylerrs.bikesquad.auth.views.AuthActivity;

/**
 * Created by jaylerr on 07-Jun-17.
 */

public class ApplicationManager {
    Activity activity;
    Context context;

    public ApplicationManager(Context context) {
        this.context = context;
        this.activity = (Activity) context;

    }

    public void signOut(){
//        SharedSignedUser sharedSignedUser = new SharedSignedUser(activity);
//        sharedSignedUser.setStateSignIn(SharedFlag.flag_unknown);
//        Intent intent = new Intent(activity, AuthActivity.class);
//        activity.startActivity(intent);
//        activity.finish();
    }

    public void restartApplication(){
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
