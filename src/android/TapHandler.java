package com.ahmedayachi.notifications;

import androidx.appcompat.app.AppCompatActivity;
import com.ahmedayachi.notifications.Notifications;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;


public class TapHandler extends AppCompatActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Notifications.callback.success();
        /*try{
            final JSONObject object=null;
            object.put("message","worked");
            Notifications.callback.error(object);
        }
        catch(JSONException exception){}*/
    }
}
