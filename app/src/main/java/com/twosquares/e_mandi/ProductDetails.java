package com.twosquares.e_mandi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.twosquares.e_mandi.MainActivity.ip;

public class ProductDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_product_details);
        ImageView iv = (ImageView) findViewById(R.id.fullImage);
        TextView Title = (TextView) findViewById(R.id.detailTitle);
        TextView Price = (TextView) findViewById(R.id.detailPrice);
        TextView Description = (TextView) findViewById(R.id.detailDescription);
        TextView Location = (TextView) findViewById(R.id.detailLocation);
        TextView Contact = (TextView) findViewById(R.id.detailContact);
//        String image_id = getIntent().getExtra("image_id");
        RowItem rowItem = (RowItem) getIntent().getExtras().getSerializable("position");
        Picasso.with(this).load("http://"+ip+"/images/reduced/" +  rowItem.getImage_id() + ".jpg").into(iv);
        Title.setText(rowItem.getTitle());
        Price.setText("₹ " + rowItem.getPrice());
        Description.setText(rowItem.getDescription());
        Location.setText(rowItem.getLocation());
        Contact.setText(rowItem.getContact());
    }

}
