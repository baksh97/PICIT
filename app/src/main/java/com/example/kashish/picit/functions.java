package com.example.kashish.picit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class functions {

    static Drawable bitmap2Drawable(Bitmap b, Context context){
        Drawable d = new BitmapDrawable(context.getResources(), b);
        return d;
    }

}
