package com.twosquares.e_mandi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import static com.twosquares.e_mandi.MainActivity.rowItems;
import static com.twosquares.e_mandi.MainActivity.user;
import static com.twosquares.e_mandi.ProductDetails.markedProducts;

/**
 * Created by PRASHANT on 27-05-2017.
 */

public class UserListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list3);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        /*mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));*/
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setScrollBarSize(0);
        String productId = getIntent().getExtras().getString("productId");
        if (markedProducts.get(productId) == null) {
            findViewById(R.id.noUserMsg).setVisibility(View.VISIBLE);
        }
        RecyclerView.Adapter customAdapter = new CustomAdapter3(this, markedProducts.get(productId));
        mRecyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }
}
