package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;

import java.util.ArrayList;
import java.util.Random;
import com.ahmedayachi.notifier.ActionListener;
import org.json.JSONObject;
import org.json.JSONArray;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action.Builder;
import android.graphics.Bitmap;
import org.json.JSONException;
import androidx.core.text.HtmlCompat;
import android.text.Spanned;
import android.content.Intent;
import android.app.PendingIntent;
import androidx.core.app.RemoteInput;


public class Action{

    private JSONObject options=null;
    private String id=null;
    private String type="button";
    private String label=null;
    private Spanned span=null;
    private IconCompat icon=null;

    public Action(JSONObject options) throws JSONException{
        this.options=options;
        this.setId();
        this.setType();
        this.setLabel();
        this.setIcon();
    }

    private void setId(){
        try{
            id=options.getString("value");
        }
        catch(JSONException exception){}
    }

    private void setType(){
        try{
            type=options.getString("type");
        }
        catch(JSONException exception){}
    }

    private void setLabel(){
        try{
            label=options.getString("label");
            try{
                final String color=options.getString("color");
                span=HtmlCompat.fromHtml("<font color='"+color+"'>"+label+"</font>",HtmlCompat.FROM_HTML_MODE_LEGACY);
            }
            catch(JSONException exception){}
        }
        catch(JSONException exception){}
    }

    private void setIcon(){
        try{
            String iconPath=options.getString("icon");
            final Bitmap bitmap=Notifier.getBitmapIcon(iconPath);
            if(bitmap!=null){
                icon=IconCompat.createWithBitmap(bitmap);
            }
        }
        catch(JSONException exception){}
    }

    public String getId(){
        return this.id;
    }
    public void addTo(NotificationCompat.Builder notibuilder) throws JSONException{
        final Intent intent=new Intent(Notifier.context,ActionListener.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id",id);
        intent.putExtra("notificationId",options.getInt("notificationId"));
        intent.putExtra("type",type);
        final PendingIntent pendingIntent=PendingIntent.getBroadcast(Notifier.context,new Random().nextInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        
        final Builder builder=new Builder(icon,span!=null?span:label,pendingIntent);
        if(!type.equals("button")){
            try{
                final RemoteInput.Builder inputbuilder=new RemoteInput.Builder(id);
                if(type.equals("input")){
                    String placeholder=options.getString("placeholder");
                    inputbuilder.setLabel(placeholder);
                }
                else if(type.equals("select")){
                    JSONArray ops=options.getJSONArray("options");
                    final int length=ops.length();
                    ArrayList<String> choices=new ArrayList<String>(length);
                    for(int i=0;i<length;i++){
                        choices.add(ops.getString(i));
                    }
                    inputbuilder.setChoices(choices.toArray(new CharSequence[choices.size()]));
                    inputbuilder.setLabel("Choose from the options above");
                    builder.setAllowGeneratedReplies(true);
                }
                builder.addRemoteInput(inputbuilder.build());
            }
            catch(JSONException exception){}
        }
        notibuilder.addAction(builder.build());
    }
}
