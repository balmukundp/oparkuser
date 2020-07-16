package in.user.updateprofile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import in.user.R;
import in.user.base.AppConstants;
import in.user.base.AppController;
import in.user.base.InputValidation;
import in.user.base.VolleyMultipartRequest;
import in.user.base.VolleySingleton;
import in.user.login.LoginModel;

import static in.user.base.AppConstants.BASEURL;


public class UpdateProfile extends AppCompatActivity {

    TextInputLayout textInputLayoutFirstName;
    TextInputLayout textInputLayoutLastNane;
    //   TextInputLayout textInputLayoutDOB;
    TextInputLayout textInputLayoutAddress;
    TextInputLayout textInputLayoutEmail;

    TextInputEditText textInputEditFirstName;
    TextInputEditText textInputTextLastName;
    TextView textInputEditDOB;
    TextInputEditText textInputEditeAddress;
    TextInputEditText textInputEditEmail;

    Toolbar toolBar;
    TextView textToolHeader;
    ImageView userImageUpdate;
    InputValidation inputValidation;
    AppCompatButton appCompatButtonRegister;
    String user_id, gender = "", userName = "";
    SharedPreferences sharedPreferences;
    RadioGroup radioGroup;
    RadioButton maleRadio, femaleRadio;
    public static Uri imageUri;
    private String selectedPath = "";
    public static final String APP_TEMP_FOLDER = "OparkUser";
    String userChoosenTask, profileIMage, UserPhoto = "", userAddress = "", UserDob = "", user_Gender = "", Login_Key = "", RegisterKey = "", userEmail = "", address = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String[] UserName = new String[3];
    String[] UserName1 = new String[3];
    SharedPreferences.Editor editor;
    Calendar myCalender;
    Date date;
    String[] user_Name;
    SimpleDateFormat mdformat;
    private static final int ALL_REQUEST_CODE = 0;
    private AppPermissions mRuntimePermission;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;
    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // mRuntimePermission = new AppPermissions(this);

        if (ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfile.this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(UpdateProfile.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }

        }

        init();
        getProfileData(user_id);

    }

    public void getProfileData(String userId) {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateProfile.this);
        progressDialog.setMessage("Loding");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_bar);

        //  http://api.opark.in/index.php/v1/user/profile?userId=96

        String urlData = BASEURL + "user/profile?userId=" + userId;
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

                        LoginModel loginModel = new LoginModel();
                        String Id = jsonObject.getString("userId");
                        String userRole = jsonObject.getString("userRole");
                        String userName = jsonObject.getString("userName");
                        String userContactNo = jsonObject.getString("userContactNo");
                        String userEmail = jsonObject.getString("userEmail");
                        String userDob = jsonObject.getString("userDob");
                        String userAddress = jsonObject.getString("userAddress");
                        String userPhoto = jsonObject.getString("userPhoto");
                        String userGender = jsonObject.getString("userGender");

                        user_Name = new String[2];
                        user_Name = userName.toString().split(" ");

                        if (user_Name.length == 1) {
                            textInputEditFirstName.setText(user_Name[0]);
                        }
                        if (user_Name.length == 2) {
                            textInputEditFirstName.setText(user_Name[0]);
                            textInputTextLastName.setText(user_Name[1]);
                        }

                        if (!userDob.equals("")) {
                            textInputEditDOB.setText(userDob);
                        }

                        textInputEditeAddress.setText(userAddress);
                        textInputEditEmail.setEnabled(false);
                        textInputEditEmail.setText(userEmail);

                        if (userPhoto.equals("")) {
                            Picasso.with(UpdateProfile.this).load(R.drawable.male).placeholder(R.drawable.male).into(userImageUpdate);
                            //userImageUpdate.setImageResource(R.drawable.male);
                        } else {
                            Picasso.with(UpdateProfile.this).load(userPhoto).placeholder(R.drawable.male).into(userImageUpdate);
                            // userImageUpdate.setImageResource(Integer.parseInt(UserPhoto));
                        }
                        if (userGender.equals("Male")) {
                            maleRadio.setChecked(true);
                        }

                        if (userGender.equals("Female")) {
                            femaleRadio.setChecked(true);
                        }
                        //Toast.makeText(UpdateProfile.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        //Toast.makeText(UpdateProfile.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProfile.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Update Profile");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //  overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

            }
        });

        sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("userId", "");
        UserPhoto = sharedPreferences.getString("UserPhoto", "");
        userAddress = sharedPreferences.getString("userAddress", "");
        userName = sharedPreferences.getString("userName", "");
        user_Gender = sharedPreferences.getString("userGender", "");
        UserDob = sharedPreferences.getString("UserDob", "");
        Login_Key = sharedPreferences.getString("Login_Key", "");
        RegisterKey = sharedPreferences.getString("RegisterKey", "");
        userEmail = sharedPreferences.getString("userEmail", "");
        address = sharedPreferences.getString("address", "");
        address = sharedPreferences.getString("city", "");


        UserName = userName.toString().split(" ");


        textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutLastNane = (TextInputLayout) findViewById(R.id.textInputLayoutLastNane);
        // textInputLayoutDOB = (TextInputLayout) findViewById(R.id.textInputLayoutDOB);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

        textInputEditFirstName = (TextInputEditText) findViewById(R.id.textInputEditFirstName);
        textInputTextLastName = (TextInputEditText) findViewById(R.id.textInputTextLastName);
        textInputEditDOB = (TextView) findViewById(R.id.textInputEditDOB);
        textInputEditeAddress = (TextInputEditText) findViewById(R.id.textInputEditeAddress);
        textInputEditEmail = (TextInputEditText) findViewById(R.id.textInputEditEmail);

        textInputEditFirstName.setSelection(textInputEditFirstName.getText().toString().length());
        textInputTextLastName.setSelection(textInputTextLastName.getText().toString().length());
        // textInputEditDOB.setSelection(textInputEditDOB.getText().toString().length());
        textInputEditeAddress.setSelection(textInputEditeAddress.getText().toString().length());
        textInputEditEmail.setSelection(textInputEditEmail.getText().toString().length());


        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        userImageUpdate = (ImageView) findViewById(R.id.userImageUpdate);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
