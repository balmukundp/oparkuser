package in.user.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;


@SuppressWarnings("DefaultFileTemplate")
public class ParkOyeFont {
    @SuppressLint("StaticFieldLeak")
    private static ParkOyeFont stribeFont;
    private static Typeface sLightTypeFace;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    // private static final String PATH_DEFAULT_FONT = "font/Helvetica_Neue.ttf";
    private static final String PATH_DEFAULT_FONT = "font/Raleway-Regular.ttf";
    private ParkOyeFont()
    {
        sLightTypeFace = Typeface.createFromAsset(sContext.getAssets(),PATH_DEFAULT_FONT);

    }
    public static ParkOyeFont getTribeFontInstance(Context context)
    {
        sContext= context;
        if(stribeFont == null)
        {
            stribeFont = new ParkOyeFont();
        }
        return stribeFont;
    }
    public Typeface getTribeRegularTypeFace()
    {
        return sLightTypeFace;
    }

}
