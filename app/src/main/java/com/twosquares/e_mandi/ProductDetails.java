package com.twosquares.e_mandi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.twosquares.e_mandi.MainActivity.ip;
import static com.twosquares.e_mandi.MainActivity.user;
import static com.twosquares.e_mandi.MyApplication.userLocalStore;
import static com.twosquares.e_mandi.UserLocalStore.SP_NAME;

public class ProductDetails extends AppCompatActivity {
    public static Map<String, ArrayList<ArrayList<String>>> markedProducts = new HashMap<String, ArrayList<ArrayList<String>>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_product_details);
        SharedPreferences sp = this.getSharedPreferences(SP_NAME, 0);
        ImageView iv = (ImageView) findViewById(R.id.fullImage);
        TextView Title = (TextView) findViewById(R.id.detailTitle);
        TextView Price = (TextView) findViewById(R.id.detailPrice);
        TextView Description = (TextView) findViewById(R.id.detailDescription);
        TextView Location = (TextView) findViewById(R.id.detailLocation);
        TextView Contact = (TextView) findViewById(R.id.detailContact);
        TextView Quantity = (TextView) findViewById(R.id.detailQuantity);
        Button btnDeletePost = (Button) findViewById(R.id.btnDeltePost);
        Button btnMarkedBy = (Button) findViewById(R.id.markedBy);
        Button btnEditPost = (Button) findViewById(R.id.btnEditPost);
//        String image_id = getIntent().getExtra("image_id");
        final RowItem rowItem = (RowItem) getIntent().getExtras().getSerializable("rowItem");
        if (rowItem.getOwner_id().equals(sp.getString("userId","")) && getIntent().getExtras().getString("Adapter").equals("CustomAdapter2")){
            findViewById(R.id.optionLayout).setVisibility(View.VISIBLE);
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
                asyncClass.execute("http://" + ip + "/deletePost.php", rowItem.getImage_id());
            }
        });
        btnMarkedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, UserListActivity.class);
                intent.putExtra("productId", rowItem.getImage_id());
                startActivity(intent);
            }
        });
        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, EditActivity.class);
                intent.putExtra("rowItem", rowItem);
                startActivity(intent);
                finish();
            }
        });
        markedProducts = userLocalStore.getStarProducts();
        Log.e("Current Product", "" + markedProducts.get(rowItem.getImage_id()));
    }

}
//, String.valueOf(getIntent().getExtras().getInt("position"))