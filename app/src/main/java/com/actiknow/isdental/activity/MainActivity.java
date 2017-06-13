package com.actiknow.isdental.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actiknow.isdental.R;
import com.actiknow.isdental.adapter.HomeServiceAdapter;

import com.actiknow.isdental.model.HomeService;

import com.actiknow.isdental.utils.SimpleDividerItemDecoration;
import com.actiknow.isdental.utils.TypefaceSpan;
import com.actiknow.isdental.utils.Utils;
import com.actiknow.isdental.utils.VisitorDetailsPref;

import com.bugsnag.android.Bugsnag;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;

import java.util.List;


import static com.actiknow.isdental.activity.LoginActivity.PERMISSION_REQUEST_CODE;


public class MainActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {
    VisitorDetailsPref visitorDetailsPref;
    int version_code;
    CoordinatorLayout clMain;
    ImageView ivVisitorCard;
    ImageView ivIndiaSupplyLogo;

    ProgressDialog progressDialog;
    ProgressBar progressBar;

    RecyclerView rvHomeServiceList;
    List<HomeService> homeServices = new ArrayList<>();
    HomeServiceAdapter homeServiceAdapter;
    TextView tvRefreshData;
    private SliderLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
//        checkPermissions ();
        initListener();
        isLogin();

    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        rvHomeServiceList = (RecyclerView) findViewById(R.id.rvHomeServiceList);
        ivVisitorCard = (ImageView) findViewById(R.id.ivVisitorCard);
        ivIndiaSupplyLogo = (ImageView) findViewById(R.id.ivIndiaSupplyLogo);
        slider = (SliderLayout) findViewById(R.id.slider);
        tvRefreshData = (TextView) findViewById(R.id.tvRefreshData);
    }

    private void initData() {
//        Bugsnag.init(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);


        visitorDetailsPref = VisitorDetailsPref.getInstance();


        progressDialog = new ProgressDialog(this);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version_code = pInfo.versionCode;


        homeServices.add(new HomeService(1, R.drawable.ic_list, "", "BRANDS"));
        homeServices.add(new HomeService(2, R.drawable.ic_program, "", "SHOP ONLINE"));
        homeServices.add (new HomeService (3, R.drawable.ic_program, "", "EVENTS"));
        homeServices.add (new HomeService (4, R.drawable.ic_hall_plan, "", "IS SPECIAL"));
       homeServices.add (new HomeService (5, R.drawable.ic_favourite, "", "OFFERS"));
//        homeServices.add (new HomeService (6, R.drawable.ic_card, "", "MY ENTRY PASS"));
        homeServices.add(new HomeService(6, R.drawable.ic_information, "", "ABOUT US"));


        homeServiceAdapter = new HomeServiceAdapter(this, homeServices);
        rvHomeServiceList.setAdapter(homeServiceAdapter);
        rvHomeServiceList.setHasFixedSize(true);
        rvHomeServiceList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvHomeServiceList.addItemDecoration(new SimpleDividerItemDecoration(this));
        rvHomeServiceList.setItemAnimator(new DefaultItemAnimator());


        Utils.setTypefaceToAllViews(this, clMain);
    }

    private void initListener() {
        ivIndiaSupplyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://indiasupply.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
    }


    /*
    private void initSlider () {
        slider.removeAllSliders ();
        for (int i = 0; i < db.getAllSmallBanners ().size (); i++) {
            Banner banner = db.getAllSmallBanners ().get (i);
            SpannableString s = new SpannableString (banner.getTitle ());
            s.setSpan (new TypefaceSpan(this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
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
    
    /*
    private void logOutFromDevice (final int device_id) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_logging_out), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_LOGOUT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_LOGOUT,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, "" + AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_ID, "");
                                    visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_NAME, "");
                                    visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_EMAIL, "");
                                    visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_MOBILE, "");
                                    visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY, "");
    
                                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                                    intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity (intent);
                                    overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                                Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            progressDialog.dismiss ();
                            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_VISITOR_LOGIN_KEY, visitorDetailsPref.getStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    */
    private void isLogin() {
        if (visitorDetailsPref.getStringPref(MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY) == "") {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            // } else {
            //      initApplication (visitorDetailsPref.getIntPref (MainActivity.this, VisitorDetailsPref.DATABASE_VERSION));
        }
        if (visitorDetailsPref.getStringPref(MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY) == "")// || userDetailPref.getStringPref (MainActivity.this, VisitorDetailsPref.HOSPITAL_DEFAULT_PATIENT_ID) == "")
            finish();
    }

    /*
    private void initApplication (final int db_version) {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        
        final JSONArray jsonArrayFavourites = new JSONArray ();
        try {
            ArrayList<Favourite> favouriteList = db.getAllFavourites ();
            for (int i = 0; i < favouriteList.size (); i++) {
                Favourite favourite = favouriteList.get (i);
                JSONObject jsonObject1 = new JSONObject ();
                jsonObject1.put ("favourite_type", favourite.getType ());
                switch (favourite.getType ()) {
                    case "EXHIBITOR":
                        jsonObject1.put ("favourite_exhibitor_id", favourite.getExhibitor_id ());
                        break;
                    case "EVENT":
                        jsonObject1.put ("favourite_event_id", favourite.getEvent_id ());
                        break;
                    case "SESSION":
                        jsonObject1.put ("favourite_session_id", favourite.getSession_id ());
                        break;
                }
                jsonArrayFavourites.put (jsonObject1);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        
        if (db_version == 0) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_initializing), false);
        }
        
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            final PackageInfo finalPInfo = pInfo;
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INIT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    // status 1=> already latest, 2=> update required
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    int status = jsonObj.getInt (AppConfigTags.STATUS);
                                    
                                    db.deleteAllBanners ();
                                    JSONArray jsonArrayBanner = jsonObj.getJSONArray (AppConfigTags.BANNERS);
                                    ArrayList<Banner> bannerArrayList = new ArrayList<Banner> ();
                                    for (int i = 0; i < jsonArrayBanner.length (); i++) {
                                        JSONObject jsonObjectBanner = jsonArrayBanner.getJSONObject (i);
                                        Banner banner = new Banner(
                                                jsonObjectBanner.getInt (AppConfigTags.BANNER_ID),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_TITLE),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_IMAGE),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_URL),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_TYPE)
                                        );
                                        bannerArrayList.add (banner);
//                                        db.createBanner (banner);
                                    }
                                    
                                    db.insertAllBanners (bannerArrayList);
                                    
                                    initSlider ();
                                    
                                    if (! error) {
                                        switch (status) {
                                            case 1:
                                                progressDialog.dismiss ();
                                                break;
                                            case 2:
                                                db.deleteAllExhibitors ();
                                                db.deleteAllStallDetails ();
                                                
                                                db.deleteAllEvents ();
                                                db.deleteAllEventSpeakers ();
                                                db.deleteAllEventTopics ();
                                                
                                                db.deleteAllSessions ();
                                                db.deleteAllSessionSpeakers ();
                                                db.deleteAllSessionTopics ();
                                                
                                                db.deleteAllCategories ();
                                                db.deleteAllCategoryMappings ();

//                                                new insertDataInDatabase (progressDialog).execute (response);
                                                new insertDataInDatabase (MainActivity.this, progressDialog, db_version).execute (response);
                                                
                                                break;
                                        }
                                        visitorDetailsPref.putIntPref (MainActivity.this, VisitorDetailsPref.DATABASE_VERSION, jsonObj.getInt (AppConfigTags.DATABASE_VERSION));
                                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_ID, jsonObj.getString (AppConfigTags.VISITOR_ID));
                                        if (jsonObj.getBoolean (AppConfigTags.VERSION_UPDATE)) {
                                            new MaterialDialog.Builder (MainActivity.this)
                                                    .content (R.string.dialog_text_new_version_available)
                                                    .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                                                    .canceledOnTouchOutside (false)
                                                    .cancelable (false)
                                                    .positiveText (R.string.dialog_action_update)
                                                    .negativeText (R.string.dialog_action_ignore)
                                                    .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            final String appPackageName = getPackageName (); // getPackageName() from Context or Activity object
                                                            try {
                                                                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("market://details?id=" + appPackageName)));
                                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                            }
                                                        }
                                                    })
                                                    .onNegative (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss ();
                                                        }
                                                    }).show ();
                                        }
                                    }
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    e.printStackTrace ();
                                }
                            } else {
                                progressDialog.dismiss ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put ("db_version", String.valueOf (db_version));
                    params.put ("app_version", String.valueOf (finalPInfo.versionCode));
                    params.put ("favourites_json", jsonArrayFavourites.toString ());
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_VISITOR_LOGIN_KEY, visitorDetailsPref.getStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
            progressDialog.dismiss ();
//            initApplication ();
        }
    }
    */
    @Override
    public void onBackPressed() {
/*
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (R.string.dialog_text_quit_application)
                .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                .neutralColor (getResources ().getColor (R.color.app_text_color_dark))
                .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                .typeface (SetTypeFace.getTypeface (this), SetTypeFace.getTypeface (this))
                .canceledOnTouchOutside (false)
                .cancelable (false)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .neutralText (R.string.dialog_action_logout)
                .onNeutral (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_ID, "");
                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_NAME, "");
                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_EMAIL, "");
                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_MOBILE, "");
                        visitorDetailsPref.putStringPref (MainActivity.this, VisitorDetailsPref.VISITOR_LOGIN_KEY, "");

                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        visitorDetailsPref.putBooleanPref (MainActivity.this, VisitorDetailsPref.LOGGED_IN_SESSION, false);


                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
*/
        super.onBackPressed();
        visitorDetailsPref.putBooleanPref(MainActivity.this, VisitorDetailsPref.LOGGED_IN_SESSION, false);
        finish();
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
//                    checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.VIBRATE,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.GET_ACCOUNTS,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE
                        },
                        PERMISSION_REQUEST_CODE);
            }
/*
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.INTERNET}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED,}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            }
*/
        }
    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Permission are required please enable them on the App Setting page")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // user denied flagging NEVER ASK AGAIN
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    } else if (Manifest.permission.RECEIVE_SMS.equals(permission)) {
//                        Utils.showToast (this, "Camera Permission is required");
//                        showRationale (permission, R.string.permission_denied_contacts);
                        // user denied WITHOUT never ask again
                        // this is a good place to explain the user
                        // why you need the permission and ask if he want
                        // to accept it (the rationale)
                    } else if (Manifest.permission.READ_SMS.equals(permission)) {
                    } else if (Manifest.permission.VIBRATE.equals(permission)) {
                    } else if (Manifest.permission.GET_ACCOUNTS.equals(permission)) {
                    } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                    } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
//                    } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals (permission)) {
                    } else if (Manifest.permission.READ_PHONE_STATE.equals(permission)) {
                    }
                }
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Uri uri = Uri.parse("http://" + slider.getBundle().get("url"));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}