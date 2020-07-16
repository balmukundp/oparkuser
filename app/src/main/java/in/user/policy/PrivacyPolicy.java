package in.user.policy;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.user.MainActivity;
import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.parking.NearestParking;
import in.user.receipt.ViewReceipt;

public class PrivacyPolicy extends AppCompatActivity {
    Toolbar toolBar;
    TextView textToolHeader;
    private String RECEIPT_URL = "", receipt = "";
    private WebView webview;
    protected ProgressDialog progressDialog;
    // JustifiedTextView policy, Collection, data, cookies, security, Links_to_OtherSites, Children_Privacy, Changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);


        init();
        //https://opark.in/O_par_aPi/userapp/live/index.php/v1/page/content?pageName=privacy_policy
        if (AppConstants.isInternetAvailable(PrivacyPolicy.this)) {
            privacy_policy();
        } else {
            Toast.makeText(this, "Interner is required!", Toast.LENGTH_SHORT).show();
        }

    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Privacy Policy");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        webview = (WebView) findViewById(R.id.webviewp);


        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
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

//
//        if (AppConstants.isInternetAvailable(PrivacyPolicy.this) && !AppConstants.isBlank(RECEIPT_URL)) {
//            showProgressDialog();
//            webview.loadUrl(RECEIPT_URL);
//
//        } else {
//            getFragmentManager().popBackStack();
//        }
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
    }

    public void privacy_policy() {
        final ProgressDialog progressDialog = new ProgressDialog(PrivacyPolicy.this);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_progress_bar);
        String urlData = AppConstants.BASEURL + "page/content?pageName=privacy_policy";
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");


                    if (status == 0) {

                        JSONObject jsonObject = json.getJSONObject("data");
                        receipt = jsonObject.getString("pageUrl");

                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.getSettings().setAppCacheEnabled(true);
                        webview.getSettings().setDatabaseEnabled(true);
                        webview.getSettings().setDomStorageEnabled(true);
                        webview.getSettings().setSupportZoom(true);
                        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        webview.getSettings().setBuiltInZoomControls(true);
                        webview.getSettings().setGeolocationEnabled(true);

                        if (AppConstants.isInternetAvailable(PrivacyPolicy.this) && !AppConstants.isBlank(receipt)) {
                            showProgressDialog();
                            webview.loadUrl(receipt);
                        } else {
                            getFragmentManager().popBackStack();
                        }


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();


                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(PrivacyPolicy.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    //  progressDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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
        progressDialog = new ProgressDialog(PrivacyPolicy.this);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_progress_bar);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
