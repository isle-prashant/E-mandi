package com.twosquares.e_mandi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twosquares.e_mandi.adapters.HomeAdapter;
import com.twosquares.e_mandi.adapters.DashboardAdapter;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;
import com.twosquares.e_mandi.views.DashboardActivity;
import com.twosquares.e_mandi.views.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.twosquares.e_mandi.views.DashboardActivity.mRecyclerViewDashboard;
import static com.twosquares.e_mandi.views.DashboardActivity.rowItemList;
import static com.twosquares.e_mandi.views.DashboardActivity.swipeRefreshLayoutDashboard;
import static com.twosquares.e_mandi.views.MainActivity.swipeRefreshLayout;
import static com.twosquares.e_mandi.views.SellingActivity.initialLayout;
import static com.twosquares.e_mandi.views.SellingActivity.laterLayout;
import static com.twosquares.e_mandi.datamodels.User.stars;

/**
 * Created by Prashant Kumar on 27-04-2017.
 */

public class AsyncClass extends AsyncTask<String, Void, Void> {
    private final OkHttpClient client = new OkHttpClient();
    public RowItem item;
    RecyclerView mRecyclerView = MainActivity.mRecyclerView;
    List<RowItem> rowItems = MainActivity.rowItems;
    Context context;
    String action;
    ProgressDialog dialog;
    int resCode = 0;
    String deletedId;


    public AsyncClass(Context context, String action) {
        this.context = context;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (action == "ViewLoader"){
            rowItems.clear();
            swipeRefreshLayout.setRefreshing(true);
        }
        if (action == "UploadData" || action == "EditPost") {

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Posting Your Ad. \nHang in There");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        if (action == "DashboardViewLoader"){
            rowItems.clear();
            rowItemList.clear();
            swipeRefreshLayoutDashboard.setRefreshing(true);
        }
        if (action == "DeletePost"){
            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Deleting data  \nPlease Wait");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    protected Void doInBackground(String... strings) {
        if (action.equals("ViewLoader") || action.equals("DashboardViewLoader")) {
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();
            Response response;
            String res = null;
            JSONObject jobj;
            try {
                response = client.newCall(request).execute();
                Log.e("Address", strings[0]);
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


        }
        /*if (action == "UploadData"){
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("image", strings[1])
                    .add("price", strings[2])
                    .add("location", strings[3])
                    .add("description", strings[4])
                    .add("phoneNo", strings[5])
                    .add("title", strings[6])
                    .add("userId", User.userId)
                    .add("quantity", strings[7])
                    .build();


            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                } else {
                    resCode = 200;
                    System.out.println(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }*/
        if (action == "Star"){
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("userId", strings[1])
                    .add("imageId", strings[2])
                    .add("ownerId", strings[3])
                    .add("userName", strings[4])
                    .build();

            Log.e("Push user", strings[4]);
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                } else {
                    resCode = 200;
                    System.out.println(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if (action == "DeletePost"){
            Response response = null;

            RequestBody requestBody = new FormBody.Builder()
                    .add("imageId", strings[1])
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                } else {
                    resCode = 200;
                    System.out.println(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if (action == "EditPost") {
            Log.e("Address", strings[0]);
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("price", strings[1])
                    .add("location", strings[2])
                    .add("description", strings[3])
                    .add("title", strings[4])
                    .add("quantity", strings[5])
                    .add("id", strings[6])
                    .build();


            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                } else {
                    resCode = 200;
                    System.out.println(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (action == "ViewLoader") {

            AnimationSet set = new AnimationSet(true);
            Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(400);
            fadeIn.setFillAfter(true);
            set.addAnimation(fadeIn);
            RecyclerView.Adapter homeAdapter = new HomeAdapter(context, rowItems);

            MainActivity.mRecyclerView.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        /*    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View childView, int position) {
                    Log.e("size of rowItems after", "" + rowItems.size());
                    Log.e("zeroth element", rowItems.get(0).getImage_id());
                    Intent intent = new Intent(context, ProductDetails.class);
                    Log.e("position", "" + position);
                    intent.putExtra("position", rowItems.get(position));
                    System.out.println("selected row " + rowItems.get(position).getImage_id());
                    context.startActivity(intent);
                }

                @Override
                public void onItemLongPress(View childView, int position) {

                }
            }));*/
        }
        if (action == "DashboardViewLoader"){
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
        if (action == "UploadData") {
            if (dialog != null)
                dialog.dismiss();
            if (resCode == 200) {
                Intent i = new Intent(context,MainActivity.class);
                ((Activity) context).finish();
                context.startActivity(i);

            }
            else {
                Toast.makeText(context,"Something went Wronng",Toast.LENGTH_SHORT);
                initialLayout.setVisibility(View.VISIBLE);
                laterLayout.setVisibility(View.GONE);
            }
        }
        if (action == "EditPost") {
            if (dialog != null)
                dialog.dismiss();
            if (resCode == 200) {
                Intent i = new Intent(context, DashboardActivity.class);
                ((Activity) context).finish();
                context.startActivity(i);
            }
        }
        if (action == "DeletePost"){
            if (dialog != null)
                dialog.dismiss();
            if (resCode == 200) {
                ((Activity) context).finish();
            }
        }
    }

}
