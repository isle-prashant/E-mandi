package com.twosquares.e_mandi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SellingActivity extends AppCompatActivity {

    Button btpic, btnup, btPostAd;
    ImageView imPreview, imageView;
    int img = 0;
    Uri m;
    static String encodedImage;
    static String filePath;
    public static String ip;
    public EditText priceTxt, descTxt, locationTxt, contactTxt, titleTxt;
    public static LinearLayout initialLayout, laterLayout;
    public static String image_id = null;

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
        contactTxt = (EditText) findViewById(R.id.editContact);
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
                String price = String.valueOf(priceTxt.getText());
                String location = String.valueOf(locationTxt.getText());
                String Description = String.valueOf(descTxt.getText());
                String contact = String.valueOf(contactTxt.getText());
                String Title = String.valueOf(titleTxt.getText());
                AsyncClass asyncClass = new AsyncClass(SellingActivity.this, "UploadData");
                asyncClass.execute("http://" + ip + "/image.php",encodedImage,price, location, Description, contact, Title);

            }
        });
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
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                System.out.println(selectedImage);
                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                    grabimage(selectedImage, imPreview);
                } else {
                    //NOT IN REQUIRED FORMAT
                    Toast.makeText(this, "Cannot Open the file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    static Bitmap bitmap;

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

           /* btpic.setEnabled(false);
            btnup.setEnabled(false);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.imageUploadProgress);
            TextView progressText = (TextView) findViewById(R.id.imageUploadText);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);*/
            initialLayout.setVisibility(View.GONE);
            laterLayout.setVisibility(View.VISIBLE);
           /* new UploadImage().execute("https://api.imgur.com/3/image");
            System.out.println("calling");*/

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



}
