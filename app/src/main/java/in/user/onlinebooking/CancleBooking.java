package in.user.onlinebooking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppSession;
import in.user.receipt.Receipt;


public class CancleBooking extends AppCompatActivity {

    private Context mContext;
    private View view;
    private WebView webview;
    private String RECEIPT_URL = "";
    private AppSession appSession;
    private boolean isPrint = false;
    protected ProgressDialog progressDialog;
    Toolbar toolBar;
    TextView textToolHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_booking);
        findViews();
    }

    private void findViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Cancle Booking");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancleBooking.this, Receipt.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        Intent intent = getIntent();
        String receipt = intent.getStringExtra("receipt1");

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
                //https://opark.in/O_par_aPi/pytm/apiResponse.php
                dismissProgressDialog();
            }

        });
        if (AppConstants.isInternetAvailable(CancleBooking.this) && !AppConstants.isBlank(RECEIPT_URL)) {
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

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(CancleBooking.this);
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
