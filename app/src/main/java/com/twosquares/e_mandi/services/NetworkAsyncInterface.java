package com.twosquares.e_mandi.services;

import java.util.HashMap;

/**
 * Created by Prashant Kumar on 2/12/2018.
 */

public interface NetworkAsyncInterface{
    public void onResponse(int requestCode, HashMap<Response, Object> response);
}
