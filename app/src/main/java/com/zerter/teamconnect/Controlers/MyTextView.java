package com.zerter.teamconnect.Controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;


import com.zerter.teamconnect.R;

import java.util.Hashtable;

/**
 * Rozszerzenie textView
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextView";
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
//        setFontOswaldRegular();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
//       setFontOswaldRegular();
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String customFont = a.getString(R.styleable.MyTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typefaces.get(ctx,asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }
//    public  TextView setFontOswaldRegular(){
//        TextView textView = new TextView(context);
//        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Oswald-Regular.ttf");
//        textView.setTypeface(type);
//        return textView;
//    }
//    public  TextView setFontOswaldBold(){
//        TextView textView = new TextView(context);
//        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Oswald-Bold.ttf");
//        textView.setTypeface(type);
//        return textView;
//    }

    /**
     * Created by ampmedia on 2017-08-22.
     */

    public static class Typefaces {
        private static final String TAG = "Typefaces";

        private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

        public static Typeface get(Context c, String assetPath) {
            synchronized (cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        Typeface t = Typeface.createFromAsset(c.getAssets(),
                                assetPath);
                        cache.put(assetPath, t);
                    } catch (Exception e) {
                        Log.e(TAG, "Could not get typeface '" + assetPath
                                + "' because " + e.getMessage());
                        return null;
                    }
                }
                return cache.get(assetPath);
            }
        }
    }
}
