package com.actiknow.isdental.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.isdental.R;

import com.actiknow.isdental.activity.InformationActivity;
import com.actiknow.isdental.model.HomeService;
import com.actiknow.isdental.utils.SetTypeFace;
import com.actiknow.isdental.utils.Utils;
import com.actiknow.isdental.utils.VisitorDetailsPref;
import com.actiknow.isdental.utils.qr_code.QRContents;
import com.actiknow.isdental.utils.qr_code.QREncoder;
import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;


public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<HomeService> homeServiceList = new ArrayList<HomeService> ();

    public HomeServiceAdapter(Activity activity, List<HomeService> homeServiceList) {
        this.activity = activity;
        this.homeServiceList = homeServiceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_home, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final HomeService homeService = homeServiceList.get(position);
        holder.tvServiceName.setTypeface(SetTypeFace.getTypeface(activity));
        holder.tvServiceName.setText(homeService.getService_name ());
        Glide.with(activity).load("").placeholder(homeService.getIcon()).into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return homeServiceList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvServiceName;
        ImageView ivIcon;

        public ViewHolder(View view) {
            super(view);
            tvServiceName = (TextView) view.findViewById(R.id.tvServiceName);
            ivIcon = (ImageView) view.findViewById(R.id.ivServiceIcon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeService homeService = homeServiceList.get(getLayoutPosition());
            switch (homeService.getId()) {

                case 7:
                    Intent intent6 = new Intent (activity, InformationActivity.class);
                    activity.startActivity (intent6);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    }
}
