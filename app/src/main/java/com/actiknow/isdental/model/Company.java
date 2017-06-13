package com.actiknow.isdental.model;


import java.util.ArrayList;

public class Company {
    int id;
    String Brand_logo, Brand_name, Brand_description;
    ArrayList<StallDetail> stallDetailList = new ArrayList<> ();

    public Company(int id, String Brand_logo, String Brand_name, String Brand_description) {
        this.id = id;
        this.Brand_logo = Brand_logo;
        this.Brand_name = Brand_name;
        this.Brand_description = Brand_description;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getBrand_logo () {
        return Brand_logo;
    }

    public void setBrand_logo (String Brand_logo) {
        this.Brand_logo = Brand_logo;
    }

    public String getBrand_name () {
        return Brand_name;
    }

    public void setBrand_name (String Brand_name) {
        this.Brand_name = Brand_name;
    }

    public ArrayList<StallDetail> getStallDetailList () {
        return stallDetailList;
    }

    public void setStallDetailList (ArrayList<StallDetail> stallDetailList) {
        this.stallDetailList = stallDetailList;
    }

    public void setStallDetailInList (StallDetail stallDetail) {
        this.stallDetailList.add (stallDetail);
    }

    public void clearStallDetailList () {
        this.stallDetailList.clear ();
    }

    public String getBrand_description () {
        return Brand_description;
    }

    public void setBrand_description (String Brand_description) {
        this.Brand_description = Brand_description;
    }
}