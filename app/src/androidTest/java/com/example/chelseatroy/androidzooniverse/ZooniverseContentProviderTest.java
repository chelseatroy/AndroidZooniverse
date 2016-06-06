package com.example.chelseatroy.androidzooniverse;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ZooniverseContentProviderTest extends ProviderTestCase2<ZooniverseContentProvider> {
    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    public ZooniverseContentProviderTest() {
        super(ZooniverseContentProvider.class, "com.example.chelseatroy.androidzooniverse.provider");
    }

    @Override
    @Before
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @Test
    public void insert() {
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put("title", "Project Supernova");

        Uri projectsUri = Uri.parse("content://com.example.chelseatroy.androidzooniverse.provider/projects");
        Uri newUri = getMockContentResolver().insert(
                projectsUri,
                values
        );
        assertThat(newUri, is(ContentUris.withAppendedId(projectsUri, 1L)));
    }

    @Test
    public void insert_throwsException_givenUnknownUri() {
        ContentValues values = new ContentValues();
        Uri unknownUri = Uri.parse("content://com.example.chelseatroy.androidzooniverse.provider/unknown-uri");

        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Unknown uri: content://com.example.chelseatroy.androidzooniverse.provider/unknown-uri");

        getMockContentResolver()
                .insert(unknownUri, values);
    }

    @Test
    public void query() {
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put("title", "Project Supernova");

        Uri projectsUri = Uri.parse("content://com.example.chelseatroy.androidzooniverse.provider/projects");
        Uri newUri = getMockContentResolver().insert(
                projectsUri,
                values
        );

        Cursor result = getMockContentResolver().query(
                projectsUri,
                null, null,
                null,
                null
        );

        assertThat(result.getCount(), is(1));
        result.moveToFirst();
        assertThat(result.getInt(0), is(1));
        assertThat(result.getString(1), is("Project Supernova"));
    }

    @Test
    public void query_throwsException_givenUnknownUri() {
        Uri unknownUri = Uri.parse("content://com.example.chelseatroy.androidzooniverse.provider/unknown-uri");

        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Unknown uri: content://com.example.chelseatroy.androidzooniverse.provider/unknown-uri");

        getMockContentResolver()
                .query(unknownUri, null, null, null, null);
    }

}
