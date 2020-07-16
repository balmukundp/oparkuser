package in.user.onlinebooking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import in.user.MainActivity;
import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppSession;


public class Payment extends AppCompatActivity {

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
        setContentView(R.layout.activity_payment);
        findViews();
    }

    private void findViews() {
//        if (null != getArguments() && getArguments().containsKey(AppConstants.COMMON_TAG)) {
//            RECEIPT_URL = getArguments().getString(AppConstants.COMMON_TAG);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Payment");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Payment.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

            }
        });

        Intent intent = getIntent();
        String receipt = intent.getStringExtra("receipt");

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

        //  webview.setWebViewClient(new MyWebViewClient());

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //http://staggingapi.opark.in/global/receipt/booking/1357531245110.html
                //https://opark.in/O_par_aPi/pytm/apiRedirect.php

                //http://staggingapi.opark.in/global/receipt/booking/6433115.html
                //https://accounts-uat.paytm.com/oauth2/login/otp?response_type=code&scope=paytm&theme=pg-otp&redirectUri=https:securegw-stage.paytm.in/theia/oauthResponse&email-prefill=nitin@daffodilglobal.com&clientId=testclient&loginData=OP20180806128615:Daffod97592936822665:WAP:20180806111212800110168038800017749:7A1C30BFD8101318548E7075CA2B1CE2.Daffod97592936822665OP20180806128615:MANUAL
                //https://securegw-stage.paytm.in/theia/processTransaction?MID=Daffod97592936822665&ORDER_ID=OP2018080812864&route=&oauth=true
                String XLS_PPT_FILE = url;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //http://staggingapi.opark.in/global/receipt/booking/1357531245110.html
                //https://opark.in/O_par_aPi/pytm/apiRedirect.php
                //https://securegw-stage.paytm.in/theia/processTransaction

                String XLS_PPT_FILE = url;
                //https://opark.in/O_par_aPi/pytm/apiResponse.php
                dismissProgressDialog();
            }

        });
        if (AppConstants.isInternetAvailable(Payment.this) && !AppConstants.isBlank(RECEIPT_URL)) {
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

        /*if (AppConstants.isInternetAvailable(Payment.this) && !AppConstants.isBlank(RECEIPT_URL)) {
            showProgressDialog();
            webview.loadUrl(RECEIPT_URL);

        } else {
            getFragmentManager().popBackStack();
        }*/
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgressDialog();
        }
    }

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(Payment.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Payment.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
