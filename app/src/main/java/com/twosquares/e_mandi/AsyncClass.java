package com.twosquares.e_mandi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import static com.twosquares.e_mandi.DashboardActivity.mRecyclerViewDashboard;
import static com.twosquares.e_mandi.DashboardActivity.rowItemList;
import static com.twosquares.e_mandi.DashboardActivity.swipeRefreshLayoutDashboard;
import static com.twosquares.e_mandi.MainActivity.rowItems;
import static com.twosquares.e_mandi.MainActivity.swipeRefreshLayout;
import static com.twosquares.e_mandi.MainActivity.user;
import static com.twosquares.e_mandi.SellingActivity.initialLayout;
import static com.twosquares.e_mandi.SellingActivity.laterLayout;
import static com.twosquares.e_mandi.User.stars;

/**
 * Created by PRASHANT on 27-04-2017.
 */

public class AsyncClass extends AsyncTask<String, Void, Void> {
    RecyclerView mRecyclerView = MainActivity.mRecyclerView;
    List<RowItem> rowItems = MainActivity.rowItems;
    Context context;
    String action;
    public RowItem item;
    ProgressDialog dialog;
    int resCode = 0;

    AsyncClass(Context context, String action) {
        this.context = context;
        this.action = action;
    }


    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (action == "ViewLoader"){
            rowItems.clear();
            swipeRefreshLayout.setRefreshing(true);
        }
        if (action == "UploadData") {

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
    }

    @Override
    protected Void doInBackground(String... strings) {
        if (action == "ViewLoader" || action == "DashboardViewLoader") {
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
                    String star = "false";
                    if (stars.contains(jobj.getString("image_id"))){
                        star = "true";
                    }
                    String items[] = new String[]{jobj.getString("title"), jobj.getString("image_id"), jobj.getString("price"), jobj.getString("phoneNo"), jobj.getString("location"), jobj.getString("description"), star, jobj.getString("ownerId"), jobj.getString("quantity")};

                    String image_id = jobj.getString("image_id");
                    item = new RowItem(items);

                    rowItems.add(item);
                    Log.e("ImageId", rowItems.get(i).getImage_id());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (action == "UploadData"){
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("image", strings[1])
                    .add("price", strings[2])
                    .add("location", strings[3])
                    .add("description", strings[4])
                    .add("phoneNo", strings[5])
                    .add("title", strings[6])
                    .add("userId", user.userId)
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
        }
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
//            Animation slideUp = new TranslateAnimation(0, 0, ViewUtils.getScreenHeight(context),0);
            RecyclerView.Adapter customAdapter = new CustomAdapter(context, rowItems);

            MainActivity.mRecyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
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
                if (rowItems.get(i).getOwner_id().equals(user.userId)){
                    rowItemList.add(rowItems.get(i));
                }
            }
            RecyclerView.Adapter customAdapter2 = new CustomAdapter2(context, rowItemList);
            mRecyclerViewDashboard.setAdapter(customAdapter2);
            customAdapter2.notifyDataSetChanged();
            swipeRefreshLayoutDashboard.setRefreshing(false);
        }
        if (action == "UploadData"){
            if (dialog != null)
                dialog.dismiss();
            if (resCode == 200) {
                Intent i = new Intent(context,MainActivity.class);
                ((Activity) context).finish();
                ((Activity) context).startActivity(i);

            }
            else {
                Toast.makeText(context,"Something went Wronng",Toast.LENGTH_SHORT);
                initialLayout.setVisibility(View.VISIBLE);
                laterLayout.setVisibility(View.GONE);
            }
        }
    }

}
