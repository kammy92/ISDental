package com.actiknow.isdental.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actiknow.isdental.R;
import com.actiknow.isdental.adapter.BrandsContactDetailsAdapter;
import com.actiknow.isdental.model.BrandsContactDetails;
import com.actiknow.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
}
