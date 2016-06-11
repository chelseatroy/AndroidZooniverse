package com.chelseatroy.androidzooniverse.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.chelseatroy.androidzooniverse.ProjectListActivity;

public class IntentServiceIdlingResource implements IdlingResource {
    private Context mContext;
    private ResourceCallback mResourceCallback;

    public IntentServiceIdlingResource(Context context) {
        this.mContext = context;
    }

    @Override
    public String getName() {
        return IntentServiceIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !isIntentServiceRunning();
        if (idle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.mResourceCallback = resourceCallback;
    }

    private boolean isIntentServiceRunning() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ProjectListActivity.GetProjectsService.class.getName().equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}