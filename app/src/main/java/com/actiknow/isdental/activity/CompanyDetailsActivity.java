package com.actiknow.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.isdental.R;
import com.actiknow.isdental.adapter.CompanyContactAdapter;
import com.actiknow.isdental.model.CompanyContact;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.AppConfigURL;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.NetworkConnection;
import com.actiknow.isdental.utils.UserDetailsPref;
import com.actiknow.isdental.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 13/06/2017.
 */

public class CompanyDetailsActivity extends AppCompatActivity {
    ImageView ivBack;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvTitle;
    RelativeLayout rlBack;
    RecyclerView rvCompanyContactList;
    int company_id;
    ImageView ivCompanyLogo;
    TextView tvCompanyName;
    TextView tvCompanyWebsite;
    TextView tvCompanyAboutUs;
    TextView tvCompanyBrands;
    TextView tvCompanyCategories;
    ProgressDialog progressDialog;
    ImageView ivFacebook;
    ImageView ivLinkedin;
    ImageView ivTwitter;
    ImageView ivPinterest;
    ImageView ivInstagram;
    ImageView ivYoutube;
    ImageView ivGooglePlus;
    LinearLayout llSocialButtons;
    CoordinatorLayout clMain;
    
    CompanyContactAdapter companyContactAdapter;
    List<CompanyContact> companyContactList = new ArrayList<> ();
    
