package com.twosquares.e_mandi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.twosquares.e_mandi.MainActivity.ip;
import static com.twosquares.e_mandi.MainActivity.user;

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
        TextView Quantity = (TextView) findViewById(R.id.detailQuantity);
        Button btnDeletePost = (Button) findViewById(R.id.btnDeltePost);
//        String image_id = getIntent().getExtra("image_id");
        final RowItem rowItem = (RowItem) getIntent().getExtras().getSerializable("rowItem");
        if (rowItem.getOwner_id().equals(user.userId) && getIntent().getExtras().getString("Adapter").equals("CustomAdapter2")){
            btnDeletePost.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load("http://"+ip+"/images/reduced/" +  rowItem.getImage_id() + ".jpg").into(iv);
        Title.setText(rowItem.getTitle());
        Price.setText("₹ " + rowItem.getPrice());
        Description.setText(rowItem.getDescription());
        Location.setText(rowItem.getLocation());
        Contact.setText(rowItem.getContact());
        Quantity.setText(rowItem.getQuantity() + " Kg(s)");
        btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncClass asyncClass = new AsyncClass(ProductDetails.this,"DeletePost");
                asyncClass.execute("http://" + ip + "/deletePost.php", rowItem.getImage_id(), String.valueOf(getIntent().getExtras().getInt("position")));
            }
        });
    }

}
