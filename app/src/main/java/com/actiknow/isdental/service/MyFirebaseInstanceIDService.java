package com.actiknow.isdental.service;

import com.actiknow.isdental.utils.UserDetailsPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName ();

    @Override
    public void onTokenRefresh () {
        super.onTokenRefresh ();
        UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
        userDetailsPref.putStringPref (getApplicationContext (), UserDetailsPref.USER_FIREBASE_ID, FirebaseInstanceId.getInstance ().getToken ());
    }
}

