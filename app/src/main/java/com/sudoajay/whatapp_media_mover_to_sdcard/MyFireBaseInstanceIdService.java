package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refresh_Token = FirebaseInstanceId.getInstance().getToken();
        Log.i("Tokensad - " , refresh_Token);
    }
}
