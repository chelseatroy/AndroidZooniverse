package com.chelseatroy.androidzooniverse.project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chelseatroy.androidzooniverse.R;
import com.chelseatroy.androidzooniverse.helper.IntentServiceIdlingResource;
import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProjectDetailScreenTest {
    @Rule
    public ActivityTestRule<ProjectDetailActivity> mActivityTestRule
            = new ActivityTestRule<>(ProjectDetailActivity.class, true, false);
    private Context mTargetContext;

    @Before
    public void setUp() {
        mTargetContext = InstrumentationRegistry.getTargetContext();
        IdlingResource idlingResources = new IntentServiceIdlingResource(mTargetContext);
        Espresso.registerIdlingResources(idlingResources);
    }

    @Test
    public void detail() {
        Intent startIntent = new Intent();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, "1");
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.put(ZooniverseContract.Projects.REDIRECT, "cats.com");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);
        onView(withId(R.id.title_text))
                .check(matches(withText("Classify Cats")));
        onView(withId(R.id.description_text))
                .check(matches(withText("For Science")));
    }

    @Test
    public void clickingButton_projectWithRedirect_goesToProjectWithRedirect() {
        Intent startIntent = new Intent();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, "1");
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.put(ZooniverseContract.Projects.REDIRECT, "http://www.cats.com");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);

        Matcher<Intent> expectedIntent = allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("http://www.cats.com"));
        Intents.init();

        onView(withId(R.id.go_to_project_button)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }

    @Test
    public void clickingButton_projectWithNoRedirect_goesToProjectWithSlug() {
        Intent startIntent = new Intent();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ZooniverseContract.Projects._ID, "1");
        contentValues.put(ZooniverseContract.Projects.TITLE, "Classify Cats");
        contentValues.put(ZooniverseContract.Projects.DESCRIPTION, "For Science");
        contentValues.put(ZooniverseContract.Projects.SLUG, "cats");
        Uri uri = mTargetContext.getContentResolver().insert(
                ZooniverseContract.Projects.CONTENT_URI, contentValues);
        startIntent.setData(uri);

        mActivityTestRule.launchActivity(startIntent);

        Matcher<Intent> expectedIntent = allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("https://www.zooniverse.org/projects/cats"));
        Intents.init();

        onView(withId(R.id.go_to_project_button)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }
}
