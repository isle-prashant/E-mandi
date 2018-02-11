package com.twosquares.e_mandi.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;
import com.twosquares.e_mandi.utils.AsyncClass;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;

import static com.twosquares.e_mandi.views.MainActivity.ip;

/**
 * Created by PRASHANT on 28-05-2017.
 */

public class EditActivity extends AppCompatActivity {

    public static LinearLayout initialLayout, laterLayout;
    public EditText priceTxt, descTxt, locationTxt, quantityTxt, titleTxt;
    Button btPostAd;
    ImageView imageView;
    int PLACE_REQUEST = 2;
    String price, location, Description, contact, Title, quantity;
    RowItem rowItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imageView = (ImageView) findViewById(R.id.Image);
        btPostAd = (Button) findViewById(R.id.btnPostAd);
        btPostAd.setText("Update");
        priceTxt = (EditText) findViewById(R.id.editPrice);
        descTxt = (EditText) findViewById(R.id.editDescription);
        locationTxt = (EditText) findViewById(R.id.editLocation);
        quantityTxt = (EditText) findViewById(R.id.editQuantity);
        ImageView ivLocation = (ImageView) findViewById(R.id.ivLocation);
        titleTxt = (EditText) findViewById(R.id.editTitle);
        update();
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(EditActivity.this);
                    startActivityForResult(intent, PLACE_REQUEST);


                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        btPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intialize();
                if (validate()) {
                    contact = User.phoneNo;
                    AsyncClass asyncClass = new AsyncClass(EditActivity.this, "EditPost");
                    asyncClass.execute("http://" + ip + "/updatePost.php", price, location, Description, Title, quantity, rowItem.getImage_id());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Log.e("Place : ", "" + place.getLatLng());
                String address = String.format("" + place.getAddress());
                locationTxt.setText(address);
            }
        }
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

    public void update() {
        rowItem = (RowItem) getIntent().getExtras().getSerializable("rowItem");
        titleTxt.setText(rowItem.getTitle());
        priceTxt.setText(rowItem.getPrice());
        descTxt.setText(rowItem.getDescription());
        locationTxt.setText(rowItem.getLocation());
        quantityTxt.setText(rowItem.getQuantity());
        Picasso.with(this).load("http://" + MainActivity.ip + "/images/reduced/" + rowItem.getImage_id() + ".jpg").into(imageView);
    }
}
