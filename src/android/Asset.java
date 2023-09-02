package com.corella.notifier;

import com.corella.notifier.Notifier;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.IconCompat;


public class Asset {

    static final AssetManager assetManager=Notifier.context.getAssets();
    private Bitmap bitmap=null;

    public Asset(String url){
        try{
            url=url.replaceFirst("file:///android_asset/","");
            InputStream stream=assetManager.open(url);
            bitmap=BitmapFactory.decodeStream(stream);
        }
        catch(IOException exception){}
    }

    public Bitmap toBitmap(){
        return this.bitmap;
    }
    public IconCompat toIconCompat(){
        return IconCompat.createWithBitmap(this.bitmap);
    }
}
