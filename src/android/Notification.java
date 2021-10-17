package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import com.ahmedayachi.notifier.Action; 
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.Intent;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.graphics.drawable.IconCompat;
import android.graphics.Bitmap;


public class Notification{

    private JSONObject props=null;
    private final NotificationCompat.Builder builder=new NotificationCompat.Builder(Notifier.context,Notifier.channelId);
    private static final NotificationManagerCompat notificationManager=NotificationManagerCompat.from(Notifier.context);
    private AppCompatActivity activity=null;

    public Notification(AppCompatActivity activity,JSONObject props) throws JSONException{
        this.props=props;
        this.activity=activity;
        int id=props.getInt("id");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.setSmallIcon();
        this.setLargeIcon();
        this.setTitle();
        this.setText();
        final Intent intent=new Intent(activity,activity.getClass());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_NO_CREATE);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);
        this.setActions();
        

        notificationManager.notify(id,builder.build());
    }

    private void setSmallIcon(){
        boolean set=false;
        try{
            String path=props.getString("icon");
            Bitmap bitmap=Notifier.getBitmapIcon(path);
            if(bitmap==null){
                builder.setSmallIcon(Notifier.appinfo.icon);
            }
            else{
                builder.setSmallIcon(IconCompat.createWithBitmap(bitmap));
            }
        }
        catch(JSONException exception){
            builder.setSmallIcon(Notifier.appinfo.icon);
        }
    }

    private void setLargeIcon(){
        try{
            String path=props.getString("largeIcon");
            if(path.equals("appIcon")){
                final Drawable drawable=Notifier.appinfo.loadIcon(Notifier.context.getPackageManager());
                final Bitmap bitMapIcon=((BitmapDrawable)drawable).getBitmap();
                builder.setLargeIcon(bitMapIcon);
            }
            else{
                Bitmap bitmap=Notifier.getBitmapIcon(path);
                if(bitmap!=null){
                    builder.setLargeIcon(bitmap);
                }
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
                for(int i=0;i<length;i++){
                    final JSONObject options=actions.getJSONObject(i);
                    final Action action=new Action(options);
                    action.addTo(builder);
                }
            }
        }
        catch(JSONException exception){}
    }
}
