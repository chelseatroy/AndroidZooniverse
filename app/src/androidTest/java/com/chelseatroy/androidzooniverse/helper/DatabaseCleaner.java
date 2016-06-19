package com.chelseatroy.androidzooniverse.helper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

public class DatabaseCleaner {
    public static int clean() {
        Context targetContext= InstrumentationRegistry.getTargetContext();
        return targetContext
                .getContentResolver()
                .delete(ZooniverseContract.Projects.CONTENT_URI, null, null);
    }
}