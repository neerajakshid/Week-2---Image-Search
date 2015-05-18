package com.neeraja.imagesearch.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class DimmensionsHelper {

    // Converting dp to pixel
    public static float convertDpToPixel(final Context context, final float dp){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    // Converting px to dp
    public static int convertPixelsToDp(final Context context, final float px ){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px/displayMetrics.density)+0.5f);


    }
}

