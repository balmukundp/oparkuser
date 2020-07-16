package in.user.receipt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.receipt.adapter.ReceiptAdapter;
import in.user.receipt.model.ClickListener;
import in.user.receipt.model.DividerItemDecoration;
import in.user.receipt.model.ReceiptModel;
import in.user.receipt.model.RecyclerTouchListener;


public class Receipt extends AppCompatActivity {
    RecyclerView recyclerView;
    ReceiptAdapter receiptAdapter;
    ArrayList<ReceiptModel> receiptModelList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    String userId;
    Toolbar toolBar;
    TextView textToolHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        init();


    }

    public void init() {

        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("My Receipt");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));

        setListener();

    }

    public void setListener() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(Receipt.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

//                Toast.makeText(CarInActivity.this, vehicleModelList.get(position).getTransactionId(), Toast.LENGTH_SHORT).show();
                try {

                    if (AppConstants.isInternetAvailable(Receipt.this)) {

                        if (receiptModelList.get(position).getReceipt().equals("")) {
                            Toast.makeText(Receipt.this, "Expire & Cancel by System!", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(Receipt.this, ViewReceipt.class);
                            intent.putExtra("receipt", receiptModelList.get(position).getReceipt());
                            intent.putExtra("cancelBooking", receiptModelList.get(position).getCancelStatus());
                            intent.putExtra("parkingStatus", receiptModelList.get(position).getParkingStatus());
                            intent.putExtra("orderId", receiptModelList.get(position).getOrderId());
                            intent.putExtra("bookingId", receiptModelList.get(position).getBookingId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }

                    } else {
                        Toast.makeText(Receipt.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                    }


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstants.isInternetAvailable(Receipt.this)) {
            receiptServices(userId);
        } else {
            Toast.makeText(Receipt.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

    }

    public void receiptServices(String userId) {
        final ProgressDialog pDialog = new ProgressDialog(Receipt.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
       /*http://staggingapi.opark.in/index.php/v1/report/list?parkingId=1*/
        String urlData = AppConstants.BASEURL + "booking/list?userId=" + userId;

        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                receiptModelList.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());
                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    int detail = json.getInt("detail");

                    if (status == 0) {
                        final JSONArray arrayObj = new JSONArray(data);
                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);
                            ReceiptModel receiptModel = new ReceiptModel();

                            String orderId = jsonObject.getString("orderId");
                            String bookingId = jsonObject.getString("bookingId");
                            String parkingName = jsonObject.getString("parkingName");
                            String bookingDateTime = jsonObject.getString("bookingDateTime");
                            String boardingDateTime = jsonObject.getString("boardingDateTime");
                            String holdingTime = jsonObject.getString("holdingTime");
                            String vehicleNo = jsonObject.getString("vehicleNo");
                            String mobileNo = jsonObject.getString("mobileNo");
                            String vehicleType = jsonObject.getString("vehicleType");
                            String receipt = jsonObject.getString("receipt");
                            String timeDifference1 = jsonObject.getString("timeDifference");
                            String parkingStatus = jsonObject.getString("parkingStatus");
                            String cancelStatus = jsonObject.getString("cancelStatus");


                            receiptModel.setOrderId(orderId);
                            receiptModel.setBookingId(bookingId);
                            receiptModel.setParkingName(parkingName);
                            receiptModel.setBoardingDateTime(boardingDateTime);
                            receiptModel.setBookingDateTime(bookingDateTime);
                            receiptModel.setHoldingTime(holdingTime);
                            receiptModel.setVehicleNo(vehicleNo);
                            receiptModel.setMobileNo(mobileNo);
                            receiptModel.setVehicleType(vehicleType);
                            receiptModel.setReceipt(receipt);
                            receiptModel.setParkingStatus(parkingStatus);
                            receiptModel.setCancelStatus(cancelStatus);

                            receiptModel.setTimeDifference(timeDifference1);

                            receiptModelList.add(receiptModel);
                        }
                        receiptAdapter = new ReceiptAdapter(Receipt.this, Receipt.this, receiptModelList);
                        recyclerView.setAdapter(receiptAdapter);
                        receiptAdapter.notifyDataSetChanged();


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(Receipt.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

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
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);

    }
}
