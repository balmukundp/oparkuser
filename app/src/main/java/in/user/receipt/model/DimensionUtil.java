package in.user.receipt.model;

import android.content.Context;
import android.util.DisplayMetrics;


public class DimensionUtil {
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int getScreenHeightWithoutStatusBarAndToolbar(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        //return metrics.heightPixels;
        int result = metrics.heightPixels;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = result - context.getResources().getDimensionPixelSize(resourceId);
        }
        return result - dpToPx(56, context);
    }

    public static int getScreenHeightWithoutStatusBar(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        //return metrics.heightPixels;
        int result = metrics.heightPixels;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = result - context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float spToPx(int sp, Context context) {
        return sp * context.getResources().getDisplayMetrics().scaledDensity;
    }

}
