package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import android.widget.Toast;
import android.view.Gravity;

class ToastView{
    
    
    private JSONObject options=null;
    private Toast toast=null;

    public ToastView(JSONObject options){
        if(options!=null){
            this.options=options;
            String text=options.optString("text","");
            toast=Toast.makeText(Notifier.context,text,getDuration());
        }
    }

    private int getDuration(){
        String lasting=options.optString("lasting","short");
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
    public void cancel(){
        if(toast!=null){
            toast.cancel();
        }
    }
}
