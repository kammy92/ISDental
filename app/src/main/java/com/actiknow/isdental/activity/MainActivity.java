package com.actiknow.isdental.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.actiknow.isdental.R;
import com.actiknow.isdental.adapter.HomeServiceAdapter;
import com.actiknow.isdental.helper.DatabaseHandler;
import com.actiknow.isdental.model.Banner;
import com.actiknow.isdental.model.HomeService;
import com.actiknow.isdental.utils.AppConfigTags;
import com.actiknow.isdental.utils.AppConfigURL;
import com.actiknow.isdental.utils.Constants;
import com.actiknow.isdental.utils.CustomImageSlider;
import com.actiknow.isdental.utils.NetworkConnection;
import com.actiknow.isdental.utils.SetTypeFace;
import com.actiknow.isdental.utils.SimpleDividerItemDecoration;
import com.actiknow.isdental.utils.UserDetailsPref;
import com.actiknow.isdental.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bugsnag.android.Bugsnag;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.actiknow.isdental.activity.LoginActivity.PERMISSION_REQUEST_CODE;


public class MainActivity extends AppCompatActivity {
    UserDetailsPref userDetailsPref;
    CoordinatorLayout clMain;
    ImageView ivIndiaSupplyLogo;
    
    ProgressDialog progressDialog;
    
