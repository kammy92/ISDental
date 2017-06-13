package com.actiknow.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actiknow.isdental.R;

import com.actiknow.isdental.model.ExhibitorContactDetail;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class ExhibitorContactAdapter extends RecyclerView.Adapter<ExhibitorContactAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<ExhibitorContactDetail> exhibitorContactDetails = new ArrayList<ExhibitorContactDetail>();

    public ExhibitorContactAdapter(Activity activity, List<ExhibitorContactDetail> exhibitorContactDetails) {
        this.activity = activity;
        this.exhibitorContactDetails = exhibitorContactDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_exhivitor_detail, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final ExhibitorContactDetail exhibitor = exhibitorContactDetails.get(position);


        holder.tvContactPerson.setTypeface(SetTypeFace.getTypeface(activity));
        holder.tvEmail.setTypeface(SetTypeFace.getTypeface(activity));
        holder.tvWebsite.setTypeface(SetTypeFace.getTypeface(activity));
        holder.tvFullAddress.setTypeface(SetTypeFace.getTypeface(activity));

        holder.tvContactPerson.setText(exhibitor.getContact_person());
        holder.tvEmail.setText(exhibitor.getEmail());
        holder.tvWebsite.setText(exhibitor.getWebsite());
        holder.tvFullAddress.setText(exhibitor.getAddress());


        for (int i = 0; i < exhibitor.getContactList().size(); i++) {
            final ArrayList<String> contactList2 = exhibitor.getContactList();
            TextView tv = new TextView(activity);
            tv.setText(Html.fromHtml("<u><font color='blue'>" + contactList2.get(i) + "</font></u>"), TextView.BufferType.SPANNABLE);
            tv.setTextSize(16);
            tv.setPadding(0, 5, 0, 5);
            tv.setTypeface(SetTypeFace.getTypeface(activity, Constants.font_name));
            tv.setTextColor(activity.getResources().getColor(R.color.app_text_color_dark));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactList2.get(finalI)));
                    sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(sIntent);
                }
            });
            holder.llPhone.addView(tv);
        }
    }




    @Override
    public int getItemCount() {
        return exhibitorContactDetails.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactPerson;
        LinearLayout llPhone;
        TextView tvEmail;
        TextView tvWebsite;
        TextView tvFullAddress;



        public ViewHolder(View view) {
            super(view);
            tvContactPerson = (TextView) view.findViewById(R.id.tvContactPerson);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvWebsite = (TextView) view.findViewById(R.id.tvWebsite);
            tvFullAddress = (TextView) view.findViewById(R.id.tvFullAddress);
            llPhone = (LinearLayout) view.findViewById(R.id.llPhone);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {



        }
    }
}
