package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;
import android.view.Gravity;
import android.view.ViewOutlineProvider;
import android.util.DisplayMetrics;
import android.graphics.Outline;



class ToastView{
    
    private JSONObject props=null;
    private Toast toast=null;

    public ToastView(JSONObject props){
        if(props!=null){
            this.props=props;
            String text=props.optString("text","");
            toast=Toast.makeText(Notifier.context,text,getDuration());
            View view=toast.getView();
            setTextColor(view);
            setBackgroundColor(view);
            setBorderRadius(view);
            setPosition();
        }
    }

    private void setTextColor(View view){
        TextView textview=(TextView)view.findViewById(android.R.id.message);
        String textColor=props.optString("color","#000000");
        textview.setTextColor(Notifier.getColor(textColor));
    }

    private void setBackgroundColor(View view){
        String backgroundColor=props.optString("backgroundColor","white");
        view.setBackgroundColor(Notifier.getColor(backgroundColor));
        view.setAlpha(0.9f);
    }

    private int getDuration(){
        String lasting=props.optString("lasting","short");
        int duration=Toast.LENGTH_SHORT;
        if(lasting.equals("long")){
            duration=Toast.LENGTH_LONG;
        }
        return duration;
    }

    private void setBorderRadius(View view){
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider(){
            @Override
            public void getOutline(View view,Outline outline){
                outline.setRoundRect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom(),65.0f);
            }
        });
    }

    private void setPosition(){
        final int gravity=getGravity();
        double offset=0;
        if(gravity!=Gravity.CENTER){
            DisplayMetrics displayMetrics=Notifier.context.getResources().getDisplayMetrics();
            int height=displayMetrics.heightPixels;
            offset=200*height/2340;
        }
        toast.setGravity(gravity,0,(int)offset);
    }

    private int getGravity(){
        final String verticalAlign=props.optString("verticalAlign","bottom");
        switch(verticalAlign){
            case "top": return Gravity.TOP;
            case "middle": return Gravity.CENTER;
            default: return Gravity.BOTTOM;
        }
    }

    public void show(){
        if(toast!=null){
            toast.show();
        }
    }
}
