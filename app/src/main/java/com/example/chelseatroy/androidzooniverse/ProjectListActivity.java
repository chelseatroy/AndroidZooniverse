package com.example.chelseatroy.androidzooniverse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int PROJECTS = 0;
    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        ListView listView = (ListView) findViewById(android.R.id.list);

        mAdapter = new ProjectListCursorAdapter();
        listView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(PROJECTS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestQueue requestQueue = RequestManager.getInstance(this).getRequestQueue();
        StringRequest request = new StringRequest(
                "https://panoptes.zooniverse.org/api/projects",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Projects projects = new Gson().fromJson(response, Projects.class);
                        for (Project project : projects.projects) {

                            ContentValues values = new ContentValues();
                            values.put(ZooniverseContract.Projects._ID, project.id);
                            values.put(ZooniverseContract.Projects.TITLE, project.title);

                            getContentResolver().insert(ZooniverseContract.Projects.CONTENT_URI, values);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error = " + error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/vnd.api+json; version=1");

                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ZooniverseContract.Projects.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    public static class Projects {
        public List<Project> projects;
    }

    public static class Project {
        public int id;
        public String title;
    }

    private class ProjectListCursorAdapter extends CursorAdapter {
        public ProjectListCursorAdapter() {
            super(ProjectListActivity.this, null, 0);
        }

        class ViewHolder {
            public TextView mTitleTextView;

            public ViewHolder(View view) {
                mTitleTextView = (TextView) view.findViewById(android.R.id.text1);

            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.mTitleTextView.setText(cursor.getString(cursor.getColumnIndex(ZooniverseContract.Projects.TITLE)));
        }
    }
}
