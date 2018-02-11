package com.twosquares.e_mandi.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.twosquares.e_mandi.utils.RequestBuilder;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.datamodels.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SellingActivity extends AppCompatActivity {

    public static LinearLayout initialLayout, laterLayout;
    public static String image_id = null;
    static String encodedImage;
    static String filePath;
    static Bitmap bitmap;
    public EditText priceTxt, descTxt, locationTxt, quantityTxt, titleTxt;
    Button btpic, btnup, btPostAd;
    ImageView imPreview, imageView;
    int img = 0;
    Uri m;
    String ip;
    int PLACE_REQUEST = 2;
    String price, location, Description, contact, Title, quantity;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(SellingActivity.this,MainActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(SellingActivity.this,DashboardActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        ip = getString(R.string.ip);
        System.out.println(ip);

        imPreview = (ImageView) findViewById(R.id.Imageprev);
        imageView = (ImageView) findViewById(R.id.Image);
        initialLayout = (LinearLayout) findViewById(R.id.initialLayout);
        laterLayout = (LinearLayout) findViewById(R.id.laterLayout);
        btpic = (Button) findViewById(R.id.click);
        btnup = (Button) findViewById(R.id.pick);
        btPostAd = (Button) findViewById(R.id.btnPostAd);
        priceTxt = (EditText) findViewById(R.id.editPrice);
        descTxt = (EditText) findViewById(R.id.editDescription);
        locationTxt = (EditText) findViewById(R.id.editLocation);
        quantityTxt = (EditText) findViewById(R.id.editQuantity);
        ImageView ivLocation = (ImageView) findViewById(R.id.ivLocation);
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(SellingActivity.this);
                    startActivityForResult(intent, PLACE_REQUEST);


                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        titleTxt = (EditText) findViewById(R.id.editTitle);
        btpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photoclickink();
            }
        });
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        btPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intialize();
                if (validate()) {
                    contact = User.phoneNo;
                    RequestBuilder requestBuilder = new RequestBuilder();
                    HashMap<String,String> params = new HashMap<>();
                    params.put("image",encodedImage);
                    params.put("price",price);
                    params.put("location", location);
                    params.put("description", Description);
                    params.put("phoneNo",contact);
                    params.put("title", Title);
                    params.put("userId", User.userId);
                    params.put("quantity", quantity);
                    Request request = requestBuilder.createPostRequest("http://" + ip + "/image.php", params);
                    new UploadAsync(SellingActivity.this).execute(request);
//                    AsyncClass asyncClass = new AsyncClass(SellingActivity.this, "UploadData");
//                    asyncClass.execute("http://" + ip + "/image.php", encodedImage, price, location, Description, contact, Title, quantity);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public File createTemporaryFile() throws Exception {
        File tempFile = new File(getExternalFilesDir(null), "picture.jpg");
        return tempFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //To click an image
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                grabimage(m, imPreview);
            }
        }
        //To pick an image
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                filePath = getPath(selectedImage);
                grabimage(selectedImage, imPreview);
            }
        }

        if (requestCode == PLACE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Log.e("Place : ", ""+place.getLatLng());
                String address = String.format(""+place.getAddress());
                locationTxt.setText(address);
            }
        }
    }

    public void grabimage(Uri uri, ImageView imageView) {
        if (uri != null) {


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                byte[] b = bao.toByteArray();

                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                imageView.setImageBitmap(bitmap);
                this.imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initialLayout.setVisibility(View.GONE);
            laterLayout.setVisibility(View.VISIBLE);

        } else
            Toast.makeText(this, "Something went Wrong. Please try Again", Toast.LENGTH_SHORT).show();


    }


    public void Photoclickink() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = null;
        try {
            photo = File.createTempFile("picture",".jpg",getExternalFilesDir(null));
            photo.deleteOnExit();
            System.out.println("file name" + photo);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            photo.delete();
        }


        if (photo != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                m = FileProvider.getUriForFile(this, "com.example.android.fileProvider", photo);
            } else {
                m = Uri.fromFile(photo);
            }
        }
        i.putExtra(MediaStore.EXTRA_OUTPUT, m);
        startActivityForResult(i, img);
        File file = new File(getExternalFilesDir(null), "picture.jpg");
        if (file != null){
            Log.e("File", "deleted");
            file.delete();
        }
        photo.delete();
    }


    public boolean validate() {
        boolean valid = true;
        if (Title.isEmpty() || Title.length() > 20) {
            titleTxt.setError("please enter valid title");
            valid = false;
        }
        if (price.isEmpty() || price.length() > 10) {
            priceTxt.setError("please enter valid price");
            valid = false;
        }
        if (Description.isEmpty() || Description.length() > 140) {
            descTxt.setError("please enter description");
            valid = false;
        }
        if (quantity.isEmpty() || quantity.length() > 4) {
            descTxt.setError("please enter a valid quantity");
            valid = false;
        }
        if (location.isEmpty() || location.length() > 200) {
            locationTxt.setError("please chose your location");
            valid = false;
        }
        return valid;
    }

    public void intialize() {
        Title = titleTxt.getText().toString().trim();
        price = priceTxt.getText().toString().trim();
        location = locationTxt.getText().toString().trim();
        Description = descTxt.getText().toString().trim();
        quantity = String.valueOf(quantityTxt.getText());

    }

    class UploadAsync extends AsyncTask<Request, Void, Void>{
        int resCode = 0;
        private final OkHttpClient client = new OkHttpClient();
        ProgressDialog dialog;
        Context context;
        public UploadAsync(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Posting Your Ad. \nHang in There");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Request... requests) {
            Response response = null;
            try {
                response = client.newCall(requests[0]).execute();
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                } else {
                    resCode = 200;
                    System.out.println(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null)
                dialog.dismiss();
            if (resCode == 200) {
                Intent i = new Intent(context,MainActivity.class);
                ((Activity) context).finish();
                context.startActivity(i);

            }
        }
    }
}