    RecyclerView rvHomeServiceList;
    List<HomeService> homeServices = new ArrayList<> ();
    HomeServiceAdapter homeServiceAdapter;
    SliderLayout slider;
    DatabaseHandler db;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView ();
        initData ();
//        checkPermissions ();
        initListener ();
        isLogin ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvHomeServiceList = (RecyclerView) findViewById (R.id.rvHomeServiceList);
        ivIndiaSupplyLogo = (ImageView) findViewById (R.id.ivIndiaSupplyLogo);
        slider = (SliderLayout) findViewById (R.id.slider);
    }
    
    private void initData () {
        Bugsnag.init (this);
        db = new DatabaseHandler (getApplicationContext ());
        userDetailsPref = UserDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        
        homeServices.add (new HomeService (1, R.drawable.ic_list, "", "BRANDS"));
        homeServices.add (new HomeService (2, R.drawable.ic_program, "", "SHOP ONLINE"));
        homeServices.add (new HomeService (3, R.drawable.ic_program, "", "EVENTS"));
        homeServices.add (new HomeService (4, R.drawable.ic_hall_plan, "", "IS SPECIAL"));
        homeServices.add (new HomeService (5, R.drawable.ic_favourite, "", "OFFERS"));
        homeServices.add (new HomeService (6, R.drawable.ic_information, "", "ABOUT US"));
        
        homeServiceAdapter = new HomeServiceAdapter (this, homeServices);
        rvHomeServiceList.setAdapter (homeServiceAdapter);
        rvHomeServiceList.setHasFixedSize (true);
        rvHomeServiceList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvHomeServiceList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvHomeServiceList.setItemAnimator (new DefaultItemAnimator ());
        
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initListener () {
        ivIndiaSupplyLogo.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri = Uri.parse ("http://indiasupply.com");
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
                
            }
        });
    }
    
    private void initSlider () {
        slider.removeAllSliders ();
        for (int i = 0; i < db.getAllHomeBanners ().size (); i++) {
            final Banner banner = db.getAllHomeBanners ().get (i);
            CustomImageSlider slider2 = new CustomImageSlider (this);
            slider2
                    .image (banner.getImage ())
                    .setScaleType (BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
                        @Override
                        public void onSliderClick (BaseSliderView slider) {
                            Uri uri;
                            if (banner.getUrl ().contains ("http://") || banner.getUrl ().contains ("https://")) {
                                uri = Uri.parse (banner.getUrl ());
                            } else {
                                uri = Uri.parse ("http://" + banner.getUrl ());
                            }
                            Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                            startActivity (intent);
                        }
                    });

//            DefaultSliderView defaultSliderView = new DefaultSliderView (activity);
//            defaultSliderView
//                    .image (image)
//                    .setScaleType (BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
//                        @Override
//                        public void onSliderClick (BaseSliderView slider) {
//                            Intent intent = new Intent (activity, PropertyDetailActivity.class);
//                            intent.putExtra (AppConfigTags.PROPERTY_ID, property.getId ());
//                            activity.startActivity (intent);
//                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
//                        }
//                    });
//
//            defaultSliderView.bundle (new Bundle ());
            // defaultSliderView.getBundle ().putString ("extra", String.valueOf (s));
//            holder.slider.addSlider (defaultSliderView);
            slider.addSlider (slider2);
        }
        slider.getPagerIndicator ().setVisibility (View.GONE);
        slider.setPresetTransformer (SliderLayout.Transformer.Fade);
        slider.setCustomAnimation (new DescriptionAnimation ());
        slider.setDuration (5000);
        slider.addOnPageChangeListener (new ViewPagerEx.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
            
            @Override
            public void onPageSelected (int position) {
            }
            
            @Override
            public void onPageScrollStateChanged (int state) {
                switch (state) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    }
    
    private void isLogin () {
        if (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == "") {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
            finish ();
        } else {
            initApplication ();
        }
    }
    
    private void initApplication () {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
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
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    int status = jsonObj.getInt (AppConfigTags.STATUS);
                                    
                                    if (! error) {
                                        db.deleteAllBanners ();
                                        JSONArray jsonArrayBanner = jsonObj.getJSONArray (AppConfigTags.BANNERS);
                                        ArrayList<Banner> bannerArrayList = new ArrayList<> ();
                                        for (int i = 0; i < jsonArrayBanner.length (); i++) {
                                            JSONObject jsonObjectBanner = jsonArrayBanner.getJSONObject (i);
                                            Banner banner = new Banner (
                                                    jsonObjectBanner.getInt (AppConfigTags.BANNER_ID),
                                                    jsonObjectBanner.getString (AppConfigTags.BANNER_TITLE),
                                                    jsonObjectBanner.getString (AppConfigTags.BANNER_IMAGE),
                                                    jsonObjectBanner.getString (AppConfigTags.BANNER_URL),
                                                    jsonObjectBanner.getString (AppConfigTags.BANNER_TYPE)
                                            );
                                            bannerArrayList.add (banner);
                                        }
                                        db.insertAllBanners (bannerArrayList);
                                        initSlider ();
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
                                                            final String appPackageName = getPackageName ();
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
                    Map<String, String> params = new Hashtable<> ();
                    params.put ("app_version", String.valueOf (finalPInfo.versionCode));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
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
    
    @Override
    public void onBackPressed () {
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
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_ID, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.HEADER_USER_LOGIN_KEY, "");

                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putBooleanPref (MainActivity.this, UserDetailsPref.LOGGED_IN_SESSION, false);


                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
*/
        super.onBackPressed ();
        userDetailsPref.putBooleanPref (MainActivity.this, UserDetailsPref.LOGGED_IN_SESSION, false);
        finish ();
    }
    
    public void checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission (Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
//                    checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        
                requestPermissions (new String[] {
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
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale (permission);
                    if (! showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                        builder.setMessage ("Permission are required please enable them on the App Setting page")
                                .setCancelable (false)
                                .setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                                    public void onClick (DialogInterface dialog, int id) {
                                        dialog.dismiss ();
                                        Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts ("package", getPackageName (), null));
                                        startActivity (intent);
                                    }
                                });
                        AlertDialog alert = builder.create ();
                        alert.show ();
                        // user denied flagging NEVER ASK AGAIN
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    } else if (Manifest.permission.RECEIVE_SMS.equals (permission)) {
//                        Utils.showToast (this, "Camera Permission is required");
//                        showRationale (permission, R.string.permission_denied_contacts);
                        // user denied WITHOUT never ask again
                        // this is a good place to explain the user
                        // why you need the permission and ask if he want
                        // to accept it (the rationale)
                    } else if (Manifest.permission.READ_SMS.equals (permission)) {
                    } else if (Manifest.permission.VIBRATE.equals (permission)) {
                    } else if (Manifest.permission.GET_ACCOUNTS.equals (permission)) {
                    } else if (Manifest.permission.READ_CONTACTS.equals (permission)) {
                    } else if (Manifest.permission.CALL_PHONE.equals (permission)) {
//                    } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals (permission)) {
                    } else if (Manifest.permission.READ_PHONE_STATE.equals (permission)) {
                    }
                }
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
}