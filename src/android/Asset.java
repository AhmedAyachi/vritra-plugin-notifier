package com.ahmedayachi.notifier;

import com.ahmedayachi.notifier.Notifier;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.IconCompat;


public class Asset{

    static final AssetManager assetManager=Notifier.context.getAssets();
    private IconCompat icon=null;

    public Asset(String url){
        try{
            url=url.replaceFirst("file:///android_asset/","");
            InputStream stream=assetManager.open(url);
            final Bitmap bitmap=BitmapFactory.decodeStream(stream);
            icon=IconCompat.createWithBitmap(bitmap);
        }
        catch(IOException exception){}
    }

    public IconCompat toIconCompat(){
        return this.icon;
    }
}
