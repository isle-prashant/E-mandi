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
 * Created by Prashant Kumar on 27-05-2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    Context context;
    ArrayList<ArrayList<String>> list;
    int lastPosition = -1;

    public UsersAdapter(Context context, ArrayList<ArrayList<String>> objects) {
        this.context = context;
        list = objects;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_3, parent, false);
        // set the view's size, margins, paddings and layout parameters

        UsersAdapter.ViewHolder vh = new UsersAdapter.ViewHolder(v, context);

        return vh;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, final int position) {

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
