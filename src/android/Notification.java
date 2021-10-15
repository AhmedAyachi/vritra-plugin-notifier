package com.ahmedayachi.notifier;

import org.json.JSONObject;
import org.json.JSONException;
import com.ahmedayachi.notifier.Notifier;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.appcompat.app.AppCompatActivity;


public class Notification{

    
    public Notification(AppCompatActivity activity,JSONObject props) throws JSONException{
        id=props.getInt("id");
        NotificationCompat.Builder builder=new NotificationCompat.Builder(activity,Notifier.channelId);
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
}
