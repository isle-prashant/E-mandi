package com.twosquares.e_mandi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.twosquares.e_mandi.MainActivity.ip;
import static com.twosquares.e_mandi.MainActivity.rowItems;
import static com.twosquares.e_mandi.MainActivity.swipeRefreshLayout;
import static com.twosquares.e_mandi.MainActivity.user;


public class DashboardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static SwipeRefreshLayout swipeRefreshLayoutDashboard;
    public static RecyclerView mRecyclerViewDashboard;
    public static List<RowItem> rowItemList;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(DashboardActivity.this,MainActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(DashboardActivity.this,SellingActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        rowItemList = new ArrayList<RowItem>();
        swipeRefreshLayoutDashboard = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout1);
        swipeRefreshLayoutDashboard.setOnRefreshListener(this);
        mRecyclerViewDashboard = (RecyclerView) findViewById(R.id.item_list1);
        mRecyclerViewDashboard.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        /*mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));*/
        mRecyclerViewDashboard.setLayoutManager(mLayoutManager);
        mRecyclerViewDashboard.setScrollBarSize(0);
        for (int i = 0; i < rowItems.size(); i ++){
            if (rowItems.get(i).getOwner_id().equals(User.userId)) {
                rowItemList.add(rowItems.get(i));
            }
        }
        RecyclerView.Adapter customAdapter = new CustomAdapter2(this, rowItemList);
        mRecyclerViewDashboard.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    @Override
    public void onRefresh() {
        AsyncClass asyncClass = new AsyncClass(DashboardActivity.this, "DashboardViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
