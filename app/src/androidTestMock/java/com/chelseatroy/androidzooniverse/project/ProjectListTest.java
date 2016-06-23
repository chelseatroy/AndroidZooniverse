package com.chelseatroy.androidzooniverse.project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chelseatroy.androidzooniverse.R;
import com.chelseatroy.androidzooniverse.helper.DatabaseCleaner;
import com.chelseatroy.androidzooniverse.helper.IntentServiceIdlingResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProjectListTest {
    @Rule
    public ActivityTestRule<ProjectListActivity> mActivityTestRule
            = new ActivityTestRule<>(ProjectListActivity.class);

    @Before
    public void setUp() {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        IdlingResource idlingResources = new IntentServiceIdlingResource(targetContext);
        Espresso.registerIdlingResources(idlingResources);

        DatabaseCleaner.clean();
    }

    @Test
    public void listAndDetail() {
        // list
        onData(allOf(withRowString("title", "Snapshot Supernova"),
                withRowString("description", "Help in the hunt for supernovae, live!")))
                .perform(click());

        // detail
        onView(withId(R.id.title_text))
                .check(matches(withText("Snapshot Supernova")));
    }
}
