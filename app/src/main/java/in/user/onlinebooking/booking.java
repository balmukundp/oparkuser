package in.user.onlinebooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.base.CustomRequest;
import in.user.login.Login;
import in.user.login.LoginModel;
import in.user.registration.Otp;


public class booking extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolBar;
    TextView textToolHeader;
    private RadioButton twoWheeler, fourWheeler;
    private RadioGroup radio;
    private String vehicleType = "", availableSlots, userContactNo, availableSlotstwo, availableSlotsfour, av, pID, Login_Key, userId = "", text_1 = "", text_2 = "", parkingId = "";
    Button btnCheckIn;
    TextView etMobileNo, etTime, tvTotalSpots, etDate, tvAvailableSpots, text1, text2, two_wheeler, four_wheeler;
    TextInputLayout textInputLayoutMobileNumber;
    TextInputLayout textInputLayoutVehicleNumber;
    LinearLayout star;
    TextInputEditText textInputEditMobileNumber;
    TextInputEditText textInputEditVehicleNumber;

    DatePickerDialog datePickerDialog;
    Calendar myCalendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    SharedPreferences sharedPreferences;
    String[] availableSlots1 = new String[3];
    String[] availableSlotsW = new String[3];
    boolean login, register;

    Calendar newCalendar, newDate;
    TimePickerDialog mEventTimePickerDialog;
    Date mSelectDate;
    SimpleDateFormat sdf;
    SimpleDateFormat mdformat;
    LinearLayout layoutbooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        findViews();

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio.check(checkedId);

                if (checkedId == R.id.twowheeler) {
                    twoWheeler.setChecked(true);
                    vehicleType = "2Wheeler ";

                } else {
                    fourWheeler.setChecked(true);
                    vehicleType = "4Wheeler ";
                }
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(booking.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                Date newDate = myCalendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        pID = sharedPreferences.getString("pID", "");
        parkingRate(pID);
    }

    private void findViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Booking");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        star = (LinearLayout) findViewById(R.id.star);
        textInputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayoutVehicleNumber = (TextInputLayout) findViewById(R.id.textInputLayoutVehicleNumber);

        textInputEditMobileNumber = (TextInputEditText) findViewById(R.id.textInputEditMobileNumber);
        textInputEditVehicleNumber = (TextInputEditText) findViewById(R.id.textInputEditVehicleNumber);
        radio = (RadioGroup) findViewById(R.id.radio);
        twoWheeler = (RadioButton) findViewById(R.id.twowheeler);
        fourWheeler = (RadioButton) findViewById(R.id.fourwheeler);
        etDate = (TextView) findViewById(R.id.etDate);
        etTime = (TextView) findViewById(R.id.etTime);
        etMobileNo = (TextView) findViewById(R.id.etMobileNo);
        tvAvailableSpots = (TextView) findViewById(R.id.tvAvailableSpots);
        two_wheeler = (TextView) findViewById(R.id.two_wheeler);
        four_wheeler = (TextView) findViewById(R.id.four_wheeler);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        tvTotalSpots = (TextView) findViewById(R.id.tvTotalSpots);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        layoutbooking = (LinearLayout) findViewById(R.id.layoutbooking);

        etTime.setHint(Html.fromHtml(getString(R.string.enter_Time)));
        etTime.requestFocus();
        Calendar calendar = Calendar.getInstance();
        mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = mdformat.format(calendar.getTime());

        etDate.setText(strDate);
        etDate.setEnabled(false);
        etTime.setOnClickListener(this);


        btnCheckIn.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);

        textInputEditVehicleNumber.setHint("MP07MQ1234");
        textInputEditVehicleNumber.setSelection(textInputEditVehicleNumber.getText().length());
        // textInputEditVehicleNumber.requestFocus();

//        etTime.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog dlg;
//                dlg = new TimePickerDialog(booking.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        etTime.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, false);
//                dlg.setTitle("Select Time");
//                dlg.show();
//            }
//        });


        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);

        availableSlots = sharedPreferences.getString("Slots", "");
        av = sharedPreferences.getString("av", "");
        userId = sharedPreferences.getString("userId", "");
        userContactNo = sharedPreferences.getString("userContactNo", "");
        pID = sharedPreferences.getString("pID", "");
        Login_Key = sharedPreferences.getString("Login_Key", "");
        text_1 = sharedPreferences.getString("text1", "");
        text_2 = sharedPreferences.getString("text2", "");
        // parkingId = sharedPreferences.getString("pID", "");


        availableSlots1 = availableSlots.toString().split(",");
        availableSlotstwo = availableSlots1[0];
