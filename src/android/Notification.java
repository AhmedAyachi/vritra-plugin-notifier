package com.ahmedayachi.notifier;


import java.util.Random;
import com.ahmedayachi.notifier.Notifier;
import com.ahmedayachi.notifier.Action; 
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CallbackContext;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import android.content.Intent;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Color;


public class Notification{

    private JSONObject props=null;
    protected static final JSONObject callbacks=new JSONObject();
    private final NotificationCompat.Builder builder=new NotificationCompat.Builder(Notifier.context,Notifier.channelId);
    protected static final NotificationManagerCompat manager=NotificationManagerCompat.from(Notifier.context);
    protected int id;
    protected Boolean once;

    public Notification(AppCompatActivity activity,JSONObject props,CallbackContext callbackcontext){
        this.props=props;
        id=props.optInt("id",new Random().nextInt(1000));
        once=props.optBoolean("once",true);
        
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.setSmallIcon();
        this.setLargeIcon();
        this.setTitle();
        this.setBody();
        //this.setBackgroundColor();
        this.setActions();

        final Intent intent=new Intent(Notifier.context,activity.getClass());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        final PendingIntent pendingIntent=PendingIntent.getActivity(Notifier.context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent).setAutoCancel(once);
        try{
            callbacks.put(Integer.toString(id),callbackcontext);
        }
        catch(JSONException exception){}
        
        manager.notify(id,builder.build());
    }

    private void setSmallIcon(){
        String icon=props.optString("icon");
        if(icon==null){
            builder.setSmallIcon(Notifier.appinfo.icon);
        }
        else{
            final Bitmap bitmap=Notifier.getBitmapIcon(icon);
            if(bitmap==null){
                builder.setSmallIcon(Notifier.appinfo.icon);
            }
            else{
                builder.setSmallIcon(IconCompat.createWithBitmap(bitmap));
            }
        }
            
    }

    /* private void setBackgroundColor(){
        try{
            final String backgroudColor=props.getString("backgroudColor");
            builder.setColor(Color.parseColor(backgroudColor));
            builder.setColorized(true);
            //builder.setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        catch(JSONException exception){}
    } */
    private void setLargeIcon(){
        String icon=props.optString("largeIcon");
        if(icon.equals("appIcon")){
            final Drawable drawable=Notifier.appinfo.loadIcon(Notifier.context.getPackageManager());
            final Bitmap bitMapIcon=((BitmapDrawable)drawable).getBitmap();
            builder.setLargeIcon(bitMapIcon);
        }
        else{
            final Bitmap bitmap=Notifier.getBitmapIcon(icon);
            if(bitmap!=null){
                builder.setLargeIcon(bitmap);
            }
        }
    }

    private void setTitle(){
        final String title=props.optString("title","");
        builder.setContentTitle(title);
    }

    private void setBody(){
        final String body=props.optString("body","");
        builder.setContentText(body);
    }

    private void setActions(){
        final JSONArray actions=props.optJSONArray("actions");
        if(actions!=null){
            final int length=actions.length();
            if(length>0){
                for(int i=0;i<length;i++){
                    final JSONObject options=actions.optJSONObject(i);
                    if(options!=null){
                        try{
                            options.put("notificationId",id);
                            options.put("once",once);
                            final Action action=new Action(options);
                            action.addTo(builder);
                        }
                        catch(JSONException exception){}
                    }
                }
            }
        }
    }
}
