package com.twosquares.e_mandi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twosquares.e_mandi.views.ProductDetails;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.datamodels.RowItem;

import java.util.List;

import static com.twosquares.e_mandi.views.MainActivity.ip;

/**
 * Created by PRASHANT on 22-05-2017.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    Context context;
    List<RowItem> rowItem;
    int lastPosition = -1;

    public DashboardAdapter(Context context, List objects) {
        this.context = context;
        rowItem = objects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTitleView, mPriceView, mDescriptionView;
        public ImageView mImageView;
        public RelativeLayout rl;
        Context mContext;

        public ViewHolder(final View v, final Context mContext) {
            super(v);
            this.mContext = mContext;
            mTitleView = (TextView) v.findViewById(R.id.titleDashboard);
            mImageView = (ImageView) v.findViewById(R.id.imgThumbnailDashboard);
            mDescriptionView = (TextView) v.findViewById(R.id.descriptionDashboard);
            mPriceView = (TextView) v.findViewById(R.id.priceDashboard);
            mImageView.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        public void clearAnimation() {

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, ProductDetails.class);
            Log.e("position", "" + getAdapterPosition());
            intent.putExtra("rowItem", rowItem.get(getAdapterPosition()));
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("Adapter", "DashboardAdapter");
            System.out.println("selected row " + rowItem.get(getAdapterPosition()).getImage_id());
            mContext.startActivity(intent);
        }
    }


    /*@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_item,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgThumbnail);
        TextView tv = (TextView) convertView.findViewById(R.id.txt);
        Picasso.with(context).load("http://i.imgur.com/" + rowItem.get(position) + "s.jpg").into(imageView);
        return convertView;
    }*/

    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_2, parent, false);
        // set the view's size, margins, paddings and layout parameters

        DashboardAdapter.ViewHolder vh = new DashboardAdapter.ViewHolder(v, context);

        return vh;
    }

    @Override
    public void onBindViewHolder(DashboardAdapter.ViewHolder holder, final int position) {


        holder.mTitleView.setText(rowItem.get(position).getTitle());
        holder.mPriceView.setText("₹ " + rowItem.get(position).getPrice());
        holder.mDescriptionView.setText(rowItem.get(position).getDescription());
//        Picasso.with(context).setLoggingEnabled(true);


        Picasso.with(context).load("http://" + ip + "/images/thumbnails/" + rowItem.get(position).getImage_id() + ".jpg").into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return rowItem.size();
    }


}
