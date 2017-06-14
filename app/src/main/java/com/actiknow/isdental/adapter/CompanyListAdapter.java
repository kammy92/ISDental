package com.actiknow.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.isdental.R;

import com.actiknow.isdental.activity.BrandContactDetailsActivity;
import com.actiknow.isdental.model.Company;
import com.actiknow.isdental.model.StallDetail;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Company> CompanyList = new ArrayList<Company> ();

    public CompanyListAdapter(Activity activity, List<Company> CompanyList) {
        this.activity = activity;
        this.CompanyList = CompanyList;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_brand, parent, false);
        return new ViewHolder (sView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Company company = CompanyList.get (position);
        List<StallDetail> stallDetails = company.getStallDetailList ();

        holder.llStallDetails.removeAllViews ();
        for (int i = 0; i < stallDetails.size (); i++) {
            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams (
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView (activity);
            tv.setTypeface (SetTypeFace.getTypeface (activity));
            tv.setLayoutParams (lparams);
            tv.setText ("Stall : " + stallDetails.get (i).getStall_number ());
            holder.llStallDetails.addView (tv);
        }

        holder.tvBrandName.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvBrandDescription.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvBrandName.setText (company.getBrand_name ());
        if (company.getBrand_description ().length () > 0) {
            holder.rlMain.setBackgroundColor (activity.getResources ().getColor (R.color.text_color_orange));
            holder.tvBrandDescription.setText (company.getBrand_description ());
            holder.tvBrandDescription.setVisibility (View.VISIBLE);
        } else {
            holder.rlMain.setBackgroundColor (activity.getResources ().getColor (R.color.app_background));
            holder.tvBrandDescription.setVisibility (View.GONE);
        }
    }

    @Override
    public int getItemCount () {
        return CompanyList.size ();
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBrandName;
        ImageView ivBrandLogo;
        LinearLayout llStallDetails;
        TextView tvBrandDescription;
        RelativeLayout rlMain;

        public ViewHolder (View view) {
            super (view);
            tvBrandName = (TextView) view.findViewById (R.id.tvBrandName);
            ivBrandLogo = (ImageView) view.findViewById (R.id.ivBrandLogo);
            llStallDetails = (LinearLayout) view.findViewById (R.id.llStallDetails);
            tvBrandDescription = (TextView) view.findViewById (R.id.tvBrandDescription);
            rlMain = (RelativeLayout) view.findViewById (R.id.rlMain);
            view.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {

            Company company = CompanyList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, BrandContactDetailsActivity.class);
            intent.putExtra (AppConfigTags.COMPANY_ID, company.getId ());
            activity.startActivity (intent);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);



        }
    }
}
