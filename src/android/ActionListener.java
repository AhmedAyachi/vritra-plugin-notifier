package com.wurm.notifier;

import com.wurm.notifier.Notification;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.apache.cordova.CallbackContext;
import org.json.JSONObject;
import androidx.core.app.RemoteInput;
import android.os.Bundle;
import org.json.JSONException;


public class ActionListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent){
        try{
            final int notificationId=intent.getIntExtra("notificationId",-1);
            final CallbackContext callback=(CallbackContext)Notification.callbacks.opt(Integer.toString(notificationId));
            if(callback!=null){
                final String ref=intent.getStringExtra("ref");
                final String type=intent.getStringExtra("type");
                JSONObject params=new JSONObject();
                params.put("ref",ref);
                params.put("type",type);
                if(type.matches("input|select")){
                    final Bundle bundle=RemoteInput.getResultsFromIntent(intent);
                    final String input=bundle.getString(ref);
                    params.put("input",input);
                }
                callback.success(params);
                final Boolean once=intent.getBooleanExtra("once",true);
                if(once){
                    Notifier.dismiss(notificationId);
                }
                
            }
        }
        catch(JSONException exception){}
    }
}
