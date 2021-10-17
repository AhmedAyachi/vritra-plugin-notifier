package com.ahmedayachi.notifier;

import org.apache.cordova.*;
import com.ahmedayachi.notifier.Notification;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.pm.ApplicationInfo;


public class Notifier extends CordovaPlugin{

    static Context context;
    static ApplicationInfo appinfo;
    //static String packageName;
    static final String channelId="NotifierChannel";

    @Override
    public void initialize(CordovaInterface cordova,CordovaWebView webView){
        Notifier.context=cordova.getContext();
        Notifier.appinfo=Notifier.context.getApplicationInfo();
        //Notifier.packageName=Notifier.context.getPackageName();
        this.createNotificationChannel();
    }

    @Override
    public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException{
        if(action.equals("notify")) {
            JSONObject props=args.getJSONObject(0);
            this.notify(props,callbackContext);
            return true;
        }
        return false;
    }

    private void notify(JSONObject props,CallbackContext callbackContext) throws JSONException{
        final AppCompatActivity activity=cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable(){
            public void run(){
                try{
                    final Notification notification=new Notification(activity,props);

                }
                catch(JSONException exception){}
            }
        });
    }

    private void createNotificationChannel(){
        final AppCompatActivity activity=cordova.getActivity();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name="name";
            String description="description";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(channelId,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
}