package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notification;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.core.app.RemoteInput;
import android.os.Bundle;

public class ActionListener extends BroadcastReceiver{

    @Override
    public void onReceive(Context context,Intent intent){
        String id=intent.getStringExtra("id");
        String type=intent.getStringExtra("type");
        int notificationId=intent.getIntExtra("notificationId",-7);
        try{
            JSONObject params=new JSONObject();
            params.put("value",id);
            params.put("type",type);
            CallbackContext callback=(CallbackContext)Notification.callbacks.get(id);
            if(type.equals("input")||type.equals("select")){
                Bundle bundle=RemoteInput.getResultsFromIntent(intent);
                String input=bundle.getString(id);
                params.put("input",input);
            }
            callback.success(params);
            Notification.notificationManager.cancel(notificationId);
            Notification.callbacks.remove(id);
        }
        catch(JSONException exception){}
        
    }
}
