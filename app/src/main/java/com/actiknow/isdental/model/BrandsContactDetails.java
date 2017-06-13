package com.actiknow.isdental.model;

import java.util.ArrayList;

/**
 * Created by Admin on 16-05-2017.
 */

public class BrandsContactDetails {
    int id;
    String contact_person, address, email, website;
    ArrayList<String> contactList = new ArrayList<> ();

    public BrandsContactDetails(int id, String contact_person, String address, String email, String website, ArrayList<String> contactList) {
        this.id = id;
        this.contact_person = contact_person;
        this.address = address;
        this.email = email;
        this.website = website;
        this.contactList = contactList;

    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<String> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<String> contactList) {
        this.contactList = contactList;
    }
}
