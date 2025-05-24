package com.vritra.notifier;


import java.util.Random;
import com.vritra.notifier.Notifier;
import com.vritra.notifier.Action; 
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CallbackContext;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.HtmlCompat;
import android.content.Intent;
import android.os.Build;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Color;


public class Notification {

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

        this.setTitle();
        this.setBody();
        this.setSmallIcon();
        this.setLargeIcon();
        this.setActions();
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setOngoing(props.optBoolean("sticky",false));
        this.setSubText();

        final Intent intent=new Intent(Notifier.context,activity.getClass());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        final PendingIntent pendingIntent=PendingIntent.getActivity(
            Notifier.context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT |
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        builder.setContentIntent(pendingIntent).setAutoCancel(once);
        try{
            callbacks.put(Integer.toString(id),callbackcontext);
        }
        catch(JSONException exception){}
        
        manager.notify(id,builder.build());
    }

    private void setTitle(){
        final String title=props.optString("title","");
        final String color=props.optString("color",null);
        if(color==null){
            builder.setContentTitle(title);
        }
        else{
            builder.setContentTitle(HtmlCompat.fromHtml("<font color='"+color+"'>"+title+"</font>",HtmlCompat.FROM_HTML_MODE_LEGACY));
            builder.setColor(Color.parseColor(color));
            builder.setColorized(true);
        }
    }

    private void setBody(){
        final String body=props.optString("body","");
        final boolean fullbody=props.optBoolean("fullbody",false);
        if(fullbody){
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        }
        else{
            builder.setContentText(body);
        }
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

    private void setSubText(){
        final String subtext=props.optString("subtext",null);
        if(subtext!=null){
            builder.setSubText(subtext);
        }
    }
}
