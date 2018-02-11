package com.twosquares.e_mandi.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.twosquares.e_mandi.utils.AsyncClass;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.adapters.DashboardAdapter;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;
import com.twosquares.e_mandi.utils.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.twosquares.e_mandi.datamodels.User.stars;
import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.views.MainActivity.rowItems;
import static com.twosquares.e_mandi.views.MainActivity.swipeRefreshLayout;


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
        RecyclerView.Adapter customAdapter = new DashboardAdapter(this, rowItemList);
        mRecyclerViewDashboard.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    @Override
    public void onRefresh() {
        RequestBuilder requestBuilder = new RequestBuilder();
        Request request = requestBuilder.createGetRequest("http://"+ip+"/index.json", null);
//        AsyncClass asyncClass = new AsyncClass(DashboardActivity.this, "DashboardViewLoader");
//        asyncClass.execute("http://"+ip+"/index.json");
        new DashboardAsync(DashboardActivity.this).execute(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class DashboardAsync extends AsyncTask<Request, Void, Void> {
        int resCode = 0;
        private final OkHttpClient client = new OkHttpClient();
        ProgressDialog dialog;
        Context context;
        public DashboardAsync(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            rowItems.clear();
            rowItemList.clear();
            swipeRefreshLayoutDashboard.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Request... requests) {
            Response response;
            String res = null;
            JSONObject jobj;
            try {
                response = client.newCall(requests[0]).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                res = response.body().string();
                System.out.println(res);
                JSONArray arr = new JSONArray(res);
                for (int i = 0; i < arr.length(); i++) {
                    jobj = arr.getJSONObject(i);
                    Gson gson = new Gson();
                    RowItem newItem = gson.fromJson(jobj.toString(), RowItem.class);
                    Boolean star = false;
                    if (stars.contains(jobj.getString("image_id"))){
                        star = true;
                    }
                    newItem.setImportant(star);

                    rowItems.add(newItem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException je) {
                je.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < rowItems.size(); i ++){
                if (rowItems.get(i).getOwner_id().equals(User.userId)) {
                    rowItemList.add(rowItems.get(i));
                }
            }
            RecyclerView.Adapter dashboardAdapter = new DashboardAdapter(context, rowItemList);
            mRecyclerViewDashboard.setAdapter(dashboardAdapter);
            dashboardAdapter.notifyDataSetChanged();
            swipeRefreshLayoutDashboard.setRefreshing(false);
        }
    }
}
