package in.user.receipt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.onlinebooking.CancleBooking;


public class ViewReceipt extends AppCompatActivity {

    private String RECEIPT_URL = "", orderId = "", bookingId = "",cancelBooking="",parkingStatus="";
    private WebView webview;
    protected ProgressDialog progressDialog;
    Toolbar toolBar;
    TextView textToolHeader;
    Button cancelBooking1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        init();


    }

    public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Receipt Detail");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cancelBooking1 = (Button) findViewById(R.id.cancelBooking);

        Intent intent = getIntent();
        String receipt = intent.getStringExtra("receipt");
        orderId = intent.getStringExtra("orderId");
        bookingId = intent.getStringExtra("bookingId");
        cancelBooking = intent.getStringExtra("cancelBooking");
        parkingStatus = intent.getStringExtra("parkingStatus");
         if (cancelBooking.equals("")&&parkingStatus.equals("")){
             cancelBooking1.setVisibility(View.VISIBLE);
         }else {
             cancelBooking1.setVisibility(View.GONE);
         }



        cancelBooking1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent1 = new Intent(ViewReceipt.this, CancleBooking.class);
//                intent1.putExtra("orderId", orderId );
//                intent1.putExtra("bookingId", bookingId);
//                startActivity(intent1);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                cancelBooking(bookingId, orderId);
            }
        });


        // Toast.makeText(this, receipt, Toast.LENGTH_SHORT).show();
        RECEIPT_URL = receipt;

        webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setGeolocationEnabled(true);

        setListener();


    }

    public void setListener() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String XLS_PPT_FILE = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String XLS_PPT_FILE = url;
                dismissProgressDialog();
            }

        });


        if (AppConstants.isInternetAvailable(ViewReceipt.this) && !AppConstants.isBlank(RECEIPT_URL)) {
            showProgressDialog();
            webview.loadUrl(RECEIPT_URL);

        } else {
            getFragmentManager().popBackStack();
        }
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
    }

    public void cancelBooking(String bookingId, String orderId) {

        final ProgressDialog pDialog = new ProgressDialog(ViewReceipt.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "booking/cancel?bookingId=" + bookingId + "&orderId=" + orderId;

        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                progressDialog.dismiss();

                try {

                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    int detail = json.getInt("detail");

                    if (status == 0) {
                        JSONObject arrayObj = json.getJSONObject("data");
                        String receipt1 = arrayObj.getString("receipt");

                        Intent intent1 = new Intent(ViewReceipt.this, CancleBooking.class);
                        intent1.putExtra("receipt1", receipt1);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        },

                new com.android.volley.Response.ErrorListener() {
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

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(ViewReceipt.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
