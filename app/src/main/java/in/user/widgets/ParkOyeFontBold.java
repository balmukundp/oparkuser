package in.user.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;


@SuppressWarnings("DefaultFileTemplate")
public class ParkOyeFontBold {
    @SuppressLint("StaticFieldLeak")
    private static ParkOyeFontBold stribeFont;
    private static Typeface sLightTypeFace;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static final String PATH_DEFAULT_FONT = "font/Raleway-Bold.ttf";
    private ParkOyeFontBold()
    {
        sLightTypeFace = Typeface.createFromAsset(sContext.getAssets(),PATH_DEFAULT_FONT);

    }
    public static ParkOyeFontBold getTribeFontInstance(Context context)
    {
        sContext= context;
        if(stribeFont == null)
        {
            stribeFont = new ParkOyeFontBold();
        }
        return stribeFont;
    }
    public Typeface getTribeRegularTypeFace()
    {
        return sLightTypeFace;
    }

}