    String companyWebsite;
    String companyAboutUs;
    String companyBrands;
    String companyCategories;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_company_detail);
        getExtras ();
        initView ();
        initData ();
        initListener ();
        getCompanyDetails ();
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        company_id = intent.getIntExtra (AppConfigTags.COMPANY_ID, 0);
    }
    
    private void initView () {
        ivBack = (ImageView) findViewById (R.id.ivBack);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rvCompanyContactList = (RecyclerView) findViewById (R.id.rvBrandsContactList);
        tvCompanyName = (TextView) findViewById (R.id.tvCompanyName);
        tvCompanyWebsite = (TextView) findViewById (R.id.tvCompanyWebsite);
        tvCompanyAboutUs = (TextView) findViewById (R.id.tvCompanyAboutUs);
        tvCompanyBrands = (TextView) findViewById (R.id.tvCompanyBrands);
        tvCompanyCategories = (TextView) findViewById (R.id.tvCompanyCategories);
        ivCompanyLogo = (ImageView) findViewById (R.id.ivCompanyLogo);
        
        llSocialButtons = (LinearLayout) findViewById (R.id.llSocialButtons);
        ivFacebook = (ImageView) findViewById (R.id.ivFacebook);
        ivTwitter = (ImageView) findViewById (R.id.ivTwitter);
        ivLinkedin = (ImageView) findViewById (R.id.ivLinkedIn);
        ivInstagram = (ImageView) findViewById (R.id.ivInstagram);
        ivGooglePlus = (ImageView) findViewById (R.id.ivGooglePlus);
        ivYoutube = (ImageView) findViewById (R.id.ivYouTube);
        ivPinterest = (ImageView) findViewById (R.id.ivPinterest);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        
        Utils.setTypefaceToAllViews (this, ivBack);
    }
    
    private void initData () {
        collapsingToolbarLayout.setTitleEnabled (false);
        appBar.setExpanded (true);
        progressDialog = new ProgressDialog (CompanyDetailsActivity.this);
        
        companyContactAdapter = new CompanyContactAdapter (CompanyDetailsActivity.this, companyContactList);
        rvCompanyContactList.setAdapter (companyContactAdapter);
        rvCompanyContactList.setHasFixedSize (true);
        rvCompanyContactList.setLayoutManager (new LinearLayoutManager (CompanyDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCompanyContactList.setItemAnimator (new DefaultItemAnimator ());
    }
    
    private void initListener () {
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        appBar.addOnOffsetChangedListener (new AppBarLayout.OnOffsetChangedListener () {
            @Override
            public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
                
                if (verticalOffset == 0) {
                    //expanded
                    tvTitle.setVisibility (View.GONE);
                } else if (Math.abs (verticalOffset) >= appBarLayout.getTotalScrollRange ()) {
                    //collapsed
                    tvTitle.setVisibility (View.VISIBLE);
                } else {
                    //idle
                    tvTitle.setVisibility (View.GONE);
                }
            }
        });
        
        ivFacebook.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivFacebook.getContentDescription ().toString ().contains ("http://") || ivFacebook.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivFacebook.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivFacebook.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivTwitter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivTwitter.getContentDescription ().toString ().contains ("http://") || ivTwitter.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivTwitter.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivTwitter.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivLinkedin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivLinkedin.getContentDescription ().toString ().contains ("http://") || ivLinkedin.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivLinkedin.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivLinkedin.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivGooglePlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivGooglePlus.getContentDescription ().toString ().contains ("http://") || ivGooglePlus.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivGooglePlus.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivGooglePlus.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivYoutube.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivYoutube.getContentDescription ().toString ().contains ("http://") || ivYoutube.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivYoutube.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivYoutube.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivInstagram.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivInstagram.getContentDescription ().toString ().contains ("http://") || ivInstagram.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivInstagram.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivInstagram.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivPinterest.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivPinterest.getContentDescription ().toString ().contains ("http://") || ivPinterest.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivPinterest.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivPinterest.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        
        tvCompanyWebsite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (tvCompanyWebsite.getText ().toString ().contains ("http://") || tvCompanyWebsite.getText ().toString ().contains ("https://")) {
                    uri = Uri.parse (tvCompanyWebsite.getText ().toString ());
                } else {
                    uri = Uri.parse ("http://" + tvCompanyWebsite.getText ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
    }

    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void getCompanyDetails () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            companyContactList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
    
                                    if (! is_error) {
                                        tvTitle.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                                        tvCompanyName.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                                        tvCompanyWebsite.setText (Html.fromHtml ("<u><font color='#0645AD'>" + "website" + "</font></u>"), TextView.BufferType.SPANNABLE);
                                        Glide.with (CompanyDetailsActivity.this).load (jsonObj.getString (AppConfigTags.COMPANY_IMAGE)).placeholder (R.mipmap.ic_launcher).into (ivCompanyLogo);
        
                                        companyWebsite = jsonObj.getString (AppConfigTags.COMPANY_WEBSITE);
        
                                        int weight = 0;
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_FACEBOOK).length () > 0)) {
                                            ivFacebook.setVisibility (View.VISIBLE);
                                            weight++;
                                            ivFacebook.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_FACEBOOK));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_TWITTER).length () > 0)) {
                                            ivTwitter.setVisibility (View.VISIBLE);
                                            weight++;
                                            ivTwitter.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_TWITTER));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_LINKEDIN).length () > 0)) {
                                            weight++;
                                            ivLinkedin.setVisibility (View.VISIBLE);
                                            ivLinkedin.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_LINKEDIN));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_PINTEREST).length () > 0)) {
                                            weight++;
                                            ivPinterest.setVisibility (View.VISIBLE);
                                            ivPinterest.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_PINTEREST));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_YOUTUBE).length () > 0)) {
                                            weight++;
                                            ivYoutube.setVisibility (View.VISIBLE);
                                            ivYoutube.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_YOUTUBE));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_GOOGLEPLAS).length () > 0)) {
                                            weight++;
                                            ivGooglePlus.setVisibility (View.VISIBLE);
                                            ivGooglePlus.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_GOOGLEPLAS));
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_INSTAGRAM).length () > 0)) {
                                            ivInstagram.setVisibility (View.VISIBLE);
                                            ivInstagram.setContentDescription (jsonObj.getString (AppConfigTags.COMPANY_INSTAGRAM));
                                            weight++;
                                        }
        
                                        llSocialButtons.setWeightSum (weight);
        
        
                                        JSONArray jsonArrayBrand = jsonObj.getJSONArray (AppConfigTags.CONTACTS);
                                        companyContactList.clear ();
                                        for (int i = 0; i < jsonArrayBrand.length(); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject(i);
                                            companyContactList.add (i, new CompanyContact (
                                                    jsonObjectBrand.getInt (AppConfigTags.CONTACT_ID),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_ATTENDANT),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_DESIGNATION),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_TYPE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_ADDRESS),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_EMAIL),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_WEBSITE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_PHONE1),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_PHONE2),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_TITLE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_OPEN_TIME),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_CLOSE_TIME),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_HOLIDAY)
                                            ));
                                        }
                                        companyContactAdapter.notifyDataSetChanged ();
                                        progressDialog.dismiss ();
                                    } else {
                                        Utils.showSnackBar (CompanyDetailsActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (CompanyDetailsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    progressDialog.dismiss ();
                                }
                            } else {
                                Utils.showSnackBar (CompanyDetailsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (CompanyDetailsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);

                            }
                        }
                    }) {

                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.USER_LOGIN_KEY, userDetailsPref.getStringPref (CompanyDetailsActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
}
