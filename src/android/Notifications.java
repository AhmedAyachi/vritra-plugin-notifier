package com.ahmedayachi.notifications;

import android.app.Activity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.ahmedayachi.alertdetails.AlertDetails;
import android.R;
import android.os.Build;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.content.pm.ApplicationInfo;


public class Notifications extends CordovaPlugin{

    static String channelId="channelId";

    @Override
    public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException{
        if(action.equals("create")) {
            JSONObject options=args.getJSONObject(0);
            this.create(options,callbackContext);
            return true;
        }
        return false;
    }

    private void create(JSONObject options,CallbackContext callbackContext) throws JSONException{
        final Activity activity=cordova.getActivity();
        final Context context=cordova.getContext();
        final ApplicationInfo appinfo=context.getApplicationInfo();
        this.createNotificationChannel();
        this.cordova.getThreadPool().execute(new Runnable(){
            NotificationCompat.Builder builder;
            Integer id;
            public void run(){
                try{
                    id=options.getInt("id");
                    builder=new NotificationCompat.Builder(activity,channelId);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    this.setSmallIcon();
                    this.setLargeIcon();
                    this.setTitle();
                    this.setText();

                    final Intent intent=new Intent(context,CordovaActivity.class);//(activity,AlertDetails.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,0);
                    builder.setContentIntent(pendingIntent).setAutoCancel(true).
                    addAction(R.drawable.edit_text,"yes",pendingIntent).
                    addAction(R.drawable.edit_text,"no",pendingIntent);
                    
                    NotificationManagerCompat notificationManager=NotificationManagerCompat.from(activity);
                    notificationManager.notify(id.intValue(),builder.build());
                    callbackContext.success();
                }
                catch(JSONException exception){}
            }

            private void setSmallIcon(){
                Integer icon;
                try{
                    icon=options.getInt("smallIcon");
                }
                catch(JSONException exception){
                    icon=null;
                    builder.setSmallIcon(appinfo.icon);
                }
                if(icon!=null){
                    builder.setSmallIcon(icon.intValue());
                }
            }

            private void setLargeIcon(){
                Boolean withIcon=false;
                try{
                    withIcon=options.getBoolean("withLargeIcon");
                }
                catch(JSONException exception){}
                if(withIcon){
                    Integer icon;
                    try{
                        icon=options.getInt("largeIcon");
                    }
                    catch(JSONException exception){
                        final Drawable drawable=appinfo.loadIcon(context.getPackageManager());
                        final Bitmap bitMapIcon=((BitmapDrawable)drawable).getBitmap();
                        icon=null;
                        builder.setLargeIcon(bitMapIcon);
                    }
                    /*if(icon!=null){
                        builder.setLargeIcon(icon.intValue());
                    }*/
                }
            }

            private void setTitle(){
                String title=null;
                try{
                    title=options.getString("title");
                }
                catch(JSONException exception){}
                if(title!=null){
                    builder.setContentTitle(title);
                }
            }

            private void setText(){
                String text=null;
                try{
                    text=options.getString("text");
                }
                catch(JSONException exception){}
                if(text!=null){
                    builder.setContentText(text);
                }
            }
        });
    }

    private void createNotificationChannel(){
        final Activity activity=cordova.getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name="name";
            String description="description";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(channelId,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
}