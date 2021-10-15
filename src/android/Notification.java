package com.ahmedayachi.notifier;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.ahmedayachi.notifier.Notifier;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.Intent;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;


public class Notification{

    private JSONObject props=null;
    private NotificationCompat.Builder builder=null;
    private AppCompatActivity activity=null;
    public Notification(AppCompatActivity activity,JSONObject props) throws JSONException{
        this.props=props;
        this.activity=activity;
        Integer id=props.getInt("id");
        this.builder=new NotificationCompat.Builder(activity,Notifier.channelId);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.setSmallIcon();
        this.setLargeIcon();
        this.setTitle();
        this.setText();
        final Intent intent=new Intent(activity,activity.getClass());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);
        this.setActions();
        
        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(activity);
        notificationManager.notify(id.intValue(),builder.build());
    }

    private void setSmallIcon(){
        String icon;
        try{
            icon=props.getString("icon");
        }
        catch(JSONException exception){
            builder.setSmallIcon(Notifier.appinfo.icon);
        }
    }

    private void setLargeIcon() throws JSONException{
        String icon=props.getString("largeIcon");
        if(icon.equals("appIcon")){
            final Drawable drawable=Notifier.appinfo.loadIcon(Notifier.context.getPackageManager());
            final Bitmap bitMapIcon=((BitmapDrawable)drawable).getBitmap();
            builder.setLargeIcon(bitMapIcon);
        }
    }

    private void setTitle(){
        String title=null;
        try{
            title=props.getString("title");
        }
        catch(JSONException exception){}
        if(title!=null){
            builder.setContentTitle(title);
        }
    }

    private void setText(){
        String text=null;
        try{
            text=props.getString("text");
        }
        catch(JSONException exception){}
        if(text!=null){
            builder.setContentText(text);
        }
    }

    private void setActions(){
        JSONArray actions=null;
        try{
            actions=props.getJSONArray("actions");
            int length=actions.length();
            if(length>0){
                final Intent intent=new Intent(activity,TapHandler.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,0);
                for(int i=0;i<length;i++){
                    final JSONObject action=actions.getJSONObject(i);
                    final String label=action.getString("label");
                    Integer iconkey=null;
                    try{
                        iconkey=action.getInt("icon");
                    }
                    catch(JSONException exception){
                        iconkey=1;
                    };
                    final Action.Builder actionbuilder=new Action.Builder(iconkey.intValue(),label,pendingIntent);
                    builder.addAction(actionbuilder.build());
                }
            }
        }
        catch(JSONException exception){}
    }
}
