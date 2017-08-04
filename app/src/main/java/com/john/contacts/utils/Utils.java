package com.john.contacts.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by johns on 7/31/2017.
 */

public class Utils
{
    public void setText(TextView et, String data) {
        if (TextUtils.isEmpty(data) || data.equals("0"))
            et.setText("None");
        else
            et.setText(data);
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
