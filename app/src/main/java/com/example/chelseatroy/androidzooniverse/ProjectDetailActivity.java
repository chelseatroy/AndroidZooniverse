package com.example.chelseatroy.androidzooniverse;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ProjectDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_detail);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, getIntent().getData(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        TextView titleTextView = (TextView) findViewById(R.id.title_text);
        titleTextView.setText(data.getString(data.getColumnIndex(ZooniverseContract.Projects.TITLE)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
