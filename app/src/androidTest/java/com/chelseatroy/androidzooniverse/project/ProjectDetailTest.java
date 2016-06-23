package com.chelseatroy.androidzooniverse.project;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.chelseatroy.androidzooniverse.R;
import com.chelseatroy.androidzooniverse.helper.DatabaseCleaner;
import com.chelseatroy.androidzooniverse.helper.IntentServiceIdlingResource;
import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProjectDetailTest {
    @Rule
    public IntentsTestRule<ProjectDetailActivity> mActivityTestRule
            = new IntentsTestRule<>(ProjectDetailActivity.class, true, false);
    private Context mTargetContext;

    @Before
    public void setUp() {
        mTargetContext = InstrumentationRegistry.getTargetContext();
        IdlingResource idlingResources = new IntentServiceIdlingResource(mTargetContext);
        Espresso.registerIdlingResources(idlingResources);

        DatabaseCleaner.clean();
    }

    @Test
    public void detail() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, 1);
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.put(ZooniverseContract.Projects.REDIRECT, "cats.com");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);

        Intent startIntent = new Intent();
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);

        onView(withId(R.id.title_text))
                .check(matches(withText("Classify Cats")));
        onView(withId(R.id.description_text))
                .check(matches(withText("For Science")));
    }

    @Test
    public void goToProjectButton_forProjectWithRedirect_goesToRedirect() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, 1);
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.put(ZooniverseContract.Projects.REDIRECT, "http://www.cats.com");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);

        Intent startIntent = new Intent();
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);

        intending(allOf(hasAction(Intent.ACTION_VIEW),
                hasData("http://www.cats.com")))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        onView(withId(R.id.go_to_project_button))
                .perform(click());
    }

    @Test
    public void goToProjectButton_forProjectWithNoRedirect_goesToSlug() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, "1");
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.putNull(ZooniverseContract.Projects.REDIRECT);
        contentValues.put(ZooniverseContract.Projects.SLUG, "cats");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);

        Intent startIntent = new Intent();
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);

        intending(allOf(hasAction(Intent.ACTION_VIEW),
                hasData("https://www.zooniverse.org/projects/cats")))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        onView(withId(R.id.go_to_project_button))
                .perform(click());
    }
}
