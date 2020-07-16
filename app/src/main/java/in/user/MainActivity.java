package in.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.base.ReloadMapCallback;
import in.user.base.SingleShotLocationProvider;
import in.user.login.Login;
import in.user.login.LoginModel;
import in.user.onlinebooking.booking;
import in.user.parking.NearestParking;
import in.user.policy.AboutUs;
import in.user.policy.ContactUs;
import in.user.policy.PrivacyPolicy;
import in.user.policy.Refund_and_cancellation;
import in.user.policy.TERMSANDCONDITIONS;
import in.user.receipt.Receipt;
import in.user.registration.Otp;
import in.user.updateprofile.UpdateProfile;

import static java.util.Locale.getDefault;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, ReloadMapCallback {
    private static final String STATE_SCORE = "1";
    private static final String STATE_LEVEL = "0";
    NavigationView navigationView;
    Toolbar toolBar;
    TextView tvTitleToolbar;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String agentId, userRole, userName, userContactNo, vendorId, isUserVerified, city, cityId;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    //LocationRequest mLocationRequest;
    private GoogleMap mMap;
    //  private List<NearestParking> nearestParkingArrayList = new ArrayList<>();
    // private NearestParking parkingBean = new NearestParking();
    ClusterMarkerLocation parkingBean;
    private List<ClusterMarkerLocation> nearestParkingArrayList = new ArrayList<>();

    private LinearLayout llAvailableSpot;
    private TextView tvLocation;
    private TextView tvAvailableSlots;
    private TextView tvDistance;
    private TextView tvuserContactNumber;
    private TextView tvUsername;
    private TextView tvTimeToTravel, book;
    ImageView refresh;
    String Login_Key, RegisterKey, UserPhoto = "", userEmail;
    private LatLng latLng;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String cid = "", mcc = "", mnc = "", lac = "";
    String networkOperator;
    private String mLat = "";
    private String mLong = "";
    LocationManager lm;
    Location location;
    private static final int REQUEST_CODE_GPS_SETTINGS = 101;

    private String latitute = "", nothing = "", address12 = "";
    private String longitute = "";
    private TextView userContactNumber, nameHeader, headerContactNumber;
    ImageView userImage;
    static final float COORDINATE_OFFSET = 0.00002f; // You can change this value according to your need
    boolean login, register;
    private ClusterManager<ClusterMarkerLocation> mClusterManager;
    ProgressBar pb;
    Circle circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        View hraderView = navigationView.getHeaderView(0);
        nameHeader = hraderView.findViewById(R.id.Username);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);
        } else {
            changeTextStatus(false);
        }

    }

    // Method to change the text status
    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {
            if (mMap == null) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("No Internet Connetion, Connect to Internet... ").setIcon(R.drawable.oparklogonew).setTitle("Oops!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        AppController.activityPaused();// On Pause notify the Application
        // getMapData();
    }


    @Override
    protected void onResume() {
        super.onResume();

        AppController.activityResumed();// On Pause Resume the Application
        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);

        agentId = sharedPreferences.getString("userId", "");
        userRole = sharedPreferences.getString("userRole", "");
        userName = sharedPreferences.getString("userName", "");
        userContactNo = sharedPreferences.getString("userContactNo", "");
        isUserVerified = sharedPreferences.getString("isUserVerified", "");
        city = sharedPreferences.getString("city", "");
        cityId = sharedPreferences.getString("cityId", "");
        userEmail = sharedPreferences.getString("userEmail", "");
        Login_Key = sharedPreferences.getString("Login_Key", "");
        RegisterKey = sharedPreferences.getString("RegisterKey", "");


        if (Login_Key.equals("true") || RegisterKey.equals("true")) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_receipt).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_update_profile).setVisible(true);
            nameHeader.setText(userName);
            headerContactNumber.setText(userContactNo);
            UserPhoto = sharedPreferences.getString("UserPhoto", "");

            if (UserPhoto.equals("")) {
                Picasso.with(MainActivity.this).load(R.drawable.male).placeholder(R.drawable.male).into(userImage);

            } else {
                Picasso.with(MainActivity.this).load(UserPhoto).placeholder(R.drawable.male).into(userImage);
            }

        } else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_receipt).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_update_profile).setVisible(false);
        }

    }


    @SuppressLint("NewApi")
    public void init() {

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");

        navigationView =   findViewById(R.id.nav_view);
        drawerLayout =  findViewById(R.id.drawer_layout);

        llAvailableSpot =   findViewById(R.id.llAvailableSpot);
        tvLocation =  findViewById(R.id.tvLocation);
        tvAvailableSlots =   findViewById(R.id.tvAvailableSlots);
        tvDistance =   findViewById(R.id.tvDistance);
        tvTimeToTravel = findViewById(R.id.tvTimeToTravel);
        book =   findViewById(R.id.book);
        refresh =   findViewById(R.id.refresh);



        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));


        View hraderView = navigationView.getHeaderView(0);
        nameHeader =  hraderView.findViewById(R.id.Username);
        userContactNumber =   hraderView.findViewById(R.id.userContactNumber);
        userImage =   hraderView.findViewById(R.id.userImage);
        nameHeader.setText(userName);
        userContactNumber.setText(userContactNo);
        userImage.setImageResource(R.drawable.male);

        setListener();

    }

    private void getMapData() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 102);
            } else {
                loadmapdata();
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            networkOperator = telephonyManager.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator)) {
                mcc = (networkOperator.substring(0, 3));
                mnc = (networkOperator.substring(3));
            }//40478

            cid = String.valueOf(cellLocation.getCid());
            lac = String.valueOf(cellLocation.getLac());
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {

                latitute = String.valueOf(location.getLatitude());
                longitute = String.valueOf(location.getLongitude());
                if (AppConstants.isInternetAvailable(MainActivity.this)) {
                    nearestParking(latitute, longitute, cid, mcc, mnc, lac);
                } else {
                    Toast.makeText(this, "Internet is required!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (AppConstants.isInternetAvailable(MainActivity.this)) {
                    nearestParking(mLat, mLong, cid, mcc, mnc, lac);
                } else {
                    Toast.makeText(this, "Internet is required!", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    public void readstatus() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 102);
        }
    }

    public void loadmapdata() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        networkOperator = telephonyManager.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            mcc = (networkOperator.substring(0, 3));
            mnc = (networkOperator.substring(3));
        }//40478

        cid = String.valueOf(cellLocation.getCid());
        lac = String.valueOf(cellLocation.getLac());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {

            latitute = String.valueOf(location.getLatitude());
            longitute = String.valueOf(location.getLongitude());
            if (AppConstants.isInternetAvailable(MainActivity.this)) {
                nearestParking(latitute, longitute, cid, mcc, mnc, lac);
                // Toast.makeText(this, "latitute= " + latitute + ", " + "longitute = " + longitute, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location required!", Toast.LENGTH_SHORT).show();
            }

        } else {

            if (AppConstants.isInternetAvailable(MainActivity.this)) {
                nearestParking(mLat, mLong, cid, mcc, mnc, lac);
                //Toast.makeText(this, mLat + ", " + mLong, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location required!", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void checkGpsAndGetLocation() {
        if (AppConstants.isGPSEnabled(MainActivity.this)) {
            getLocation(MainActivity.this);
        } else if (AppConstants.isNetworkProviderAvailable(MainActivity.this)) {
            getLocation(MainActivity.this);
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
                        latitute = location.longitude + "";
                        longitute = location.longitude + "";


                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkGpsAndGetLocation();
                        loadmapdata();
                        break;
                    }
                }
            case 102:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkGpsAndGetLocation();
                        loadmapdata();
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
                getLocation(MainActivity.this);
                // checkGpsAndGetLocation();
                loadmapdata();

                //  AppConstants.log(TAG, " Location providers: " + provider);
                //Start searching for location and update the location text when update available.
                // getLocation(Login.this);
            } else {
                //Users did not switch on the GPS
                AppConstants.showToast(MainActivity.this, "Some thing went wroung");
            }
        }
    }

    private void setListener() {

        book.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (nothing.equals("2Wheeler:0") || nothing.equals("4Wheeler:0") || nothing.equals("2Wheeler:0,4Wheeler:0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("There is no available slots ,try to another nearest parking").setIcon(R.drawable.logo).setTitle(address12)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // loadmapdata();
                                    dialog.cancel();
                                }
                            });
                            /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });*/
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Intent intentbook = new Intent(MainActivity.this, booking.class);
                    startActivity(intentbook);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    intentbook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                }

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMapData();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {

                    case R.id.nav_update_profile:

                        Intent intentUp = new Intent(MainActivity.this, UpdateProfile.class);
                        startActivity(intentUp);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_receipt:

                        Intent intent = new Intent(MainActivity.this, Receipt.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;

                    case R.id.nav_about_us:
                        Intent nav_about_us = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(nav_about_us);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        return true;

                    case R.id.nav_Privacy_policy:


                        Intent intentabout = new Intent(MainActivity.this, PrivacyPolicy.class);
                        startActivity(intentabout);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;
                    case R.id.nav_Contact_us:
                        Intent nav_Contact_us = new Intent(MainActivity.this, ContactUs.class);
                        startActivity(nav_Contact_us);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;
                    case R.id.nav_Terms_and_condition:
                        Intent nav_Terms_and_condition = new Intent(MainActivity.this, TERMSANDCONDITIONS.class);
                        startActivity(nav_Terms_and_condition);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;
                    case R.id.nav_Refund_and_cancellation:
                        Intent nav_Refund_and_cancellation = new Intent(MainActivity.this, Refund_and_cancellation.class);
                        startActivity(nav_Refund_and_cancellation);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;
                    case R.id.nav_logout:
                        logout();
                        return true;
                }
                return true;
            }

        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        }

        ;
        actionBarDrawerToggle.syncState();


        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to Logout?").setIcon(R.mipmap.ic_launcher).setTitle("Opark")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //getDetails(agentId, "1");

                        getSharedPreferences("oparkuser", Context.MODE_PRIVATE).edit().remove("userId").remove("userRole").remove("UserPhoto").remove("userAddress")
                                .remove("userGender").remove("userName").remove("userContactNo").remove("isUserVerified").remove("city").remove("cityId").remove("Login_Key").remove("RegisterKey")
                                .remove("UserDob").commit();
                        // login = AppConstants.getSetting(MainActivity.this, AppConstants.Login_Key, false);

                        Intent logout_intent1 = new Intent(MainActivity.this, MainActivity.class);

                        startActivity(logout_intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void nearestParking(String latitude, String longitude, String cid, String mcc, String mnc, String lac) {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/search/parking?latitude=23.226494&longitude=77.441501*/
        String urlData = AppConstants.BASEURL + "search/parking?latitude=" + latitude + "&longitude=" + longitude + "&cid=" + cid + "&mcc=" + mcc + "&mnc=" + mnc + "&lac=" + lac;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                nearestParkingArrayList.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());

                    final String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");

                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);

                            ClusterMarkerLocation parkinglListModel = new ClusterMarkerLocation();

                            //parkinglListModel.setDistance(jsonObject.getString("distance"));
                            parkinglListModel.setVendorId(jsonObject.getString("vendorId"));
                            parkinglListModel.setParkingId(jsonObject.getString("parkingId"));
                            parkinglListModel.setLatitude(jsonObject.getString("latitude"));
                            parkinglListModel.setLongitude(jsonObject.getString("longitude"));
                            parkinglListModel.setTimetoTravel(jsonObject.getString("timetoTravel"));
                            parkinglListModel.setDistance(jsonObject.getString("distance"));
                            parkinglListModel.setAddress(jsonObject.getString("address"));
                            parkinglListModel.setText1(jsonObject.getString("text1"));
                            parkinglListModel.setText2(jsonObject.getString("text2"));
                            parkinglListModel.setAvailableSlots(jsonObject.getString("availableSlot"));


                            nearestParkingArrayList.add(parkinglListModel);
                        }


                        double location;

                        for (int i = 0; i < nearestParkingArrayList.size(); i++) {
                            ClusterMarkerLocation bean = nearestParkingArrayList.get(i);
                            latLng = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
                            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(11.0f);
                            CameraUpdate zoom1 = CameraUpdateFactory.zoomOut();

                            if (i == 0) {
                                // Toast.makeText(MainActivity.this, "Connection is so week Press Refresh button  ", Toast.LENGTH_SHORT).show();

                                openFirstPosition(bean);

                                CircleOptions circleOptions = new CircleOptions();
                                circleOptions.center(new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude())));
                                circleOptions.radius(80);
                                circleOptions.fillColor(Color.TRANSPARENT);
                                circleOptions.strokeWidth(5);
                                circleOptions.strokeColor(Color.BLUE);
                                mMap.addCircle(circleOptions);

                                mMap.moveCamera(center);
                                mMap.animateCamera(zoom);

                                // mClusterManager.addItem(new ClusterMarkerLocation( bean.getAddress(),latLng));

                            }
                            mClusterManager.addItem(new ClusterMarkerLocation(latLng, bean.getAddress(), bean.getAvailableSlots(), bean.getDistance(), bean.getTimetoTravel(), bean.getParkingId()
                                    , bean.getVendorId(), bean.getText1(), bean.getText2(), bean.getLatitude(), bean.getLatitude()));

                        }
                        mClusterManager.cluster();


                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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

        request.setRetryPolicy(new

                DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().

                getRequestQueue().

                add(request);
    }


    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);


        mClusterManager = new ClusterManager<>(this, mMap);


        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(
                new ClusterManager.OnClusterClickListener<ClusterMarkerLocation>() {
                    @Override
                    public boolean onClusterClick(Cluster<ClusterMarkerLocation> cluster) {

                        return false;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<ClusterMarkerLocation>() {
                    @Override
                    public boolean onClusterItemClick(ClusterMarkerLocation clusterItem) {

                        //Toast.makeText(MapsActivity.this, "Cluster item click", Toast.LENGTH_SHORT).show();
                        if (location != null) {

                            latitute = String.valueOf(location.getLatitude());
                            longitute = String.valueOf(location.getLongitude());
                            if (AppConstants.isInternetAvailable(MainActivity.this)) {

                                showDataOnBottomView(latitute, longitute, clusterItem.getParkingId());
                            } else {
                                Toast.makeText(MainActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (AppConstants.isInternetAvailable(MainActivity.this)) {
                                showDataOnBottomView(latitute, longitute, clusterItem.getParkingId());
                            } else {
                                Toast.makeText(MainActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // if true, click handling stops here and do not show info view, do not move camera
                        // you can avoid this by calling:
                        // renderer.getMarker(clusterItem).showInfoWindow();

                        return false;
                    }
                });

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));

        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarkerLocation>() {
                    @Override
                    public void onClusterItemInfoWindowClick(ClusterMarkerLocation stringClusterItem) {

                        // Toast.makeText(MapsActivity.this, "Clicked info window: " + stringClusterItem.title,
                        // Toast.LENGTH_SHORT).show();
                    }
                });

        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);


    }


    public void data() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Gson gson = new Gson();
        String json1 = sharedPrefs.getString("mylist", "");
        Type type = new TypeToken<List<ClusterMarkerLocation>>() {
        }.getType();
        List<ClusterMarkerLocation> arrayList = gson.fromJson(json1, type);

        if (arrayList != null && arrayList.size() > 0) {
            double lat;
            double lng;
            for (int i = 0; i < arrayList.size(); i++) {
                ClusterMarkerLocation bean = arrayList.get(i);
                String av = bean.getAvailableSlots();
                lat = Double.parseDouble(bean.getLatitude());  //generator.nextDouble() / 3;
                lng = Double.parseDouble(bean.getLongitude());//generator.nextDouble() / 3;
                final LatLng latLng = new LatLng(lat, lng);
                if (av.equals("2Wheeler:0") || av.equals("4Wheeler:0") || av.equals("2Wheeler:0,4Wheeler:0")) {
                    mClusterManager.addItem(new ClusterMarkerLocation(latLng, bean.getAddress(), bean.getAvailableSlots(), bean.getDistance(), bean.getTimetoTravel(), bean.getParkingId()
                            , bean.getVendorId(), bean.getText1(), bean.getText2(), bean.getLatitude(), bean.getLatitude()));
                } else {
                    mClusterManager.addItem(new ClusterMarkerLocation(latLng, bean.getAddress(), bean.getAvailableSlots(), bean.getDistance(), bean.getTimetoTravel(), bean.getParkingId()
                            , bean.getVendorId(), bean.getText1(), bean.getText2(), bean.getLatitude(), bean.getLatitude()));
                }

            }
            mClusterManager.cluster();
        }
    }

    public void showDataOnBottomView(String latitute, String longitute, String parkingId) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.setContentView(R.layout.custom_progress_bar);
        // https://opark.in/O_par_aPi/userapp/stg/index.php/v1/search/parking_distance?latitude=23.2219&longitude=77.4393&parkingId=5
        String urlData = AppConstants.BASEURL + "search/parking_distance?latitude=" + latitute + "&longitude=" + longitute + "&parkingId=" + parkingId;
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

                        ParkingSlots loginModel = new ParkingSlots();

                        loginModel.setVendorId(jsonObject.getString("vendorId"));
                        loginModel.setDistance(jsonObject.getString("distance"));
                        loginModel.setTimetoTravel(jsonObject.getString("timetoTravel"));
                        loginModel.setParkingId(jsonObject.getString("parkingId"));
                        loginModel.setParkingName(jsonObject.getString("parkingName"));
                        loginModel.setLatitude(jsonObject.getString("latitude"));
                        loginModel.setLongitude(jsonObject.getString("longitude"));
                        loginModel.setAvailableSlots(jsonObject.getString("availableSlots"));
                        loginModel.setAddress(jsonObject.getString("address"));
                        loginModel.setText1(jsonObject.getString("text1"));
                        loginModel.setText2(jsonObject.getString("text2"));

                        String availableSlots = jsonObject.getString("availableSlots");
                        String distance = jsonObject.getString("distance");
                        String timetoTravel = jsonObject.getString("timetoTravel");
                        String address = jsonObject.getString("address");

                        tvAvailableSlots.setText(availableSlots);
                        tvDistance.setText(distance);
                        tvTimeToTravel.setText(timetoTravel);
                        tvLocation.setText(address);

                        showDataOnBottomView(loginModel);
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void openFirstPosition(ClusterMarkerLocation bean) {
        parkingBean = bean;
        try {
            llAvailableSpot.setVisibility(View.VISIBLE);
            tvAvailableSlots.setText(bean.getAvailableSlots());
            tvDistance.setText(bean.getDistance());
            tvTimeToTravel.setText(bean.getTimetoTravel());
            tvLocation.setText(bean.getAddress());
            String pID = String.valueOf(bean.getParkingId());
            String av = String.valueOf(bean.getAvailableSlots());
            String text1 = String.valueOf(bean.getText1());
            String text2 = String.valueOf(bean.getText2());
            nothing = av;
            address12 = String.valueOf(bean.getAddress());


            sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("av", av);
            editor.putString("Slots", av);
            editor.putString("pID", pID);
            editor.putString("text1", text1);
            editor.putString("text2", text2);

            editor.apply();
            editor.commit();

        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    private void showDataOnBottomView(ParkingSlots loginModel) {
        try {
            if (llAvailableSpot.getVisibility() == View.GONE) {
                llAvailableSpot.setVisibility(View.VISIBLE);
            }
            ParkingSlots bean = loginModel;

            //  parkingBean = bean;

            String av = String.valueOf(bean.getAvailableSlots());
            String pID = String.valueOf(bean.getParkingId());
            String text1 = String.valueOf(bean.getText1());
            String text2 = String.valueOf(bean.getText2());
            nothing = av;
            address12 = String.valueOf(bean.getAddress());

            sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("av", av);
            editor.putString("Slots", av);
            editor.putString("pID", pID);
            editor.putString("text1", text1);
            editor.putString("text2", text2);

            editor.apply();
            editor.commit();

        } catch (Exception e) {

        }
    }

    private ClusterMarkerLocation getModelFromString(String data) {
        ClusterMarkerLocation bean = new ClusterMarkerLocation();
        bean = new Gson().fromJson(data, ClusterMarkerLocation.class);
        return bean;
    }

    private String getStringFromModel(NearestParking bean) {
        String data = "";
        data = new Gson().toJson(bean);
        return data;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onReload() {

    }
}
