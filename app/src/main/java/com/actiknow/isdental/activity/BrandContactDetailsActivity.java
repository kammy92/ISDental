package com.actiknow.isdental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actiknow.isdental.R;
import com.actiknow.isdental.adapter.BrandsContactDetailsAdapter;
import com.actiknow.isdental.adapter.CompanyListAdapter;
import com.actiknow.isdental.model.BrandsContactDetails;
import com.actiknow.isdental.model.Company;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.AppConfigURL;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.NetworkConnection;
import com.actiknow.isdental.utils.SimpleDividerItemDecoration;
import com.actiknow.isdental.utils.Utils;
import com.actiknow.isdental.utils.VisitorDetailsPref;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

public class BrandContactDetailsActivity extends AppCompatActivity {
    ImageView ivBack;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    RelativeLayout rlBack;
    RecyclerView rvBrandsContactList;
    int companyId;


    BrandsContactDetailsAdapter brandsContactDetailsAdapter;
    List<BrandsContactDetails> brandsContactDetailsList=new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_brands_detail);
        initView ();
        initData ();
        initListener ();
    }

    private void getExtras () {
        Intent intent = getIntent ();
        companyId = intent.getIntExtra (AppConfigTags.COMPANY_ID, 0);
    }


    private void initView () {
        ivBack = (ImageView) findViewById (R.id.ivBack);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvBrandsContactList=(RecyclerView)findViewById(R.id.rvBrandsContactList) ;

        Utils.setTypefaceToAllViews (this, ivBack);
    }

    private void initData () {
        collapsingToolbarLayout.setTitleEnabled (false);
        appBar.setExpanded (true);




        ArrayList<String> list=new ArrayList<>();
        list.add("9889198981");
        list.add("8527875036");
        list.add("9889134343");
        list.add("9878934543");


        brandsContactDetailsList.add(new BrandsContactDetails(1,"Dr Rahul jain","A-49 New Delhi","Rahuljain_35@yahoo.com","www.actiknow.com",list));
        brandsContactDetailsList.add(new BrandsContactDetails(1,"Dr Karman Singh","A-49 New Delhi","Rahuljain_35@yahoo.com","www.actiknow.com",list));
        brandsContactDetailsAdapter = new BrandsContactDetailsAdapter(this, brandsContactDetailsList);
        rvBrandsContactList.setAdapter (brandsContactDetailsAdapter);
        rvBrandsContactList.setHasFixedSize (true);
        rvBrandsContactList.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBrandsContactList.setItemAnimator (new DefaultItemAnimator());

    }

    private void initListener () {
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getCompanyBrandContactList () {
        if (NetworkConnection.isNetworkAvailable (this)) {
          //  tvNoResult.setVisibility (View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_BRANDS_CONTACT_LIST+"/"+companyId, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_BRANDS_CONTACT_LIST+"/"+companyId,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            brandsContactDetailsList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayBrand = jsonObj.getJSONArray(AppConfigTags.COMPANY);
                                        for (int i = 0; i < jsonArrayBrand.length(); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject(i);
                                            BrandsContactDetails contact = new BrandsContactDetails();
                                                   contact.setId(jsonObjectBrand.getInt(AppConfigTags.BRAND_CONTACT_ID));
                                                    contact.setContact_person(jsonObjectBrand.getString(AppConfigTags.BRAND_CONTACT_NAME));
                                                    contact.setAddress(jsonObjectBrand.getString(AppConfigTags.BRAND_CONTACT_ADDRESS));
                                                    contact.setEmail(jsonObjectBrand.getString(AppConfigTags.BRAND_CONTACT_EMAIL));
                                                    contact.setWebsite(jsonObjectBrand.getString(AppConfigTags.BRAND_CONTACT_WEBSITE));

                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();

                                }
                            } else {
//
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
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
                    VisitorDetailsPref visitorDetailsPref = VisitorDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.USER_LOGIN_KEY, Constants.login_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
//
        }
    }
}
