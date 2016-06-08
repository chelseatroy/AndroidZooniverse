package com.example.chelseatroy.androidzooniverse;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.CursorMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.RequestQueue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Set;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProjectListScreenTest {
    @Rule
    public ActivityTestRule<ProjectListActivity> mActivityTestRule = new ActivityTestRule<>(ProjectListActivity.class);

    @Before
    public void setUp() {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        IdlingResource idlingResources = new VolleyIdlingResource(targetContext);
        Espresso.registerIdlingResources(idlingResources);
    }

    @Test
    public void showsProjects() {
        onData(withRowString(1, "Snapshot Supernova"))
                .check(matches(isDisplayed()));
        onData(withRowString(2, "Help in the hunt for supernovae, live!"))
                .check(matches(isDisplayed()));
    }

    private static class VolleyIdlingResource implements IdlingResource {
        private Context mContext;
        private ResourceCallback mCallback;

        public VolleyIdlingResource(Context context) {
            mContext = context;
        }

        @Override
        public String getName() {
            return "VolleyIdlingResource";
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
            Field mCurrentRequests = null;
            try {
                mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            try {
                assert mCurrentRequests != null;
                mCurrentRequests.setAccessible(true);
                RequestManager requestManager = RequestManager.getInstance(mContext);
                RequestQueue requestQueue = requestManager.getRequestQueue();
                Set set = (Set) mCurrentRequests.get(requestQueue);
                return !set.isEmpty();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
