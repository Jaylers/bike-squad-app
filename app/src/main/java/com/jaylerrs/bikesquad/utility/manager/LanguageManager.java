package com.jaylerrs.bikesquad.utility.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.utility.sharedpreference.SharedLanguageSetting;
import com.jaylerrs.bikesquad.utility.sharedstring.SharedFlag;

import java.util.Locale;

/**
 * Created by jaylerr on 07-Jun-17.
 */

public class LanguageManager {
    private Activity activity;
    private Context context;
    private SharedLanguageSetting setting;

    public LanguageManager(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        setting = new SharedLanguageSetting(activity);
    }

    public void setApplicationLanguage(){
        String language = getLanguage();
        Configuration config = new Configuration();
        switch (language){
            case "TH" : config.locale = new Locale("th"); break;
            case "EN" : config.locale = Locale.ENGLISH; break;
            default: config.locale = Locale.ENGLISH; break;
        }
        activity.getResources().updateConfiguration(config, null);
    }

    public void reloadLanguage(){
        String language = getLanguage();
        Configuration config = new Configuration();
        switch (language){
            case "TH" : config.locale = new Locale("th"); break;
            case "EN" : config.locale = Locale.ENGLISH; break;
            default: config.locale = Locale.ENGLISH; break;
        }
        activity.getResources().updateConfiguration(config, null);

        ApplicationManager applicationManager = new ApplicationManager(activity);
        applicationManager.restartApplication();
    }

    public void setLanguage(String local){
        String text = SharedFlag.flag_unknown;
        setting.setLanguage(local);
        switch (getLanguage()){
            case "TH" : text = activity.getString(R.string.language_thailand); break;
            case "EN" : text = activity.getString(R.string.language_english); break;
        }
        Toast toast = Toast.makeText(context, activity.getString(R.string.app_message_app_language)
                .concat(text), Toast.LENGTH_SHORT);
        toast.show();
        reloadLanguage();
    }

    public String getLanguage(){
        return setting.getLanguage();
    }
}
