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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.isdental.R;
import com.actiknow.isdental.model.CompanyContact;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.SetTypeFace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CompanyContactAdapter extends RecyclerView.Adapter<CompanyContactAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<CompanyContact> brandsContactDetails = new ArrayList<> ();
    
    public CompanyContactAdapter (Activity activity, List<CompanyContact> brandsContactDetails) {
        this.activity = activity;
        this.brandsContactDetails = brandsContactDetails;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_company_contact, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final CompanyContact contactDetails = brandsContactDetails.get (position);
        
        holder.tvContactPerson.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvEmail.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvWebsite.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvFullAddress.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvPhone1.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvPhone2.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvTitle.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvTime.setTypeface (SetTypeFace.getTypeface (activity));
        
        if (contactDetails.getAttendant ().length () == 0) {
            holder.rlContactPerson.setVisibility (View.GONE);
        } else {
            holder.rlContactPerson.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getTitle ().length () == 0) {
            holder.tvTitle.setVisibility (View.GONE);
        } else {
            holder.tvTitle.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getEmail ().length () == 0) {
            holder.rlEmail.setVisibility (View.GONE);
        } else {
            holder.rlEmail.setVisibility (View.GONE);
        }
        
        
        if ((contactDetails.getPhone1 ().length () == 0) && (contactDetails.getPhone2 ().length () == 0)) {
            holder.rlPhone.setVisibility (View.GONE);
        } else if (contactDetails.getPhone1 ().length () == 0 && (contactDetails.getPhone2 ().length () != 0)) {
            holder.tvPhone2.setVisibility (View.VISIBLE);
            holder.rlPhone.setVisibility (View.VISIBLE);
            holder.tvPhone1.setVisibility (View.GONE);
        } else if (contactDetails.getPhone1 ().length () != 0 && contactDetails.getPhone2 ().length () == 0) {
            holder.rlPhone.setVisibility (View.VISIBLE);
            holder.tvPhone1.setVisibility (View.VISIBLE);
            holder.tvPhone2.setVisibility (View.GONE);
        } else {
            holder.rlPhone.setVisibility (View.VISIBLE);
            holder.tvPhone1.setVisibility (View.VISIBLE);
            holder.tvPhone2.setVisibility (View.VISIBLE);
        }
        
        if (contactDetails.getWebsite ().length () == 0) {
            holder.rlWebsite.setVisibility (View.GONE);
        } else {
            holder.rlWebsite.setVisibility (View.VISIBLE);
        }
        
        if (contactDetails.getAddress ().length () == 0) {
            holder.rlAddress.setVisibility (View.GONE);
        } else {
            holder.rlAddress.setVisibility (View.VISIBLE);
        }
        
        holder.tvContactPerson.setText (contactDetails.getAttendant ());
        holder.tvEmail.setText (Html.fromHtml ("<u><font color='#0645AD'>" + contactDetails.getEmail () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvWebsite.setText (Html.fromHtml ("<u><font color='#0645AD'>" + contactDetails.getWebsite () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvFullAddress.setText (contactDetails.getAddress ());
        holder.tvTitle.setText (contactDetails.getTitle ());
        holder.tvPhone1.setText (Html.fromHtml ("<u><font color='#0645AD'>" + contactDetails.getPhone1 () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvPhone2.setText (Html.fromHtml ("<u><font color='#0645AD'>" + contactDetails.getPhone2 () + "</font></u>"), TextView.BufferType.SPANNABLE);
        
        
        try {
            Calendar c = Calendar.getInstance ();
            String string1 = contactDetails.getOpen_time ();
            Date time1 = new SimpleDateFormat ("HH:mm:ss").parse (string1);
            Calendar calendar1 = Calendar.getInstance ();
            calendar1.setTime (time1);
            
            String string2 = contactDetails.getClose_time ();
            Date time2 = new SimpleDateFormat ("HH:mm:ss").parse (string2);
            Calendar calendar2 = Calendar.getInstance ();
            calendar2.setTime (time2);
            calendar2.add (Calendar.DATE, 1);
            
            String someRandomTime = new SimpleDateFormat ("HH:mm:ss").format (c.getTime ());
            Date d = new SimpleDateFormat ("HH:mm:ss").parse (someRandomTime);
            Calendar calendar3 = Calendar.getInstance ();
            calendar3.setTime (d);
            calendar3.add (Calendar.DATE, 1);
            
            Date currentTime = calendar3.getTime ();
            Date endTime = calendar2.getTime ();
            
            String day[] = contactDetails.getHolidays ().trim ().split (",");
            SimpleDateFormat dayFormat = new SimpleDateFormat ("EEE", Locale.US);
            Calendar calendar = Calendar.getInstance ();
            
            
            for (int i = 0; i < day.length; i++)
                if (dayFormat.format (calendar.getTime ()).equalsIgnoreCase (day[i])) {
                    holder.tvTime.setText ("today is holiday");
                    
                } else if (currentTime.after (calendar1.getTime ()) && currentTime.before (calendar2.getTime ())) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    
                    long diff = endTime.getTime () - currentTime.getTime ();
                    diff = diff / (60 * 1000);
                    float diffHours = diff / 60;
                    
                    holder.tvTime.setText ("Time in the range" + "Left time is " + " " + diffHours + ":" + diff);
                    
                } else {
                    holder.tvTime.setText ("closed");
                }
        } catch (java.text.ParseException e) {
            e.printStackTrace ();
        }
        
        
        holder.tvPhone1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone1 ()));
                sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity (sIntent);
            }
        });
        
        holder.tvPhone2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone2 ()));
                sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity (sIntent);
            }
        });
        
        holder.tvEmail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent email = new Intent (Intent.ACTION_SEND);
                email.putExtra (Intent.EXTRA_EMAIL, new String[] {contactDetails.getEmail ()});
                email.putExtra (Intent.EXTRA_SUBJECT, "Enquiry");
                email.putExtra (Intent.EXTRA_TEXT, "");
                email.setType ("message/rfc822");
                activity.startActivity (Intent.createChooser (email, "Choose an Email client :"));
            }
        });
        
        holder.tvWebsite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Uri uri;
                if (contactDetails.getWebsite ().contains ("http://") || contactDetails.getWebsite ().contains ("https://")) {
                    uri = Uri.parse (contactDetails.getWebsite ());
                } else {
                    uri = Uri.parse ("http://" + contactDetails.getWebsite ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                activity.startActivity (intent);
            }
        });
        
        
        for (int i = 0; i < contactDetails.getContactList ().size (); i++) {
            final ArrayList<String> contactList2 = contactDetails.getContactList ();
            TextView tv = new TextView (activity);
            tv.setText (Html.fromHtml ("<u><font color='#0645AD'>" + contactList2.get (i) + "</font></u>"), TextView.BufferType.SPANNABLE);
            tv.setTextSize (16);
            tv.setPadding (0, 5, 0, 5);
            tv.setTypeface (SetTypeFace.getTypeface (activity, Constants.font_name));
            tv.setTextColor (activity.getResources ().getColor (R.color.app_text_color_dark));
            final int finalI = i;
            tv.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactList2.get (finalI)));
                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity (sIntent);
                }
            });
            holder.llPhone.addView (tv);
        }
    }
    
    
    @Override
    public int getItemCount () {
        return brandsContactDetails.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactPerson;
        LinearLayout llPhone;
        TextView tvEmail;
        TextView tvWebsite;
        TextView tvPhone1;
        TextView tvPhone2;
        TextView tvTime;
        TextView tvTitle;
        TextView tvFullAddress;
        
        RelativeLayout rlContactPerson;
        RelativeLayout rlPhone;
        RelativeLayout rlEmail;
        RelativeLayout rlWebsite;
        RelativeLayout rlAddress;
        
        public ViewHolder (View view) {
            super (view);
            tvContactPerson = (TextView) view.findViewById (R.id.tvContactPerson);
            tvEmail = (TextView) view.findViewById (R.id.tvEmail);
            tvWebsite = (TextView) view.findViewById (R.id.tvWebsite);
            tvFullAddress = (TextView) view.findViewById (R.id.tvFullAddress);
            tvPhone1 = (TextView) view.findViewById (R.id.tvPhone1);
            tvPhone2 = (TextView) view.findViewById (R.id.tvPhone2);
            tvTime = (TextView) view.findViewById (R.id.tvTime);
            tvTitle = (TextView) view.findViewById (R.id.tvTitle);
            llPhone = (LinearLayout) view.findViewById (R.id.llPhone);
            
            rlContactPerson = (RelativeLayout) view.findViewById (R.id.rlContactPerson);
            rlPhone = (RelativeLayout) view.findViewById (R.id.rlPhone);
            rlEmail = (RelativeLayout) view.findViewById (R.id.rlEmail);
            rlWebsite = (RelativeLayout) view.findViewById (R.id.rlWebsite);
            rlAddress = (RelativeLayout) view.findViewById (R.id.rlAddress);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
