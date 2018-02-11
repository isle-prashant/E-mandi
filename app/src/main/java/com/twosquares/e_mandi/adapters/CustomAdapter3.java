package com.twosquares.e_mandi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twosquares.e_mandi.R;

import java.util.ArrayList;

/**
 * Created by PRASHANT on 27-05-2017.
 */

public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.ViewHolder> {
    Context context;
    ArrayList<ArrayList<String>> list;
    int lastPosition = -1;

    public CustomAdapter3(Context context, ArrayList<ArrayList<String>> objects) {
        this.context = context;
        list = objects;
    }

    @Override
    public CustomAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_3, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CustomAdapter3.ViewHolder vh = new CustomAdapter3.ViewHolder(v, context);

        return vh;
    }

    @Override
    public void onBindViewHolder(CustomAdapter3.ViewHolder holder, final int position) {

        holder.mUserName.setText(list.get(position).get(1));
        holder.mUserPhoneNo.setText(list.get(position).get(3));
        holder.mUserEmail.setText(list.get(position).get(2));


    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mUserName, mUserPhoneNo, mUserEmail;

        Context mContext;

        public ViewHolder(final View v, final Context mContext) {
            super(v);
            this.mContext = mContext;
            mUserName = (TextView) v.findViewById(R.id.userName);
            mUserPhoneNo = (TextView) v.findViewById(R.id.userPhone);
            mUserEmail = (TextView) v.findViewById(R.id.userEmail);

        }

    }

}