//        availableSlotsfour = availableSlots1[1];


        if (availableSlots1.length == 1) {
            availableSlotstwo = availableSlots1[0];

            availableSlotsW = availableSlotstwo.toString().split(":");

            if (availableSlotsW.length == 2) {
                String vType = availableSlotsW[0];
                // String vSlot = availableSlotsW[1];
                if (vType.equals("2Wheeler")) {
                    twoWheeler.setVisibility(View.VISIBLE);
                    twoWheeler.setChecked(true);
                    vehicleType = "2Wheeler ";
                } else {
                    fourWheeler.setVisibility(View.VISIBLE);
                    fourWheeler.setChecked(true);
                    vehicleType = "4Wheeler ";
                }
            }

        }
        if (availableSlots1.length == 2) {
            //  availableSlotsfour = availableSlots1[1];
            twoWheeler.setVisibility(View.VISIBLE);
            twoWheeler.setChecked(true);
            vehicleType = "2Wheeler ";
            fourWheeler.setVisibility(View.VISIBLE);
        }
        etMobileNo.setText(userContactNo);

    }

    @Override
    public void onClick(View v) {

        // login = AppConstants.getSetting(booking.this, AppConstants.Login_Key, false);
        //register = AppConstants.getSetting(booking.this, AppConstants.RegisterKey, false);

        if (v == btnCheckIn) {
            //   validation(pID, userId, vehicleType, etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
            validate();
            // bookslot();
        }


        if (v == etTime) {
            // setTime();
            showTimePicker();

        }

    }


    private void showTimePicker() {

        if (newCalendar == null) {
            newCalendar = Calendar.getInstance();
        }

        mEventTimePickerDialog = new TimePickerDialog(booking.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.MONTH, newCalendar.get(Calendar.MONTH));
                datetime.set(Calendar.DAY_OF_MONTH, newCalendar.get(Calendar.DAY_OF_MONTH));
                datetime.set(Calendar.YEAR, newCalendar.get(Calendar.YEAR));
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.SECOND, 00);
                datetime.set(Calendar.MINUTE, minute);
                mSelectDate = new Date(datetime.getTimeInMillis());
                Date mCurrentDate = new Date(c.getTimeInMillis());

                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                    //it's after current
                    int hour = hourOfDay % 12;
                    etTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                } else {
                    //it's before current'
                    Toast.makeText(booking.this, "Invalid Time", Toast.LENGTH_LONG).show();
                }

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);

        mEventTimePickerDialog.show();
    }

    public void validate() {

        String time = etTime.getText().toString();
        String date = etDate.getText().toString();
        String vNo = textInputEditVehicleNumber.getText().toString();
        String mNo = etMobileNo.getText().toString();

        String url = AppConstants.BASEURL + "booking/validate";
        Map<String, String> parameterData = new HashMap<>();

        parameterData.put(("parkingId"), pID);
        parameterData.put(("boardingTime"), etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
        parameterData.put(("vehicleType"), vehicleType);
        parameterData.put(("userId"), userId);

        if (AppConstants.isInternetAvailable(this)) {


            if (time.equals("")) {
                etTime.setError("Select Boarding Time");
                etTime.requestFocus();
            } else if (vNo.equals("")) {
                textInputEditVehicleNumber.setError("Enter Vehicle Number");
                textInputEditVehicleNumber.requestFocus();
            } else {
                validateServices(url, parameterData);
            }
            // bookServices(url, parameterData);

        }
    }

    public void validateServices(String url, final Map<String, String> params) {

// pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(booking.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    bookServicesServicesValidate(jsonObject);
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

    private void bookServicesServicesValidate(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");
                    String uID = checkINresponce.getString("userId");
                    if (uID.equals("0")) {
                        SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("mobileNo", userContactNo);
                        editor.putString("parkingId", pID);
                        editor.putString("boardingTime", etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
                        editor.putString("vehicleNo", textInputEditVehicleNumber.getText().toString());
                        editor.putString("vehicleType", vehicleType);
                        editor.putString("withoutLogin", "1");
                        editor.apply();
                        editor.commit();

                        Intent intentbook = new Intent(booking.this, Login.class);
                        startActivity(intentbook);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    } else {
                        bookslot();
                    }

                } else {
                    Intent intentbook = new Intent(booking.this, Login.class);
                    startActivity(intentbook);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    Toast.makeText(booking.this, message, Toast.LENGTH_LONG).show();

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

        SharedPreferences storeAllValues1 = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        String userContactNo = storeAllValues1.getString("userContactNo", "");
        String time = etTime.getText().toString();
        String date = etDate.getText().toString();
        String vNo = textInputEditVehicleNumber.getText().toString();
        String mNo = etMobileNo.getText().toString();

        String url = AppConstants.BASEURL + "booking/create";
        Map<String, String> parameterData = new HashMap<>();

        parameterData.put(("mobileNo"), userContactNo);
        parameterData.put(("parkingId"), pID);
        parameterData.put(("boardingTime"), etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
        parameterData.put(("vehicleNo"), textInputEditVehicleNumber.getText().toString());
        parameterData.put(("vehicleType"), vehicleType);

        if (AppConstants.isInternetAvailable(this)) {
            bookServices(url, parameterData);
            /*if (Login_Key.equals("true")) {

                if (time.equals("")) {
                    etTime.setError("Select Boarding Time");
                    etTime.requestFocus();
                } else if (vNo.equals("")) {
                    textInputEditVehicleNumber.setError("Enter Vehicle Number");
                } else {
                    bookServices(url, parameterData);
                }
                // bookServices(url, parameterData);
            }*/ /*else {

                if (time.equals("")) {
                    etTime.setError("Select Boarding Time");
                    etTime.requestFocus();
                } else if (vNo.equals("")) {
                    textInputEditVehicleNumber.setError("Enter Vehicle Number");
                } else {
                    SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("mobileNo", userContactNo);
                    editor.putString("parkingId", pID);
                    editor.putString("boardingTime", etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
                    editor.putString("vehicleNo", vNo);
                    editor.putString("vehicleType", vehicleType);
                    editor.putString("withoutLogin", "1");
                    editor.apply();
                    editor.commit();

                    Intent intentbook = new Intent(booking.this, Login.class);
                    startActivity(intentbook);
                    finish();
                }
//                SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = storeAllValues.edit();
//                editor.putString("mobileNo", userContactNo);
//                editor.putString("parkingId", pID);
//                editor.putString("boardingTime", etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
//                editor.putString("vehicleNo", vNo);
//                editor.putString("vehicleType", vehicleType);
//                editor.putString("withoutLogin", "1");
//                editor.apply();
//                editor.commit();
//
//                Intent intentbook = new Intent(booking.this, Login.class);
//                startActivity(intentbook);
//                finish();


            }
*/

            // bookServices(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

    }

    public void bookServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(booking.this);
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

                    Intent intent = new Intent(booking.this, Payment.class);

                    intent.putExtra("receipt", receipt);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Toast.makeText(booking.this, message, Toast.LENGTH_LONG).show();
                    finish();


                } else {
                    if (status == 1) {
                        SharedPreferences storeAllValues = getSharedPreferences("BookingParking", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("mobileNo", userContactNo);
                        editor.putString("parkingId", pID);
                        editor.putString("boardingTime", etDate.getText().toString().trim() + " " + etTime.getText().toString().trim());
                        editor.putString("vehicleNo", textInputEditVehicleNumber.getText().toString());
                        editor.putString("vehicleType", vehicleType);
                        editor.putString("withoutLogin", "1");
                        editor.apply();
                        editor.commit();

                        Intent intentbook = new Intent(booking.this, Login.class);
                        startActivity(intentbook);
                        finish();

                        Toast.makeText(booking.this, message, Toast.LENGTH_LONG).show();
                    }

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

    public void parkingRate(String pID) {
        final ProgressDialog progressDialog = new ProgressDialog(booking.this);
        progressDialog.setMessage("Loding");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress_bar);

        //https://opark.in/O_par_aPi/userapp/live/index.php/v1/parking/rate?parkingId=1
        String urlData = AppConstants.BASEURL + "parking/rate?parkingId=" + pID;
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

                        String rateText2W = jsonObject.getString("rateText2W");
                        String rateText4W = jsonObject.getString("rateText4W");
                        if (!rateText2W.equals("")) {
                            two_wheeler.setText(rateText2W);
                            two_wheeler.setVisibility(View.VISIBLE);
                        }
                        if (!rateText4W.equals("")) {
                            four_wheeler.setText(rateText4W);
                            four_wheeler.setVisibility(View.VISIBLE);
                        }
                        text1.setText(text_1);
                        text2.setText(text_2);
                        star.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(booking.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(booking.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void validation(String parkingId, String userId, String vehicleType, String boardingTime) {

        final ProgressDialog progressDialog = new ProgressDialog(booking.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/search/parking?latitude=23.226494&longitude=77.441501*/
        String urlData = AppConstants.BASEURL + "booking/validate?parkingId=" + parkingId + "&userId=" + userId + "&vehicleType=" + vehicleType + "&boardingTime=" + boardingTime;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");


                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();


                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(booking.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    //progressDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        progressDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
    }

    public void setTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(booking.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                            //it's after current
                            int hour = hourOfDay % 12;
                            etTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                        } else {
                            //it's before current'
                            Toast.makeText(booking.this, "Invalid Time", Toast.LENGTH_LONG).show();
                        }


                        //txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
