package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notification;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;


public class ActionListener extends BroadcastReceiver{

    @Override
    public void onReceive(Context context,Intent intent){
        String id=intent.getStringExtra("id");
        int notificationId=intent.getIntExtra("notificationId",-7);
        try{
            CallbackContext callback=(CallbackContext)Notification.callback.get(id);
            callback.success(id);
            Notification.notificationManager.cancel(notificationId);
            Notification.callback.remove(id);
        }
        catch(JSONException exception){}
        
    }
}