//
//        if (UserName.length == 1) {
//            textInputEditFirstName.setText(UserName[0]);
//        }
//        if (UserName.length == 2) {
//            textInputEditFirstName.setText(UserName[0]);
//            textInputTextLastName.setText(UserName[1]);
//
//        }

        //  userImageUpdate.setImageResource(Integer.parseInt(UserPhoto));
        userImageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    maleRadio.setChecked(true);
                    gender = "Male";

                }

                if (checkedId == R.id.female) {
                    femaleRadio.setChecked(true);
                    gender = "Female";
                }
            }
        });

        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstants.isInternetAvailable(UpdateProfile.this)) {
//                    if (mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
//                        updateProfile();
//                        // Toast.makeText(AddTowVehicle.this, "All permission already given", Toast.LENGTH_SHORT).show();
//                    } else {
//                        mRuntimePermission.requestPermission(UpdateProfile.this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
//                    }
                    updateProfile();
                } else {
                    Toast.makeText(UpdateProfile.this, "Internet is required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myCalender = Calendar.getInstance();
//        mdformat = new SimpleDateFormat("dd/MM/yyyy");
//        String strDate = mdformat.format(myCalender.getTime());
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        textInputEditDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateProfile.this, date, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //  textInputEditDOB.setText(mdformat.format(myCalender.getTime()));

//        textInputEditDOB.setText(UserDob);
//        textInputEditeAddress.setText(address);
//        textInputEditEmail.setText(userEmail);


//        if (UserPhoto.equals("")) {
//            Picasso.with(UpdateProfile.this).load(R.drawable.male).placeholder(R.drawable.male).into(userImageUpdate);
//            //userImageUpdate.setImageResource(R.drawable.male);
//        } else {
//            Picasso.with(UpdateProfile.this).load(UserPhoto).placeholder(R.drawable.male).into(userImageUpdate);
//            // userImageUpdate.setImageResource(Integer.parseInt(UserPhoto));
//        }
//        if (Login_Key.equals("true") || RegisterKey.equals("RegisterKey")) {
//            if (user_Gender.equals("Male")) {
//                maleRadio.setChecked(true);
//            }
//            if (user_Gender.equals("Female")) {
//                femaleRadio.setChecked(true);
//            }
//        }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        mdformat = new SimpleDateFormat(myFormat, Locale.US);

        textInputEditDOB.setText(mdformat.format(myCalender.getTime()));
    }

    public void updateProfile() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASEURL + "user/profile",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String resultResponse = new String(response.data);
                        System.out.println("JSON RETURN  " + response.toString());
                        try {

                            JSONObject result = new JSONObject(resultResponse);
                            int res = result.getInt("status");
                            String respo = result.getString("data");


                            if (res == 0) {

                                // String RESPONSE = result.getString("data");

                                JSONObject result1 = new JSONObject(resultResponse);
                                JSONObject jsonObject = result1.getJSONObject("data");

                                String userId = jsonObject.getString("userId");
                                String UserName = jsonObject.getString("userName");
                                String userContactNo = jsonObject.getString("userContactNo");
                                UserDob = jsonObject.getString("userDob");
                                userAddress = jsonObject.getString("userAddress");
                                UserPhoto = jsonObject.getString("userPhoto");
                                String userGender = jsonObject.getString("userGender");

                                Picasso.with(UpdateProfile.this).load(UserPhoto).placeholder(R.drawable.male).into(userImageUpdate);

                                UserName1 = UserName.toString().split(" ");
                                if (UserName1.length == 1) {
                                    textInputEditFirstName.setText(UserName1[0]);
                                }
                                if (UserName1.length == 2) {
                                    textInputEditFirstName.setText(UserName1[0]);
                                    textInputTextLastName.setText(UserName1[1]);

                                }


                                // AppConstants.setString(UpdateProfile.this, AppConstants.PROFILE_IMAGE, UserPhoto);

                                sharedPreferences = getSharedPreferences("oparkuser", Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();

                                editor.putString("userName", UserName);
                                editor.putString("UserPhoto", UserPhoto);
                                editor.putString("userGender", userGender);
                                editor.putString("UserDob", UserDob);
                                editor.putString("userAddress", userAddress);
                                editor.apply();
                                editor.commit();
                                // Toast.makeText(UpdateProfile.this, message.toString(), Toast.LENGTH_SHORT).show();

                            }
                            //  Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG);
                            pDialog.dismiss();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            //progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            //progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        pDialog.dismiss();
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            pDialog.dismiss();
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            pDialog.dismiss();
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            pDialog.dismiss();
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            pDialog.dismiss();
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {

                //  String user_id = ConstantData.getString(getApplicationContext(),  USER_ID, "");

                java.util.Map<String, String> params = new HashMap<>();
                params.put("userId", user_id);
                params.put("firstName", textInputEditFirstName.getText().toString().trim());
                params.put("lastName", textInputTextLastName.getText().toString().trim());
                params.put("userDob", textInputEditDOB.getText().toString().trim());
                params.put("userAddress", textInputEditeAddress.getText().toString().trim());
                params.put("userGender", gender);
                return params;
            }

            @Override
            protected java.util.Map<String, DataPart> getByteData() {

                if (imageUri != null) {
                    String path = selectedPath;
                    InputStream iStream = null, inputStreamVideo = null;
                    byte[] inputData = null, inputVideoData = null;
                    String uploadId = UUID.randomUUID().toString();
                    try {
                        iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                        //inputStreamVideo = getApplicationContext().getContentResolver().openInputStream(chosenVideoUri);

                        inputData = getBytes(iStream);
                        // inputVideoData = getBytes(inputStreamVideo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                    }
                    java.util.Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("userPhoto", new DataPart(uploadId + ".jpg", inputData, "image/*"));
                    //params.put("video_file", new DataPart(uploadId+".mp4", inputVideoData , VIDEO_TYPE));
                    return params;
                } else {
                    return null;
                }
            }

//                String path = selectedPath;
//                InputStream iStream = null, inputStreamVideo = null;
//                byte[] inputData = null, inputVideoData = null;
//                String uploadId = UUID.randomUUID().toString();
//                try {
//                    iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                    //inputStreamVideo = getApplicationContext().getContentResolver().openInputStream(chosenVideoUri);
//
//                    inputData = getBytes(iStream);
//                    // inputVideoData = getBytes(inputStreamVideo);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                java.util.Map<String, DataPart> params = new HashMap<>();
//                // file name could found file base or direct access from real path
//                // for now just get bitmap data from ImageView
//                params.put("file", new DataPart(uploadId + ".jpg", inputData, "image/*"));
//                //params.put("video_file", new DataPart(uploadId+".mp4", inputVideoData , VIDEO_TYPE));
//
//                return params;
//            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);

    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_library),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = AppConstants.checkPermission(UpdateProfile.this);

                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    userChoosenTask = getResources().getString(R.string.take_photo);
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getResources().getString(R.string.choose_library))) {
                    userChoosenTask = getResources().getString(R.string.choose_library);
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
        //selectedPath = getRealPathFromURI(selectedImageUri);
        imageUri = Uri.fromFile(destination);
        selectedPath = destination.getAbsolutePath();

        // Toast.makeText(getActivity(), destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        userImageUpdate.setImageBitmap(thumbnail);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                //  ConstantData.setString(UserProfileActivity.this, BITIMG, String.valueOf(bm));

                Uri selectedImageUri = data.getData();
                imageUri = data.getData();
                selectedPath = getImageUrlWithAuthority(UpdateProfile.this, selectedImageUri, "post.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userImageUpdate.setImageBitmap(bm);
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri, String fileName) {

        InputStream is = null;

        if (uri.getAuthority() != null) {

            try {

                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);

                return writeToTempImageAndGetPathUri(context, bmp, fileName).toString();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (is != null) {

                        is.close();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage, String fileName) {

        String file_path = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER;
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);

            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {

            Toast.makeText(inContext, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "post.jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

}
