package com.actiknow.isdental.utils;

public class AppConfigURL {
    public static String version = "v1.0.4";
    //    public static String BASE_URL = "https://project-isdental-cammy92.c9users.io/api/" + version + "/";
    public static String BASE_URL = "http://famdent.indiasupply.com/isdental/api/" + version + "/";
    
    
    public static String URL_GETOTP = BASE_URL + "user/otp";
    
    public static String URL_REGISTER = BASE_URL + "user/register";
    
    public static String URL_INIT = BASE_URL + "/init/application";
    
    public static String URL_COMPANY_LIST = BASE_URL + "company";
    public static String URL_COMPANY_DETAILS = BASE_URL + "company";
}
