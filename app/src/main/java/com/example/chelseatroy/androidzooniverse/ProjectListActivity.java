package com.example.chelseatroy.androidzooniverse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.app.ActivityOptionsCompat.*;

public class ProjectListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ProjectListFragment())
                .commit();
    }

    public static class ProjectListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final int PROJECTS_LOADER = 0;

        private CursorAdapter mCursorAdapter;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_project_list, container, false);

            mCursorAdapter = new ProjectListCursorAdapter(getActivity());

            StaggeredGridView staggeredGridView = (StaggeredGridView) view.findViewById(R.id.grid);
            staggeredGridView.setAdapter(mCursorAdapter);
            staggeredGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // animation
                    Pair<View, String> p1 = Pair.create(view.findViewById(R.id.title_text), "titleToDetail");
                    Pair<View, String> p2 = Pair.create(view.findViewById(R.id.description_text), "descriptionToDetail");
                    ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(getActivity(), p1, p2);

                    // go to detail
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            ContentUris.withAppendedId(ZooniverseContract.Projects.CONTENT_URI, id),
                            getActivity(),
                            ProjectDetailActivity.class
                    );
                    getActivity().startActivity(intent, optionsCompat.toBundle());
                }
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();

            getLoaderManager()
                    .initLoader(PROJECTS_LOADER, null, this);

            RequestQueue requestQueue = RequestManager.getInstance(getActivity()).getRequestQueue();
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
                                values.put(ZooniverseContract.Projects.DESCRIPTION, project.description);

                                getActivity()
                                        .getContentResolver()
                                        .insert(ZooniverseContract.Projects.CONTENT_URI, values);
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
                    getActivity(),
                    ZooniverseContract.Projects.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mCursorAdapter.changeCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mCursorAdapter.changeCursor(null);
        }
    }

    public static class ProjectListCursorAdapter extends CursorAdapter {
        public ProjectListCursorAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.project_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.mTitleTextView.setText(cursor.getString(cursor.getColumnIndex(ZooniverseContract.Projects.TITLE)));
            viewHolder.mDescriptionTextView.setText(cursor.getString(cursor.getColumnIndex(ZooniverseContract.Projects.DESCRIPTION)));
        }

        public static class ViewHolder {
            public TextView mTitleTextView;
            public TextView mDescriptionTextView;

            public ViewHolder(View view) {
                mTitleTextView = (TextView) view.findViewById(R.id.title_text);
                mDescriptionTextView = (TextView) view.findViewById(R.id.description_text);
            }
        }
    }

    public static class Projects {
        public List<Project> projects;
    }

    public static class Project {
        public int id;
        public String title;
        public String description;
    }
}
