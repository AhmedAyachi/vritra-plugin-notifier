package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action.Builder;
import android.graphics.Bitmap;
import org.json.JSONException;


public class Action{

    String label=null;
    IconCompat icon=null;
    public Action(JSONObject options) throws JSONException{
        label=options.getString("label");
        try{
            String iconPath=options.getString("icon");
            final Bitmap bitmap=Notifier.getBitmapIcon(iconPath);
            if(bitmap!=null){
                icon=IconCompat.createWithBitmap(bitmap);
            }
        }
        catch(JSONException exception){}
    }

    public void addTo(NotificationCompat.Builder notibuilder){
        /*
        final Intent intent=new Intent(activity,activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,0);
        */
        final Builder builder=new Builder(icon,label,null);
        notibuilder.addAction(actionbuilder.build());
    }
}
