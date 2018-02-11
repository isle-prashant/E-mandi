package com.twosquares.e_mandi.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.adapters.UsersAdapter;

import static com.twosquares.e_mandi.views.ProductDetails.markedProducts;

/**
 * Created by Prashant Kumar on 27-05-2017.
 */

public class UserListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.item_list3);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        /*mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));*/
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setScrollBarSize(0);
        String productId = getIntent().getExtras().getString("productId");
        if (markedProducts.get(productId) == null) {
            findViewById(R.id.noUserMsg).setVisibility(View.VISIBLE);
        }
        RecyclerView.Adapter usersAdapter = new UsersAdapter(this, markedProducts.get(productId));
        mRecyclerView.setAdapter(usersAdapter);
        usersAdapter.notifyDataSetChanged();
    }
}
