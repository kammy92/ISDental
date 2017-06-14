package com.actiknow.isdental.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.isdental.R;

import com.actiknow.isdental.adapter.CompanyListAdapter;
import com.actiknow.isdental.model.Banner;
import com.actiknow.isdental.model.Company;
import com.actiknow.isdental.model.StallDetail;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.AppConfigURL;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.NetworkConnection;
import com.actiknow.isdental.utils.SimpleDividerItemDecoration;
import com.actiknow.isdental.utils.TypefaceSpan;
import com.actiknow.isdental.utils.Utils;
import com.actiknow.isdental.utils.VisitorDetailsPref;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class CompanyListActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {
    RecyclerView rvBrandList;
    ImageView ivBack;
    //    SwipeRefreshLayout swipeRefreshLayout;
    List<Company> companyList = new ArrayList<> ();
    List<Company> tempBrandList = new ArrayList<> ();
    CompanyListAdapter companyListAdapter;
    String[] category;
    List<Banner> bannerlist = new ArrayList<>();
    ImageView ivFilter;
    ImageView ivSort;
    TextView tvTitle;
    SearchView searchView;

    TextView tvNoResult;

    List<StallDetail> stallDetailList = new ArrayList<> ();

    String filterCategory = "";
    String filterSubCategory = "";

//    Dialog dialog;

    private Toolbar toolbar;
    private SliderLayout slider;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_brand_list);
        initView ();
        initData ();
        initListener ();
        getCompanyList();
//        getBrandList ();
        initSlider ();
