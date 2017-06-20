package com.jaylerrs.bikesquad.utility.manager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.jaylerrs.bikesquad.R;

/**
 * Created by jaylerr on 20-Apr-17.
 */

public class MobileManager {
    private Activity activity;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public MobileManager(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Call(String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:".concat(number)));
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    showMessageOKCancel(activity.getString(R.string.app_message_request_permission_call),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                } else activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_ASK_PERMISSIONS);
            }else {
                activity.startActivity(intent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.app_message_permission_denied),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(activity.getString(R.string.app_message_ok), okListener)
                .setNegativeButton(activity.getString(R.string.app_message_cancel), null)
                .create()
                .show();
    }
}
