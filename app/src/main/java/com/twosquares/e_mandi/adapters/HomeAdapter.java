package com.twosquares.e_mandi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twosquares.e_mandi.views.ProductDetails;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.utils.UserLocalStore;
import com.twosquares.e_mandi.datamodels.RowItem;

import java.util.List;

import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.views.MainActivity.rowItems;
import static com.twosquares.e_mandi.views.MainActivity.user;

/**
 * Created by Prashant Kumar on 15-11-2016.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    List<RowItem> rowItem;
    int lastPosition = -1;

    public HomeAdapter(Context context, List objects) {
        this.context = context;
        rowItem = objects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mTitleView, mPriceView, mDescriptionView, mQuantity;
        public ImageView mImageView, mStar;
        public RelativeLayout rl;
        Context mContext;

        public ViewHolder(final View v, final Context mContext) {
            super(v);
            this.mContext = mContext;
            mTitleView = (TextView) v.findViewById(R.id.titleListing);
            mImageView = (ImageView) v.findViewById(R.id.imgThumbnail);
            mDescriptionView = (TextView) v.findViewById(R.id.descriptionListing);
            mPriceView = (TextView) v.findViewById(R.id.priceListing);
            mStar = (ImageView) v.findViewById(R.id.icon_star);
            mQuantity = (TextView) v.findViewById(R.id.quantityListing);
            mStar.setOnClickListener(this);
            mImageView.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        public void clearAnimation() {

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mStar.getId()) {
                UserLocalStore userLocalStore= new UserLocalStore(mContext);
                RowItem Item = rowItem.get(getAdapterPosition());
                Item.setImportant(!Item.isImportant());
                userLocalStore.setStar(Item.getImage_id(),Item.getImage_id(),Item.getOwner_id());
                rowItem.set(getAdapterPosition(), Item);
                HomeAdapter.this.notifyDataSetChanged();

            }
            else{
                Intent intent = new Intent(mContext, ProductDetails.class);
                Log.e("position", "" + getAdapterPosition());
                intent.putExtra("rowItem", rowItem.get(getAdapterPosition()));
                intent.putExtra("Adapter","HomeAdapter");
                System.out.println("selected row " + rowItem.get(getAdapterPosition()).getImage_id());
                mContext.startActivity(intent);
            }
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, context);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.mTitleView.setText(rowItem.get(position).getTitle());
        holder.mPriceView.setText("₹ " + rowItem.get(position).getPrice());
        holder.mDescriptionView.setText(rowItem.get(position).getDescription());
        holder.mQuantity.setText(rowItem.get(position).getQuantity()+" Kg(s)");
//        Picasso.with(context).setLoggingEnabled(true);
        if (rowItems.get(position).getOwner_id().equals(user.userId)){
            holder.mStar.setVisibility(View.INVISIBLE);
        }
        else {
            holder.mStar.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load("http://" + ip + "/images/reduced/" + rowItem.get(position).getImage_id() + ".jpg").into(holder.mImageView);
        applyImportant(holder, rowItem.get(position));
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return rowItem.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            animation.setDuration(100 * position);
            viewToAnimate.startAnimation(animation);
         /*   ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(100 * position);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);*/
            lastPosition = position;
        }

    }

    private void applyImportant(ViewHolder holder, RowItem rowItem) {
        if (rowItem.isImportant()) {
            holder.mStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_black_24dp));
            holder.mStar.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_selected));
        } else {
            holder.mStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp));
            holder.mStar.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_normal));
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }
}
