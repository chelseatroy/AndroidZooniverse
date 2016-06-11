package com.example.chelseatroy.androidzooniverse.helper;

import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.android.volley.RequestQueue;
import com.example.chelseatroy.androidzooniverse.RequestManager;

import java.lang.reflect.Field;
import java.util.Set;

public class VolleyIdlingResource implements IdlingResource {
    private RequestQueue mRequestQueue;
    private Field mMCurrentRequests;
    private ResourceCallback mCallback;

    public VolleyIdlingResource(Context context) {
        mRequestQueue = RequestManager.getInstance(context).getRequestQueue();
        mMCurrentRequests = null;
        try {
            mMCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        mMCurrentRequests.setAccessible(true);
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !pendingRequests();
        if (idle && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    private boolean pendingRequests() {
        Set set;
        try {
            set = (Set) mMCurrentRequests.get(mRequestQueue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return !set.isEmpty();
    }
}
