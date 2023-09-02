package com.corella.notifier;

import org.apache.cordova.*;
import com.corella.notifier.Notification;
import com.corella.notifier.Asset;
import com.corella.notifier.ToastView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Base64;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Notifier extends CordovaPlugin {

    static Context context;
    static ApplicationInfo appinfo;
    static final String channelId="NotifierChannel";

    @Override
    public void initialize(CordovaInterface cordova,CordovaWebView webView){
        Notifier.context=cordova.getContext();
        Notifier.appinfo=Notifier.context.getApplicationInfo();
        //Notifier.packageName=Notifier.context.getPackageName();
        this.createNotificationChannel();
    }

    @Override
    public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException{
        if(action.equals("notify")) {
            JSONObject props=args.optJSONObject(0);
            this.notify(props,callbackContext);
            return true;
        }
        else if(action.equals("dismiss")){
            final int notificationId=args.optInt(0);
            Notifier.dismiss(notificationId);
        }
        else if(action.equals("toast")){
            JSONObject props=args.optJSONObject(0);
            this.toast(props,callbackContext);
        }
        return false;
    }

    private void notify(JSONObject props,CallbackContext callbackContext){
        final AppCompatActivity activity=cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable(){
            public void run(){
                final Notification notification=new Notification(activity,props,callbackContext);
            }
        });
    }

    static protected void dismiss(int notificationId){
        Notification.manager.cancel(notificationId);
        Notification.callbacks.remove(Integer.toString(notificationId));
    }

    private void toast(JSONObject props,CallbackContext callbackContext){
        final ToastView toastview=new ToastView(props);
        toastview.show();
    }

    private void createNotificationChannel(){
        final AppCompatActivity activity=cordova.getActivity();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name="NotifierChannel";
            String description="Notifier channel";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(channelId,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    static protected Bitmap getBitmapIcon(String path){
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

    static protected int getColor(String name){
        switch(name){
            case "brown": return  Color.parseColor("#964B00");
            case "orange": return  Color.parseColor("#FFA500");
            case "transparent": return Color.TRANSPARENT;
            default: return Color.parseColor(name);
        }
    }
}