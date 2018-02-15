package com.twosquares.e_mandi.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.pushbots.push.Pushbots;
import com.twosquares.e_mandi.adapters.HomeAdapter;
import com.twosquares.e_mandi.managers.HomeManager;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.utils.UserLocalStore;
import com.twosquares.e_mandi.utils.broadcastHandler;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static String ip;
    public static List <RowItem> rowItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static boolean openSplash = true;
    public static User user;
    public RowItem item;
    UserLocalStore userLocalStore;
    HomeAdapter homeAdapter;
    HomeManager homeManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(MainActivity.this,SellingActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       splash screen definition
        if (openSplash) {
            openSplash = false;
            startActivity(new Intent(this, SplashScreen.class));
        }
//      logged in user data from shared preference
        userLocalStore= new UserLocalStore(this);
        if (authenticate()){
            user = userLocalStore.getLoggedinUser();
        }
        else {
            startActivity(new Intent(this, UserLogin.class));
        }

        initPushbots();
        homeManager = new HomeManager(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ip = getString(R.string.ip);
        rowItems =  new ArrayList<RowItem>();
        rowItems.clear();
        isStoragePermissionGranted();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setScrollBarSize(0);
        homeAdapter = new HomeAdapter(this, rowItems);
        mRecyclerView.setAdapter(homeAdapter);
        swipeRefreshLayout.setRefreshing(true);
        homeManager.getHomeData();
//        AsyncClass asyncClass = new AsyncClass(this, "ViewLoader");
//        asyncClass.execute("http://"+ip+"/index.json");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private void initPushbots(){
        Pushbots.sharedInstance().registerForRemoteNotifications();
        Pushbots.sharedInstance().setCustomHandler(broadcastHandler.class);

    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    //permissions

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permissions", "Permission is granted");
            } else {

                Log.v("Permissions", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permissions", "Permission is granted");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resumed","true");
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        homeManager.getHomeData();
//        AsyncClass asyncClass = new AsyncClass(MainActivity.this, "ViewLoader");
//        asyncClass.execute("http://"+ip+"/index.json");
    }

    public void onResponse(Boolean status, String message){
        if (status){
            homeAdapter.notifyDataSetChanged();
        } else {
            //TODO handle error
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
/*
https://192.168.0.150/emandi.php*/
