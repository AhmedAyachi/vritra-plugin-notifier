package com.ahmedayachi.notifier;

import androidx.appcompat.app.AppCompatActivity;
import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;


public class TapHandler extends AppCompatActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Notifier.callback.success();
        /*try{
            final JSONObject object=null;
            object.put("message","worked");
            Notifier.callback.error(object);
        }
        catch(JSONException exception){}*/
    }
}