//        setUpNavigationDrawer ();
   //     getOfflineBrandList ();
    }

    /*private void getOfflineBrandList () {
        Utils.showLog (Log.DEBUG, AppConfigTags.TAG, "Getting all the Brands from local database", true);
        companyList.clear ();
        ArrayList<Company> offlineBrand = db.getAllBrandList ();
        for (Company Company : offlineBrand)
            companyList.add (Company);
        CompanyListAdapter.notifyDataSetChanged ();
//        swipeRefreshLayout.setRefreshing (false);
    }*/

    private void initView () {
        rvBrandList = (RecyclerView) findViewById (R.id.rvBrandList);
        ivBack = (ImageView) findViewById (R.id.ivBack);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
        tvNoResult = (TextView) findViewById (R.id.tvNoResult);

        ivSort = (ImageView) findViewById (R.id.ivSort);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        searchView = (SearchView) findViewById (R.id.searchView);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (swipeRefreshLayout);
        Utils.setTypefaceToAllViews (this, rvBrandList);

        toolbar = (Toolbar) findViewById (R.id.toolbar1);
        slider = (SliderLayout) findViewById (R.id.slider);

    }

    private void initData () {
      //  db = new DatabaseHandler (getApplicationContext ());
//        dialog = Utils.showBigBannerDialog (this);
        category = new String[] {"Air Abrasion", "Curing Lights", "Disposable Needles"};//        swipeRefreshLayout.setRefreshing (true);

      //  companyList.add (new Company(1, "http://seeklogo.com/images/1/3M-logo-079FB52BC8-seeklogo.com.png", "3M ESPE", "Hall 1"));
     //   companyList.add (new Company(2, "http://mudrsoc.com/wp-content/uploads/2017/01/Dentsply-Logo-Black.jpg", "DENTSPLY SIRONA", "Hall 1"));
     //   companyList.add (new Company(3, "http://www.cldental.com.au/mobile/images/equipment/compressors/durrlogo.jpg", "DUERR DENTEL INDIA", "Hall 1"));
      //  companyList.add (new Company(4, "", "UNICORN DENMART", "Hall 1"));
      //  companyList.add (new Company(5, "", "CAPRI", "Hall 1"));
       // companyList.add (new Company(6, "", "ACETON INDIA", "Hall 1"));
      //  companyList.add (new Company(7, "", "SKANRAY TECHNOLOGY", "Hall 1"));

        bannerlist.add(new Banner(1, "Title 2", "http://famdent.indiasupply.com//api/images/banners/chesa1.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=CHESA%20DENTAL%20CARE"));
        bannerlist.add(new Banner(2, "Title 2", "http://famdent.indiasupply.com//api/images/banners/chesa2.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=CHESA%20DENTAL%20CARE"));
        bannerlist.add(new Banner(3, "Title 2", "http://famdent.indiasupply.com//api/images/banners/chesa3.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=CHESA%20DENTAL%20CARE"));
        bannerlist.add(new Banner(4, "Title 2", "http://famdent.indiasupply.com//api/images/banners/chesa4.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=CHESA%20DENTAL%20CARE"));
        bannerlist.add(new Banner(5, "Title 2", "http://famdent.indiasupply.com//api/images/banners/haitech1.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=HAITECH%20MEDICAL%20SOLUTIONS%20PVT%20LTD"));
        bannerlist.add(new Banner(6, "Title 2", "http://famdent.indiasupply.com//api/images/banners/haitech2.jpg", "SMALL", "www.famdent.indiasupply.com/stall_new/Brands.php?id=HAITECH%20MEDICAL%20SOLUTIONS%20PVT%20LTD"));


        // swipeRefreshLayout.setColorSchemeColors (getResources ().getColor (R.color.colorPrimaryDark));


        searchView.setQueryHint (Html.fromHtml ("<font color = #ffffff>" + "Search" + "</font>"));
    }

    private void initListener () {
//        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
//            @Override
//            public void onRefresh () {
//                swipeRefreshLayout.setRefreshing (true);
//                getOfflineBrandList ();
//                getBrandList ();
//            }
//        });
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

     /*   ivFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                new MaterialDialog.Builder (CompanyListActivity.this)
                        .title ("Select Category")
                        .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                        .items (db.getAllCategoryName ())
                        .typeface (SetTypeFace.getTypeface (CompanyListActivity.this), SetTypeFace.getTypeface (CompanyListActivity.this))
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                        .positiveText ("RESET FILTER")
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                filterCategory = "";
                                filterSubCategory = "";
                                companyList.clear ();
                                ArrayList<Company> offlineBrand = db.getAllBrandList ();
                                for (Company Company : offlineBrand)
                                    companyList.add (Company);

                                CompanyListAdapter = new CompanyListAdapter (CompanyListActivity.this, companyList);
                                rvBrandList.setAdapter (CompanyListAdapter);

                                CompanyListAdapter.notifyDataSetChanged ();
                                ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter));
                            }
                        })
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                filterCategory = text.toString ();
                                ArrayList<String> level2CategoryList = db.getAllCategoryLevel2 (text.toString ());
                                if (level2CategoryList.size () > 1) {
                                    new MaterialDialog.Builder (CompanyListActivity.this)
                                            .title ("Select Sub Category")
                                            .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                                            .items (db.getAllCategoryLevel2 (text.toString ()))
                                            .typeface (SetTypeFace.getTypeface (CompanyListActivity.this), SetTypeFace.getTypeface (CompanyListActivity.this))
                                            .canceledOnTouchOutside (true)
                                            .cancelable (true)
                                            .itemsCallback (new MaterialDialog.ListCallback () {
                                                @Override
                                                public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                                    filterSubCategory = text.toString ();
//                                                    Utils.showToast (CompanyListActivity.this, "Filter\n Category : " + filterCategory + "\nSub Category : " + filterSubCategory, true);
//                                                    Utils.showToast (CompanyListActivity.this, "Category Ids : " + db.getAllFilteredCategoryIds (filterCategory, filterSubCategory) + "\nCompany Ids : " + db.getAllFilteredBrandIds (filterCategory, filterSubCategory), true);

                                                    companyList.clear ();
                                                    ArrayList<Company> offlineBrand = db.getAllFilteredBrandList (filterCategory, filterSubCategory);
                                                    for (Company Company : offlineBrand)
                                                        companyList.add (Company);
                                                    CompanyListAdapter.notifyDataSetChanged ();
                                                    ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter_checked));
                                                }
                                            })
                                            .show ();
                                } else {
                                    filterSubCategory = level2CategoryList.get (0);
//                                    Utils.showToast (CompanyListActivity.this, "Filter\n Category : " + filterCategory + "\nSub Category : " + filterSubCategory, true);
//                                    Utils.showToast (CompanyListActivity.this, "Category Ids : " + db.getAllFilteredCategoryIds (filterCategory, filterSubCategory) + "\nCompany Ids : " + db.getAllFilteredBrandIds (filterCategory, filterSubCategory), true);
                                    companyList.clear ();
                                    ArrayList<Company> offlineBrand = db.getAllFilteredBrandList (filterCategory, filterSubCategory);
                                    for (Company Company : offlineBrand)
                                        companyList.add (Company);
                                    CompanyListAdapter.notifyDataSetChanged ();
                                    ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter_checked));
                                }
                            }
                        })
                        .show ();

    /*

                boolean wrapInScrollView = true;
                final MaterialDialog dialog = new MaterialDialog.Builder (CompanyListActivity.this)
                        .customView (R.layout.dialog_filter, wrapInScrollView)
                        .positiveText ("APPLY")
                        .negativeText ("CANCEL")
                        .canceledOnTouchOutside (false)
                        .cancelable (false)
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ivFilter.setImageResource (R.drawable.ic_filter_checked);
                            }
                        })
                        .typeface (SetTypeFace.getTypeface (CompanyListActivity.this, Constants.font_name), SetTypeFace.getTypeface (CompanyListActivity.this, Constants.font_name))
                        .show ();


                //Spinner spCategory=(Spinner)dialog.findViewById(R.id.spCategoryType);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompanyListActivity.this, android.R.layout.simple_spinner_item, category);
                //spCategory.setAdapter(adapter);


                LinearLayout llCategory = (LinearLayout) dialog.findViewById (R.id.llCategory);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams (
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                Utils.setTypefaceToAllViews (CompanyListActivity.this, llCategory);

                for (int i = 0; i < category.length; i++) {
                    CheckBox checkBox = new CheckBox (CompanyListActivity.this);
                    checkBox.setLayoutParams (lparams);
                    checkBox.setText (category[i]);
                    llCategory.addView (checkBox);

                }
                */
         /*   }
        });*/

     /*   searchView.setOnSearchClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
//                Toast.makeText (CompanyListActivity.this, "karman open", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.GONE);
                ivBack.setVisibility (View.GONE);
//                ivSort.setVisibility (View.GONE);
                tvTitle.setVisibility (View.GONE);
            }
        });

        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit (String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                tempBrandList.clear ();
                for (Company Company : companyList) {
                    List<StallDetail> tempStallDetailList = Company.getStallDetailList ();
                    StallDetail stallDetail = tempStallDetailList.get (0);
                    if (Company.getBrand_name ().toUpperCase ().contains (newText.toUpperCase ()) ||
                            Company.getBrand_name ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            stallDetail.getStall_number ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            stallDetail.getStall_number ().toUpperCase ().contains (newText.toUpperCase ())) {
                        tempBrandList.add (Company);
                    }
                }
                CompanyListAdapter = new CompanyListAdapter (CompanyListActivity.this, tempBrandList);
                rvBrandList.setAdapter (CompanyListAdapter);
                rvBrandList.setHasFixedSize (true);
                rvBrandList.setLayoutManager (new LinearLayoutManager (CompanyListActivity.this, LinearLayoutManager.VERTICAL, false));
                rvBrandList.addItemDecoration (new SimpleDividerItemDecoration (CompanyListActivity.this));
                rvBrandList.setItemAnimator (new DefaultItemAnimator ());
                return true;
            }
        });

        searchView.setOnCloseListener (new SearchView.OnCloseListener () {
            @Override
            public boolean onClose () {
//                Toast.makeText (CompanyListActivity.this, "karman close", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.VISIBLE);
                ivBack.setVisibility (View.VISIBLE);
//                ivSort.setVisibility (View.VISIBLE);
                tvTitle.setVisibility (View.VISIBLE);
                return false;
            }
        });*/
    }

    private void initSlider () {
        for (int i = 0; i < bannerlist.size (); i++) {
            Banner banner = bannerlist.get (i);
            SpannableString s = new SpannableString (banner.getTitle ());
            s.setSpan (new TypefaceSpan (this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            DefaultSliderView defaultSliderView = new DefaultSliderView (this);
            defaultSliderView
                    .image (banner.getImage ())
                    .setScaleType (BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener (this);

            defaultSliderView.bundle (new Bundle ());
            defaultSliderView.getBundle ().putString ("url", banner.getUrl ());
            slider.addSlider (defaultSliderView);
        }

        slider.setIndicatorVisibility (PagerIndicator.IndicatorVisibility.Visible);
        slider.setPresetTransformer (SliderLayout.Transformer.Default);
        slider.setCustomAnimation (new DescriptionAnimation ());
        slider.setDuration (5000);
        slider.addOnPageChangeListener (this);
        slider.setCustomIndicator ((PagerIndicator) findViewById (R.id.custom_indicator));
        slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    }


   private void getCompanyList () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            tvNoResult.setVisibility (View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_COMPANY_LIST, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_COMPANY_LIST,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            companyList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayBrand = jsonObj.getJSONArray (AppConfigTags.COMPANY);
                                        for (int i = 0; i < jsonArrayBrand.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject (i);
                                            Company company = new Company(
                                                    jsonObjectBrand.getInt (AppConfigTags.COMPANY_ID),"",
                                                    jsonObjectBrand.getString (AppConfigTags.COMPANY_NAME),
                                                    jsonObjectBrand.getString (AppConfigTags.COMPANY_DESCRIPTION));


                                            companyList.add (i, company);
                                            companyListAdapter = new CompanyListAdapter(CompanyListActivity.this, companyList);
                                            rvBrandList.setAdapter (companyListAdapter);
                                            rvBrandList.setHasFixedSize (true);
                                            rvBrandList.setLayoutManager (new LinearLayoutManager (CompanyListActivity.this, LinearLayoutManager.VERTICAL, false));
                                            rvBrandList.addItemDecoration (new SimpleDividerItemDecoration (CompanyListActivity.this));
                                            rvBrandList.setItemAnimator (new DefaultItemAnimator ());

                                        }

                                        if (jsonArrayBrand.length () > 0) {
//                                            swipeRefreshLayout.setRefreshing (false);
                                        } else {
//                                            swipeRefreshLayout.setRefreshing (false);
                                            tvNoResult.setVisibility (View.VISIBLE);
                                        }
                                    } else {
//                                        swipeRefreshLayout.setRefreshing (false);
                                        tvNoResult.setVisibility (View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
//                                    swipeRefreshLayout.setRefreshing (false);
                                    tvNoResult.setVisibility (View.VISIBLE);
                                }
                            } else {
//                                swipeRefreshLayout.setRefreshing (false);
                                tvNoResult.setVisibility (View.VISIBLE);
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
//                            swipeRefreshLayout.setRefreshing (false);
                            tvNoResult.setVisibility (View.VISIBLE);
                        }
                    }) {
    
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
    
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    VisitorDetailsPref visitorDetailsPref = VisitorDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.USER_LOGIN_KEY, Constants.login_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
//            swipeRefreshLayout.setRefreshing (false);
            tvNoResult.setVisibility (View.VISIBLE);
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService (Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem (R.id.action_search).getActionView ();
        if (null != searchView) {
            searchView.setSearchableInfo (searchManager.getSearchableInfo (getComponentName ()));
//            searchView.setIconifiedByDefault (false);
        }


//        final int searchBarId = searchView.getContext ().getResources ().getIdentifier ("android:id/search_bar", null, null);
//        LinearLayout searchBar = (LinearLayout) searchView.findViewById (searchBarId);

        EditText et = (EditText) searchView.findViewById (R.id.search_src_text);
//        et.getBackground ().setColorFilter (R.color.text_color_grey_dark,null);
//        et.setBackgroundColor (getResources ().getColor (R.color.text_color_grey_light)); // ← If you just want a color
//        et.setBackground (getResources ().getDrawable (R.drawable.layout_search_edittext));

//        et.setFocusableInTouchMode (true);
//        et.setFocusable (true);

        LinearLayout searchBar = (LinearLayout) searchView.findViewById (R.id.search_bar);
        searchBar.setLayoutTransition (new LayoutTransition ());

        searchView.setQueryHint ("Search ATM ID");
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener () {
            public boolean onQueryTextChange (String newText) {
//                etSearch.setText (newText);
                return true;
            }

            public boolean onQueryTextSubmit (String query) {
                //Here u can get the value "query" which is entered in the search box.
                return true;
            }
        };
        searchView.setOnQueryTextListener (queryTextListener);

        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.action_search:
//                if (etSearch.isShown ()) {
//                    etSearch.setVisibility (View.GONE);
//                    final Handler handler = new Handler ();
//                    handler.postDelayed (new Runnable () {
//                        @Override
//                        public void run () {
//                            etSearch.setText ("");
//                        }
//                    }, 1000);
//                } else {
//                    etSearch.setVisibility (View.VISIBLE);
//                }
                break;
        }
        Utils.hideSoftKeyboard (CompanyListActivity.this);
/**
 if (item != null && item.getItemId () == android.R.id.home) {
 if (mDrawerLayout.isDrawerOpen (mDrawerPanel)) {
 } else {
 mDrawerLayout.openDrawer (mDrawerPanel);
 }
 return true;
 }
 */
//        return super.onOptionsItemSelected (item);
//    }
//*/
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    @Override
    public void onSliderClick (BaseSliderView slider) {
        Uri uri = Uri.parse ("http://" + slider.getBundle ().get ("url"));
        Intent intent = new Intent (Intent.ACTION_VIEW, uri);
        startActivity (intent);
    }
    
    @Override
    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
    }
    
    @Override
    public void onPageSelected (int position) {
    }
    
    @Override
    public void onPageScrollStateChanged (int state) {
    }
    
    
}
