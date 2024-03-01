package com.vritra.notifier;

import com.vritra.notifier.Notifier;
import org.json.JSONObject;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.util.DisplayMetrics;
import android.graphics.Outline;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;


class ToastView {
    
    final DisplayMetrics displayMetrics=Notifier.resources.getDisplayMetrics();
    private JSONObject props=null;
    private JSONObject style=null;
    private Toast toast=null;
    private TextView view=null;
    private View parentView=null;

    public ToastView(AppCompatActivity activity,JSONObject props){
        this.props=props;
        this.style=props.optJSONObject("style");
        final String text=props.optString("text","");
        if(style==null){
            toast=Toast.makeText(activity,text,this.getLasting());
        }
        else{
            this.parentView=activity.getWindow().getDecorView().getRootView();
            this.view=new TextView(activity);
            view.setText(text);
            this.setFontSize();
            this.setTextColor();
            this.setBackgroundColor();
            this.setOpacity();
            this.setLayout();
            this.setBorderRadius();
        }
    }

    private int getLasting(){
        final String lasting=props.optString("lasting","short");
        return lasting.equals("long")?Toast.LENGTH_LONG:Toast.LENGTH_SHORT;
    };

    private void setFontSize(){
        final int screenWidth=displayMetrics.widthPixels;
        view.setTextSize((int)(0.013*screenWidth));
    }

    private void setTextColor(){
        final String textColor=style.optString("color","black");
        view.setTextColor(Notifier.getColor(textColor));
    }

    private void setBackgroundColor(){
        final String backgroundColor=style.optString("backgroundColor","#ebebeb");
        view.setBackgroundColor(Notifier.getColor(backgroundColor));
    }

    private void setOpacity(){
        final double opacity=style.optDouble("opacity",0.9);
        view.setAlpha((float)opacity);
    }

    private void setBorderRadius(){
        final double borderRadius=style.optDouble("borderRadius",60);
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider(){
            @Override
            public void getOutline(View view,Outline outline){
                outline.setRoundRect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom(),(float)borderRadius);
            }
        });
    }

    private void setLayout(){
        final int screenWidth=displayMetrics.widthPixels;
        view.setMaxWidth((int)(0.875*screenWidth));
        final ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setGravity(Gravity.LEFT);
        view.setLayoutParams(layoutParams);
        final int paddingHorizontal=(int)(0.06*screenWidth);
        final int paddingVertical=(int)(0.04*screenWidth);
        view.setPadding(paddingHorizontal,paddingVertical,paddingHorizontal,paddingVertical);
    }

    private void setPosition(){
        final int screenWidth=displayMetrics.widthPixels;
        final int screenHeight=displayMetrics.heightPixels;
        final String verticalAlign=style.optString("verticalAlign","bottom");
        final double offset=0.0825*screenHeight;
        final ViewTreeObserver observer=view.getViewTreeObserver(); 
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                final int width=view.getWidth();
                final int height=view.getHeight();
                view.setX((int)((screenWidth-width)/2));
                switch(verticalAlign){
                    case "top": view.setY((int)offset);break;
                    case "middle":view.setY((int)((screenHeight-height)/2));break;
                    default: view.setY((int)(screenHeight-offset-height));break;
                }
                observer.removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void animate(){
        final int fadeDuration=250;
        final AlphaAnimation fadeInAnim=new AlphaAnimation(0.0f,1.0f);
        fadeInAnim.setDuration(fadeDuration);
        view.startAnimation(fadeInAnim);
        final int lasting=this.getLasting();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                final AlphaAnimation fadeOutAnim=new AlphaAnimation(1.0f,0.0f);
                fadeOutAnim.setDuration(fadeDuration);
                fadeOutAnim.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation animation){}
                    @Override
                    public void onAnimationEnd(Animation animation){
                        ((ViewGroup)parentView).removeView(view);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation){}
                });
                view.startAnimation(fadeOutAnim);
            }
        },(lasting==Toast.LENGTH_LONG?3500:2000)-fadeDuration);
    }

    public void show(){
        if(this.view!=null){
            ((ViewGroup)parentView).addView(this.view);
            view.bringToFront();
            parentView.requestLayout();
            this.setPosition();
            this.animate();
        }
        else if(toast!=null) toast.show();
    }
}
