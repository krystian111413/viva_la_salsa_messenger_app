package com.zerter.teamconnect.Controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.zerter.teamconnect.R;
import java.util.Hashtable;

/**
 * Rozszerzenie Edittext
 */

public class MyEditText extends android.support.v7.widget.AppCompatEditText {
    private  final String TAG = getClass().getName();
    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
//        setFontOswaldRegular();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
//       setFontOswaldRegular();
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        String customFont = a.getString(R.styleable.MyEditText_customFont);
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
