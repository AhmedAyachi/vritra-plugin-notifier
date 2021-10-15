package com.ahmedayachi.notifier;

import android.app.Activity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import androidx.core.app.TaskStackBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.ahmedayachi.notifier.TapHandler;
import android.R;
import android.os.Build;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.content.pm.ApplicationInfo;


public class Notifier extends CordovaPlugin{

    static final String channelId="channelId";
    @Override
    public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException{
        if(action.equals("notify")) {
            JSONObject options=args.getJSONObject(0);
            this.notify(options,callbackContext);
            return true;
        }
        return false;
    }

    private void notify(JSONObject options,CallbackContext callbackContext) throws JSONException{
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

                    final Intent openintent=new Intent(context,activity.getClass());
                    openintent.addCategory(Intent.CATEGORY_LAUNCHER);
                    openintent.setAction(Intent.ACTION_MAIN);
                    /*final Intent notiintent=new Intent(activity,TapHandler.class);
                    notiintent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    final Intent[] intents={openintent,notiintent};*/
                    final PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,openintent,PendingIntent.FLAG_CANCEL_CURRENT);
                    builder.setContentIntent(pendingIntent).setAutoCancel(true);
                    this.setActions();
                    
                    NotificationManagerCompat notificationManager=NotificationManagerCompat.from(activity);
                    notificationManager.notify(id.intValue(),builder.build());
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

            private void setActions(){
                JSONArray actions=null;
                try{
                    actions=options.getJSONArray("actions");
                    int length=actions.length();
                    if(length>0){
                        final Intent intent=new Intent(context,TapHandler.class);
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