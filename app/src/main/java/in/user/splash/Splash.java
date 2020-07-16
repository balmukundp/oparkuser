package in.user.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import in.user.MainActivity;
import in.user.R;


public class Splash extends AppCompatActivity {
    Handler handler = null;
    SplashThread splashThread;
    SharedPreferences sharedPreferences;
    public static String TAG = "Splash";
    private long DELAY_MILLIS = 5000;
    String agentId, userRole, userName, userContactNo, isUserVerified, vendorName, city, cityId,Login_Key,RegisterKey,userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);

        agentId = sharedPreferences.getString("userId", "");
        userRole = sharedPreferences.getString("userRole", "");
        userName = sharedPreferences.getString("userName", "");
        userContactNo = sharedPreferences.getString("userContactNo", "");
      //  isUserVerified = sharedPreferences.getString("isUserVerified", "");
        city = sharedPreferences.getString("city", "");
        cityId = sharedPreferences.getString("cityId", "");
        Login_Key = sharedPreferences.getString("Login_Key", "");
        userEmail = sharedPreferences.getString("userEmail", "");
        RegisterKey = sharedPreferences.getString("RegisterKey", "");
       // AppConstants.getSetting(Splash.this, AppConstants.Login_Key, false);

        initviews();


    }

    @SuppressLint("NewApi")
    public void initviews() {

        if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            //  checkGpsAndGetLocation();
        }
        handler = new Handler();
        splashThread = new SplashThread();
        handler.postDelayed(splashThread, DELAY_MILLIS);
    }

    public class SplashThread implements Runnable {
        @Override
        public void run() {

            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

//            if (agentId.equals("") && userRole.equals("") && userName.equals("") && userContactNo.equals("") && isUserVerified.equals("") &&
//                    city.equals("") && cityId.equals("")) {
//                Intent intent = new Intent(Splash.this, Login.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            } else {
//
//                if (isUserVerified.equals(false)){
//                    Intent intent = new Intent(Splash.this, Otp.class);
////                    intent.putExtra("otp", OTP);
////                    intent.putExtra("UserId", UserId);
////                    intent.putExtra("userMobileNumber", userMobileNumber);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                }
//                Intent intent = new Intent(Splash.this, MainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//
//            }
        }
    }
}
