package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import android.widget.Toast;
import android.view.Gravity;

class ToastView{
    
    private JSONObject props=null;
    private Toast toast=null;

    public ToastView(JSONObject props){
        if(props!=null){
            this.props=props;
            String text=props.optString("text","");
            toast=Toast.makeText(Notifier.context,text,getDuration());
        }
    }

    private int getDuration(){
        String lasting=props.optString("lasting","short");
        int duration=Toast.LENGTH_SHORT;
        if(lasting.equals("long")){
            duration=Toast.LENGTH_LONG;
        }
        return duration;
    }

    public void show(){
        if(toast!=null){
            toast.show();
        }
    }
}
