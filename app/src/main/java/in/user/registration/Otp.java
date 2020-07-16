package in.user.registration;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.CustomRequest;
import in.user.onlinebooking.BookingresponceModel;
import in.user.onlinebooking.Payment;


public class Otp extends AppCompatActivity {
    EditText pin1, pin2, pin3, pin4;
    TextView resend_otp;
    AppCompatButton appCompatButtonverifyOtp, appCompatButtonCancle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String pinOne, pinTwo, pinThree, pinFour, otp = "", userMobileNumber = "", UserId = "", userFullname = "", userEmail = "", city = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        registerReceiver(receiver, new IntentFilter(getString(R.string.message_receiver)));

        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSION = 124;
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECEIVE_SMS"}, REQUEST_CODE_ASK_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSION = 123;
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSION);
        }

        init();
    }

    @Override
    public void onResume() {
        registerReceiver(receiver, new IntentFilter(getString(R.string.message_receiver)));

        //  LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        registerReceiver(receiver, new IntentFilter(getString(R.string.message_receiver)));
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                if (intent != null && intent.getExtras() != null && intent.getStringExtra("message") != null) {
                    final String msg = intent.getStringExtra("message");
                    String otp = msg.replaceAll("\\D+", "");
                    pin1.setText(otp);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void init() {

        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        UserId = sharedPreferences.getString("userId", "");
        userMobileNumber = sharedPreferences.getString("userContactNo", "");

        appCompatButtonverifyOtp = (AppCompatButton) findViewById(R.id.appCompatButtonverifyOtp);
        appCompatButtonCancle = (AppCompatButton) findViewById(R.id.appCompatButtonCancle);

        pin1 = (EditText) findViewById(R.id.pin1);
        pin2 = (EditText) findViewById(R.id.pin2);
        pin3 = (EditText) findViewById(R.id.pin3);
        pin4 = (EditText) findViewById(R.id.pin4);
        resend_otp = (TextView) findViewById(R.id.resend_otp);

        appCompatButtonverifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String pin = pin1.getText().toString();
                String url = AppConstants.BASEURL + "user/verifyotp";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put("userId", UserId);
                parameterData.put("mobileNo", userMobileNumber);
                parameterData.put("otp", pin);


                if (AppConstants.isInternetAvailable(Otp.this)) {
                    if (!pin.isEmpty()) {
                        otpverify(url, parameterData);
                    } else {
                        Toast.makeText(Otp.this, "Enter OTP ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Otp.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }


            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = AppConstants.BASEURL + "user/resendotp";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("userId"), UserId);
                parameterData.put(("mobileNo"), userMobileNumber);

                if (AppConstants.isInternetAvailable(Otp.this)) {
                    resendotp(url, parameterData);
                } else {
                    Toast.makeText(Otp.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }
            }
        });


    }//624490


    public void otpverify(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Otp.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectotpverify(jsonObject);
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

    private void processJsonObjectotpverify(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");

                    String userId = checkINresponce.getString("userId");
                    String userMobile = checkINresponce.getString("userMobile");
                    boolean isUserVerified = checkINresponce.getBoolean("isUserVerified");

                    if (isUserVerified == true) {

                        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("userId", UserId);
                        editor.putString("userContactNo", userMobileNumber);
                        editor.putString("RegisterKey", "true");
                        editor.putString("userName", userFullname);
                        editor.putString("userEmail", userEmail);
                        editor.putString("city", city);
                        editor.apply();
                        editor.commit();
                        Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();
                        bookslot();


                    } else {
                        Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();
                    }


                } else {

                    Toast.makeText(Otp.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

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

    public void bookslot() {

        SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);
        String pID = storeAllValues.getString("parkingId", "");
        String boardingTime = storeAllValues.getString("boardingTime", "");
        String vNo = storeAllValues.getString("vehicleNo", "");
        String vehicleType = storeAllValues.getString("vehicleType", "");

        String url = AppConstants.BASEURL + "booking/create";
        Map<String, String> parameterData = new HashMap<>();

        parameterData.put(("mobileNo"), userMobileNumber);
        parameterData.put(("parkingId"), pID);
        parameterData.put(("boardingTime"), boardingTime);
        parameterData.put(("vehicleNo"), vNo);
        parameterData.put(("vehicleType"), vehicleType);


        if (AppConstants.isInternetAvailable(this)) {
            bookServices(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

    }

    public void bookServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Otp.this);
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

                    Intent intent = new Intent(Otp.this, Payment.class);

                    intent.putExtra("receipt", receipt);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    finish();
                    Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();

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

    public void resendotp(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Otp.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectresendotp(jsonObject);
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

    private void processJsonObjectresendotp(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {
                    Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Otp.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

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

}
