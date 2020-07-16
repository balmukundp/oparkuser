package in.user.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.base.AppSession;
import in.user.base.CustomRequest;
import in.user.base.SingleShotLocationProvider;
import in.user.onlinebooking.BookingresponceModel;
import in.user.onlinebooking.Payment;
import in.user.registration.Otp;
import in.user.registration.Registration;


public class Login extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolBar;
    TextView textToolHeader, newAccount, forgotpassword;
    TextInputLayout textInputLayoutMobileNumber, textInputLayoutPassword;
    TextInputEditText textInputEditMobileNumber, textInputTextPassword;
    AppCompatButton appCompatButtonRegister;
    String mobileNumber, password, forgotPassword, userContactNo, nav_receipt = "", withoutLogin = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int cid, mcc, mnc, lac;
    String networkOperator, receipt;
    private String mLat = "";
    private String mLong = "";
    String latitude = "", longitude = "";
    LocationManager lm;
    Location location;
    private static final int REQUEST_CODE_GPS_SETTINGS = 101;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        innit();
        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            //  checkGpsAndGetLocation();
        }

    }

    public void innit() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Login");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

        textInputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditMobileNumber = (TextInputEditText) findViewById(R.id.textInputEditMobileNumber);
        textInputTextPassword = (TextInputEditText) findViewById(R.id.textInputTextPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        newAccount = (TextView) findViewById(R.id.newAccount);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        appCompatButtonRegister.setOnClickListener(this);
        newAccount.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);


    }

    public void getData() {
        mobileNumber = textInputEditMobileNumber.getText().toString();
        password = textInputTextPassword.getText().toString();
        forgotPassword = forgotpassword.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                getData();
                if (AppConstants.isInternetAvailable(Login.this)) {
                    login(mobileNumber, password);
                } else {
                    Toast.makeText(Login.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.newAccount:
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.forgotpassword:
                Intent forgot = new Intent(Login.this, ForgotPassword.class);
                startActivity(forgot);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    public void login(String mobileNumber, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loding");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_bar);
        //https://opark.in/O_par_aPi/userapp/live/index.php/v2/
        String urlData = AppConstants.BASEURL + "user/login?username=" + mobileNumber + "&password=" + password;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("JSON RETURN  " + response.toString());
                    int status = response.getInt("status");
                    String message = String.valueOf(response.get("message"));
                    String data = String.valueOf(response.get("data"));

                    if (status == 0) {
                        JSONObject jsonObject = response.getJSONObject("data");

                        LoginModel loginModel = new LoginModel();

                        loginModel.setAgentId(jsonObject.getString("userId"));
                        loginModel.setUserRole(jsonObject.getString("userRole"));
                        loginModel.setUserName(jsonObject.getString("userName"));
                        loginModel.setUserContactNo(jsonObject.getString("userContactNo"));
                        loginModel.setIsUserVerified(jsonObject.getString("isUserVerified"));
                        loginModel.setCity(jsonObject.getString("city"));
                        loginModel.setCityId(jsonObject.getString("cityId"));

                        String userContactNo = jsonObject.getString("userContactNo");


                        userContactNo = loginModel.getUserContactNo();

                        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("userId", loginModel.getAgentId());
                        editor.putString("userRole", loginModel.getUserRole());
                        editor.putString("userName", loginModel.getUserName());
                        //  editor.putString("isUserVerified", loginModel.getIsUserVerified());
                        editor.putString("userContactNo", userContactNo);
                        editor.putString("city", loginModel.getCity());
                        editor.putString("cityId", loginModel.getCityId());
                        editor.putString("Login_Key", "true");

                        AppConstants.setSetting(Login.this, AppConstants.Login_Key, true);
//                        editor.putString("mLat", mLat);
//                        editor.putString("mLong", mLong);
                        editor.apply();
                        editor.commit();


                        if (jsonObject.getString("isUserVerified").equals("false")) {
                            Intent intent = new Intent(Login.this, Otp.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }

                        if (jsonObject.getString("isUserVerified").equals("true")) {
                            bookslot();
                        }

                        progressDialog.dismiss();
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void bookslot() {
        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        String userContactNo = sharedPreferences.getString("userContactNo", "");


        SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);

        String pID = storeAllValues.getString("parkingId", "");
        String boardingTime = storeAllValues.getString("boardingTime", "");
        String vNo = storeAllValues.getString("vehicleNo", "");
        String vehicleType = storeAllValues.getString("vehicleType", "");

        String url = AppConstants.BASEURL + "booking/create";
        Map<String, String> parameterData = new HashMap<>();

        parameterData.put("mobileNo", userContactNo);
        parameterData.put("parkingId", pID);
        parameterData.put("boardingTime", boardingTime);
        parameterData.put("vehicleNo", vNo);
        parameterData.put("vehicleType", vehicleType);
        // parameterData.put(("withoutLogin"), "");

        if (AppConstants.isInternetAvailable(this)) {
            bookServices(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

    }

    public void bookServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectbookServices(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            pDialog.dismiss();
        }
    }

    private void processJsonObjectbookServices(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");

                    BookingresponceModel bookingresponceModel = new BookingresponceModel();
                    bookingresponceModel.setReceipt(checkINresponce.getString("receipt"));
                    String receipt = bookingresponceModel.getReceipt();

                    Intent intent = new Intent(Login.this, Payment.class);

                    intent.putExtra("receipt", receipt);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

                }


            } catch (NullPointerException e) {

                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                // pDialog.dismiss();
            }
        }
    }

    private void checkGpsAndGetLocation() {
        if (AppConstants.isGPSEnabled(Login.this)) {
            getLocation(Login.this);
        } else if (AppConstants.isNetworkProviderAvailable(Login.this)) {
            getLocation(Login.this);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_GPS_SETTINGS);
        }
    }

    public void getLocation(final Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        AppConstants.log("Location", "my location is " + location.latitude + "  " + location.longitude);
                        mLat = location.latitude + "";
                        mLong = location.longitude + "";

                        AppSession appSession = new AppSession(Login.this);
                        appSession.saveToSharedPref("lat", mLat);
                        appSession.saveToSharedPref("long", mLong);
                        // validateLogin();

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkGpsAndGetLocation();
                        break;
                    }
                }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GPS_SETTINGS && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                //  AppConstants.log(TAG, " Location providers: " + provider);
                //Start searching for location and update the location text when update available.
                getLocation(Login.this);
            } else {
                //Users did not switch on the GPS
                AppConstants.showToast(Login.this, "Enable gps to get better service.");
            }
        }
    }

}
