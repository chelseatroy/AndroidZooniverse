package com.chelseatroy.androidzooniverse.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ZooniverseContentProviderTest extends ProviderTestCase2<ZooniverseContentProvider> {
    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    MockContentResolver mMockContentResolver;

    public ZooniverseContentProviderTest() {
        super(ZooniverseContentProvider.class, ZooniverseContract.AUTHORITY);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
        mMockContentResolver = getMockContentResolver();
    }

    @Test
    public void insert() {
        ContentValues values = new ContentValues();
        values.put(ZooniverseContract.Projects._ID, 1);
        values.put(ZooniverseContract.Projects.TITLE, "Project Supernova");

        Uri projectsUri = ZooniverseContract.Projects.CONTENT_URI;
        Uri newUri = mMockContentResolver.insert(
                projectsUri,
                values
        );
        assertThat(newUri, is(ContentUris.withAppendedId(projectsUri, 1L)));
    }

    @Test
    public void insert_throwsException_givenUnknownUri() {
        ContentValues values = new ContentValues();
        Uri unknownUri = Uri.withAppendedPath(ZooniverseContract.CONTENT_URI, "unknown-path");

        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Unknown uri: content://com.chelseatroy.androidzooniverse.provider/unknown-path");

        mMockContentResolver
                .insert(unknownUri, values);
    }

    @Test
    public void insert_replacesExistingRecordOnConflict() {
        ContentValues values = new ContentValues();
        values.put(ZooniverseContract.Projects._ID, 1);

        Uri projectsUri = ZooniverseContract.Projects.CONTENT_URI;
        mMockContentResolver.insert(
                projectsUri,
                values
        );
        Uri newUri = mMockContentResolver.insert(
                projectsUri,
                values
        );
        assertThat(newUri, is(ContentUris.withAppendedId(projectsUri, 1L)));
    }

    @Test
    public void query() {
        ContentValues values = new ContentValues();
        values.put(ZooniverseContract.Projects._ID, 1);
        values.put(ZooniverseContract.Projects.TITLE, "Project Supernova");

        Uri projectsUri = ZooniverseContract.Projects.CONTENT_URI;
        mMockContentResolver.insert(
                projectsUri,
                values
        );

        Cursor cursor = mMockContentResolver.query(
                projectsUri,
                null,
                null,
                null,
                null
        );

        assert cursor != null;
        assertThat(cursor.getCount(), is(1));
        cursor.moveToFirst();
        assertThat(cursor.getInt(cursor.getColumnIndex(ZooniverseContract.Projects._ID)), is(1));
        assertThat(cursor.getString(cursor.getColumnIndex(ZooniverseContract.Projects.TITLE)), is("Project Supernova"));
        cursor.close();
    }

    @Test
    public void query_forSingleProject() {
        ContentValues values1 = new ContentValues();
        values1.put(ZooniverseContract.Projects._ID, 1);
        values1.put("title", "Project Supernova");
        ContentValues values2 = new ContentValues();
        values2.put(ZooniverseContract.Projects._ID, 2);
        values2.put("title", "Ice Hunters");

        Uri projectsUri = ZooniverseContract.Projects.CONTENT_URI;
        mMockContentResolver.insert(
                projectsUri,
                values1
        );
        mMockContentResolver.insert(
                projectsUri,
                values2
        );

        Cursor cursor = mMockContentResolver.query(
                ContentUris.withAppendedId(projectsUri, 2),
                null,
                null,
                null,
                null
        );

        assert cursor != null;
        assertThat(cursor.getCount(), is(1));
        cursor.moveToFirst();
        assertThat(cursor.getInt(cursor.getColumnIndex(ZooniverseContract.Projects._ID)), is(2));
        assertThat(cursor.getString(cursor.getColumnIndex(ZooniverseContract.Projects.TITLE)), is("Ice Hunters"));
        cursor.close();
    }

    @Test
    public void query_throwsException_givenUnknownUri() {
        Uri unknownUri = Uri.withAppendedPath(ZooniverseContract.CONTENT_URI, "unknown-path");

        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Unknown uri: content://com.chelseatroy.androidzooniverse.provider/unknown-path");

        mMockContentResolver
                .query(unknownUri, null, null, null, null);
    }

}
