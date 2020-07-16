package in.user.base;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by ABSDevelopers.
 */

public class AppSession {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private static final String SHARED = "Tribe";

    public AppSession(Context context) {
        try {
            sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
            editor = sharedPref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveToSharedPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }




    public void setUserType(boolean isCorporate) {
        editor.putBoolean("UserType", isCorporate);
        editor.commit();
    }

    public boolean isUserTypeCorporate() {
        try {
            return sharedPref.getBoolean("UserType", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getFromSharedPref(String key) {
        try {
            return sharedPref.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void resetUser() {
        editor.putString("User", "");
        editor.commit();
    }






}
