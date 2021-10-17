package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import com.ahmedayachi.notifier.Asset;
import org.json.JSONObject;
import org.json.JSONArray;
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
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.graphics.Bitmap;


public class Notification{

    private JSONObject props=null;
    private static final NotificationCompat.Builder builder=new NotificationCompat.Builder(Notifier.context,Notifier.channelId);
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
            Bitmap bitmap=this.getBitmapIcon(path);
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
                Bitmap bitmap=this.getBitmapIcon(path);
                if(bitmap!=null){
                    builder.setLargeIcon(bitmap);
                }
            }
        }
        catch(JSONException exception){}
    }

    private Bitmap getBitmapIcon(String path){
        Bitmap bitmap=null;
        if(path.startsWith("data:")){
            path=path.substring(path.indexOf(",")+1);
            byte[] decoded=Base64.decode(path,Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(decoded,0,decoded.length);
        }
        else if(path.startsWith("file:///android_asset")){
            bitmap=new Asset(path).toBitmap();
        }

        return bitmap;
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
