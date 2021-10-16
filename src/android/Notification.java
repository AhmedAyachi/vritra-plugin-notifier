package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import org.json.JSONObject;
import org.json.JSONException;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.Intent;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.IconCompat;


public class Notification{

    
    public Notification(AppCompatActivity activity,JSONObject props) throws JSONException{
        this.props=props;
        this.activity=activity;
        int id=props.getInt("id");
        this.builder=new NotificationCompat.Builder(activity,Notifier.channelId);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.setSmallIcon();
        this.setLargeIcon();
        this.setTitle();
        this.setText();
        final Intent openintent=new Intent(context,activity.getClass());
        openintent.addCategory(Intent.CATEGORY_LAUNCHER);
        openintent.setAction(Intent.ACTION_MAIN);
        /*final Intent notiintent=new Intent(activity,TapHandler.class);
        notiintent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        final Intent[] intents={openintent,notiintent};*/
        final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,openintent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);
        this.setActions();
        
        final NotificationManagerCompat notificationManager=NotificationManagerCompat.from(activity);
        notificationManager.notify(id,builder.build());
    }

    private void setSmallIcon(){
        try{
            String url=props.getString("icon");
            IconCompat icon=IconCompat.createWithContentUri(url);
            builder.setSmallIcon(icon);
        }
        catch(JSONException exception){
            builder.setSmallIcon(Notifier.appinfo.icon);
        }
    }

    private void setLargeIcon(){
        try{
            String icon=props.getString("largeIcon");
            if(icon.equals("appIcon")){
                final Drawable drawable=Notifier.appinfo.loadIcon(Notifier.context.getPackageManager());
                final Bitmap bitMapIcon=((BitmapDrawable)drawable).getBitmap();
                builder.setLargeIcon(bitMapIcon);
            }
        }
        catch(JSONException exception){}
    }

    private void setTitle(){
        try{
            String title=props.getString("title");
            builder.setContentTitle(title);
        }
        catch(JSONException exception){}
    }

    private void setText(){
        try{
            String text=props.getString("text");
            builder.setContentText(text);
        }
        catch(JSONException exception){}
    }

    private void setActions(){
        JSONArray actions=null;
        try{
            actions=props.getJSONArray("actions");
            int length=actions.length();
            if(length>0){
                final Intent intent=new Intent(activity,activity.getClass());
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
