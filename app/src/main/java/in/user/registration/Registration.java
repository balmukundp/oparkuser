package in.user.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.base.CustomRequest;
import in.user.base.InputValidation;
import in.user.registration.model.SignUp;


public class Registration extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout textInputLayoutFirstName;
    TextInputLayout textInputLayoutLastNane;
    TextInputLayout textInputLayoutPassword;
    TextInputLayout textInputLayoutMobileNumber;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPasswordConform;

    TextInputEditText textInputEditFirstName;
    TextInputEditText textInputTextLastName;
    TextInputEditText textInputEditPassword;
    TextInputEditText textInputEditMobileNumber;
    TextInputEditText textInputEditemail;
    TextInputEditText textInputEditPasswordConform;
    Toolbar toolBar;
    TextView textToolHeader;

    InputValidation inputValidation;

    AppCompatButton appCompatButtonRegister;

    String cityID, firstName, lastName, password, confPassword, mobileNumber, email, userFullname, userMobileNumber, City, OTP, UserId, userEmail, emailPattern;
    Spinner spinnerCity;
    ArrayList<String> plannames = new ArrayList<>();
    ArrayList<CityName> planeList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();


    }

    public void initViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("SignUp");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        textInputLayoutFirstName = findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutLastNane = findViewById(R.id.textInputLayoutLastNane);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutMobileNumber = findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPasswordConform = findViewById(R.id.textInputLayoutPasswordConform);

        textInputEditFirstName = findViewById(R.id.textInputEditFirstName);
        textInputTextLastName = findViewById(R.id.textInputTextLastName);
        textInputEditPassword = findViewById(R.id.textInputEditPassword);
        textInputEditMobileNumber = findViewById(R.id.textInputEditMobileNumber);
        textInputEditemail = findViewById(R.id.textInputEditemail);
        textInputEditPasswordConform = findViewById(R.id.textInputEditPasswordConform);

        spinnerCity = findViewById(R.id.spinnerCity);
        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

        appCompatButtonRegister.setOnClickListener(Registration.this);
        inputValidation = new InputValidation(Registration.this);


        textInputEditPassword.setHint("Password Must be 4 digit");
        textInputEditPassword.setSelection(textInputEditPassword.getText().length());
        textInputEditPasswordConform.setHint("Confirm Password");
        textInputEditPasswordConform.setSelection(textInputEditPasswordConform.getText().length());
        textInputEditemail.setSelection(textInputEditemail.getText().length());
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationApi();
            }
        });
        callApi();
    }

    public void callApi() {
        if (AppConstants.isInternetAvailable(Registration.this)) {
            cityListService();
        } else {
            Toast.makeText(Registration.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                registration();
                break;
        }*/

    }

    public void cleareText() {
        textInputEditFirstName.setText("");
        textInputTextLastName.setText("");
        textInputEditPassword.setText("");
        textInputEditMobileNumber.setText("");
        textInputEditemail.setText("");
        textInputEditPasswordConform.setText("");


        plannames.add("Select Plan");
        CityName populateSpinnerPojo1 = new CityName();
        populateSpinnerPojo1.setCityId("0");
        populateSpinnerPojo1.setCityName("Plane");
        planeList.add(populateSpinnerPojo1);

    }

    public void registrationApi() {

        firstName = textInputEditFirstName.getText().toString();
        lastName = textInputTextLastName.getText().toString();
        password = textInputEditPassword.getText().toString();
        mobileNumber = textInputEditMobileNumber.getText().toString();
        email = textInputEditemail.getText().toString();
        confPassword = textInputEditPasswordConform.getText().toString();

        if (firstName.isEmpty()) {

            Toast.makeText(this, "Enter First name", Toast.LENGTH_LONG).show();

            textInputEditFirstName.requestFocus();

        } else if (lastName.isEmpty()) {

            Toast.makeText(this, "Enter last Name", Toast.LENGTH_LONG).show();

            textInputTextLastName.requestFocus();

        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (textInputEditPassword.getText().toString().length() < 4) {//password.isEmpty()

            Toast.makeText(this, "Password Must be 4 digit", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (confPassword.isEmpty()) {

            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (!password.contentEquals(confPassword)) {

            Toast.makeText(this, "Password does not Match", Toast.LENGTH_LONG).show();
            textInputEditPasswordConform.requestFocus();

        } else if (mobileNumber.isEmpty()) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_LONG).show();
            textInputEditMobileNumber.requestFocus();
        } else if (textInputEditMobileNumber.getText().toString().length() < 10) {//mobileNumber.isEmpty()
            Toast.makeText(this, "Mobile Number MUST BE 10 digit", Toast.LENGTH_LONG).show();
            textInputEditMobileNumber.requestFocus();

        } else {

            String url = AppConstants.BASEURL + "user/signup";
            HashMap<String, String> parameterData = new HashMap<>();

            parameterData.put("email", email);
            parameterData.put("mobileNo", mobileNumber);
            parameterData.put("city", cityID);
            parameterData.put("firstName", firstName);
            parameterData.put("lastName", lastName);
            parameterData.put("pin", password);

            if (AppConstants.isInternetAvailable(this)) {
                registerServices(url, parameterData);
            } else {
                Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void registerServices(String url, final Map<String, String> params) {

        final ProgressDialog pDialog = new ProgressDialog(Registration.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectRegisterApiServices(jsonObject);
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

    private void processJsonObjectRegisterApiServices(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");
                    SignUp signUp = new SignUp();

                    signUp.setUserID(checkINresponce.getString("userId"));
                    signUp.setUserFullname(checkINresponce.getString("userFullName"));
                    signUp.setUserMobileNo(checkINresponce.getString("userMobile"));
                    signUp.setUserEmail(checkINresponce.getString("userEmail"));
                    signUp.setCity(checkINresponce.getString("city"));
                    signUp.setOtp(checkINresponce.getString("otp"));

                    userFullname = signUp.getUserFullname();
                    UserId = signUp.getUserID();
                    userMobileNumber = signUp.getUserMobileNo();
                    City = signUp.getCity();
                    OTP = signUp.getOtp();
                    userEmail = signUp.getUserEmail();


                    cleareText();

                    Intent intent = new Intent(Registration.this, Otp.class);
                    intent.putExtra("otp", OTP);
                    intent.putExtra("UserId", UserId);
                    intent.putExtra("userMobileNumber", userMobileNumber);
                    intent.putExtra("userFullname", userFullname);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("city", City);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    Toast.makeText(Registration.this, message, Toast.LENGTH_LONG).show();

//                    textInputEditFirstName.setText("");
//                    textInputTextLastName.setText("");
//                    textInputEditPassword.setText("");
//                    textInputEditMobileNumber.setText("");
//                    textInputEditemail.setText("");

                } else {

                    Toast.makeText(Registration.this, message, Toast.LENGTH_SHORT).show();
//                    textInputEditFirstName.setText("");
//                    textInputTextLastName.setText("");
//                    textInputEditPassword.setText("");
//                    textInputEditMobileNumber.setText("");
//                    textInputEditemail.setText("");
//                    textInputEditPasswordConform.setText("");
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

    public void cityListService() {
        //http://staggingapi.opark.in/index.php/v1/city/list
        String urlData = AppConstants.BASEURL + "city/list";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");

                        plannames.add("Select City");
                        CityName populateSpinnerPojo1 = new CityName();
                        populateSpinnerPojo1.setCityId("0");
                        populateSpinnerPojo1.setCityName("Select City");
                        planeList.add(populateSpinnerPojo1);

                        try {

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(response.get("message"));
                            int status = response.getInt("status");
                            String response1 = String.valueOf(response.get("data"));
                            if (status == 0) {

                                final JSONArray arrayObj = new JSONArray(response1);

                                for (int i = 0; i < arrayObj.length(); i++) {

                                    JSONObject jsonObject = arrayObj.getJSONObject(i);

                                    String planId1 = jsonObject.getString("cityId");
                                    String planName = jsonObject.getString("cityName");

                                    CityName cityName = new CityName();
                                    cityName.setCityId(planId1);
                                    cityName.setCityName(planName);
                                    plannames.add(planName);
                                    planeList.add(cityName);
                                }
                                spinnerCity.setAdapter(new ArrayAdapter(Registration.this, R.layout.my_spinner_style, plannames));
                                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        cityID = planeList.get(pos).getCityId();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }
                                });
                            } else {

                                plannames.add(response1);
                                spinnerCity.setAdapter(new ArrayAdapter(Registration.this, R.layout.my_spinner_style, plannames));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");


                    }
                });
        {


            request.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().getRequestQueue().add(request);

        }
    }
}

   /* private boolean setSpinnerError(Spinner spinner, String error) {

        boolean flag = true;

        View selectedView = spinner.getSelectedView();

        if (!spinner.getSelectedItem().toString().equals(error)) {

            flag = true;

        } else {
            if (selectedView != null && selectedView instanceof TextView) {
                spinner.requestFocus();
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("error"); // any name of the error will do
                selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
                selectedTextView.setText("Select City"); // actual error message
                spinner.performClick(); // to open the spinner list if error is found.
                flag = false;
            }
        }
        return flag;
    }
    private void registerUser() {
        firstName = textInputEditFirstName.getText().toString();
        lastName = textInputTextLastName.getText().toString();
        password = textInputEditPassword.getText().toString();
        mobileNumber = textInputEditMobileNumber.getText().toString();
        email = textInputEditemail.getText().toString();
        confPassword = textInputEditPasswordConform.getText().toString();

        if (firstName.isEmpty()) {
            //  textInputEditFirstName.setError("Enter First name");
            Toast.makeText(this, "Enter First name", Toast.LENGTH_LONG).show();

            textInputEditFirstName.requestFocus();

        } else if (lastName.isEmpty()) {
            //   textInputTextLastName.setError("Enter last Name");
            Toast.makeText(this, "Enter last Name", Toast.LENGTH_LONG).show();

            textInputTextLastName.requestFocus();

        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (textInputEditPassword.getText().toString().length() < 4) {//password.isEmpty()
            //  textInputEditPassword.setError("Enter Password");
            Toast.makeText(this, "Password Must be 4 digit", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (confPassword.isEmpty()) {//
            //  textInputEditPassword.setError("Enter Password");
            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_LONG).show();
            textInputEditPassword.requestFocus();

        } else if (!password.contentEquals(confPassword)) {
            // textInputEditPasswordConform.setError("Password does not Match");
            Toast.makeText(this, "Password does not Match", Toast.LENGTH_LONG).show();
            textInputEditPasswordConform.requestFocus();

        } else if (mobileNumber.isEmpty()) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_LONG).show();
            // textInputEditMobileNumber.setError("Enter Mobile Number");
            textInputEditMobileNumber.requestFocus();
        } else if (textInputEditMobileNumber.getText().toString().length() < 10) {//mobileNumber.isEmpty()
            Toast.makeText(this, "Mobile Number MUST BE 10 digit", Toast.LENGTH_LONG).show();
            // textInputEditMobileNumber.setError("Enter Mobile Number");
            textInputEditMobileNumber.requestFocus();

        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // textInputEditemail.setError("Enter Valid Email");
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_LONG).show();

            textInputEditemail.requestFocus();

        } else {

            if (setSpinnerError(spinnerCity, "Select City")) {

                RegisterUser ru = new RegisterUser();
                ru.execute();
            }
        }
        *//*RegisterUser ru = new RegisterUser();
        ru.execute();*//*
    }
    class RegisterUser extends AsyncTask<Void, Void, String> {

        private ProgressBar progressBar;

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();


            HashMap<String, String> parameterData = new HashMap<>();

            parameterData.put(("email"), email);
            parameterData.put(("mobileNo"), userMobileNumber);
            parameterData.put(("city"), cityID);
            parameterData.put(("firstName"), firstName);
            parameterData.put(("lastName"), lastName);
            parameterData.put(("pin"), password);


            //returing the response
            return requestHandler.sendPostRequest(AppConstants.BASEURL, parameterData);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //displaying the progress bar while user registers on the server
            //          progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //hiding the progressbar after completion
            //  progressBar.setVisibility(View.GONE);

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

//                String message = (String) obj.get("message");
//                String data = (String) obj.get("data");
//                int status = obj.getInt("status");
                if (!s.equals("")) {

                    JSONObject checkINresponce = obj.getJSONObject("data");
//                        JSONObject message = obj.getJSONObject("message");
//                        JSONObject status = obj.getJSONObject("status");
                    SignUp signUp = new SignUp();
                    signUp.setUserID(checkINresponce.getString("userId"));
                    signUp.setUserFullname(checkINresponce.getString("userFullName"));
                    signUp.setUserMobileNo(checkINresponce.getString("userMobile"));
                    signUp.setUserEmail(checkINresponce.getString("userEmail"));
                    signUp.setCity(checkINresponce.getString("city"));
                    signUp.setOtp(checkINresponce.getString("otp"));

                    userFullname = signUp.getUserFullname();
                    UserId = signUp.getUserID();
                    userMobileNumber = signUp.getUserMobileNo();
                    City = signUp.getCity();
                    OTP = signUp.getOtp();
                    userEmail = signUp.getUserEmail();


                    cleareText();

                    Intent intent = new Intent(Registration.this, Otp.class);
                    intent.putExtra("otp", OTP);
                    intent.putExtra("UserId", UserId);
                    intent.putExtra("userMobileNumber", userMobileNumber);
                    intent.putExtra("userFullname", userFullname);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("City", City);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    //  Toast.makeText(Registration.this, message, Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

