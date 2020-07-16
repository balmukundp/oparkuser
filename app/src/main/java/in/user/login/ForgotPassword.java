package in.user.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.registration.Otp;

public class ForgotPassword extends AppCompatActivity   {

    Toolbar toolBar;
    TextView textToolHeader;
    Button btn_yes;
    TextInputLayout textInputLayoutMobileNumber;
    TextInputEditText textInputEditMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        init();



    }

    public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Forgot Password ");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_yes = (Button) findViewById(R.id.done);
        textInputEditMobileNumber = (TextInputEditText) findViewById(R.id.textInputEditMobileNumber);
        textInputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.textInputLayoutMobileNumber);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = textInputEditMobileNumber.getText().toString();
                if (!str.equals("")) {
                    forgotPassword(textInputEditMobileNumber.getText().toString().trim());

                } else {
                    Toast.makeText(ForgotPassword.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void forgotPassword(String mobileNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("Loding");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_bar);

        //https://opark.in/O_par_aPi/userapp/stg/index.php/v1/user/password?mobileNo=9993289838

        String urlData = AppConstants.BASEURL + "user/password?mobileNo=" + mobileNumber;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("JSON RETURN  " + response.toString());
                    int status = response.getInt("status");
                    String message = String.valueOf(response.get("message"));
                    String data = String.valueOf(response.get("data"));

                    if (status == 0) {

                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                        Intent forgot = new Intent(ForgotPassword.this, Login.class);
                        startActivity(forgot);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();


                    } else {
                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ForgotPassword.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


}
