package com.chelseatroy.androidzooniverse.project;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chelseatroy.androidzooniverse.R;
import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;
import com.etsy.android.grid.StaggeredGridView;

import java.util.Locale;

public class ProjectListFragment extends Fragment implements GetProjectsResultReceiver.Receiver {
    private static final int PROJECTS_LOADER = 0;
    public static final String EXTRA_RECEIVER = "receiver";

    private GetProjectsResultReceiver mGetProjectsResultReceiver;
    private OnProjectSelectedListener mOnProjectSelectedListener;
    private CursorAdapter mCursorAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnProjectSelectedListener = ((OnProjectSelectedListener) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetProjectsResultReceiver = new GetProjectsResultReceiver();
        mGetProjectsResultReceiver.setReceiver(this);
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
                Uri projectUri = ContentUris.withAppendedId(ZooniverseContract.Projects.CONTENT_URI, id);
                mOnProjectSelectedListener.onProjectSelected(projectUri, view);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(PROJECTS_LOADER, null, new ProjectListLoaderCallbacks());

        Intent service = new Intent(
                Intent.ACTION_VIEW,
                ZooniverseContract.Projects.CONTENT_URI,
                getActivity(),
                GetProjectsService.class
        );
        service.putExtra(EXTRA_RECEIVER, mGetProjectsResultReceiver);
        getActivity().startService(service);
    }

    @Override
    public void onPause() {
        super.onPause();
        mGetProjectsResultReceiver.setReceiver(null);
    }

    @Override
    public void receive(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case GetProjectsService.RESULT_CODE_OK:
                Snackbar
                        .make(getView(), String.format(Locale.US, "Got %d projects", resultData.getInt(GetProjectsService.EXTRA_COUNT)), Snackbar.LENGTH_LONG)
                        .show();
                break;
            case GetProjectsService.RESULT_CODE_SERVER_ERROR:
                Snackbar
                        .make(getView(), String.format(Locale.US, "Error fetching projects: %d", resultData.getInt(GetProjectsService.EXTRA_STATUS_CODE)), Snackbar.LENGTH_LONG)
                        .show();
                break;
            case GetProjectsService.RESULT_CODE_INTERRUPTED_ERROR:
                Snackbar
                        .make(getView(), String.format(Locale.US, "Error fetching projects: %s", resultData.getInt(GetProjectsService.EXTRA_MESSAGE)), Snackbar.LENGTH_LONG)
                        .show();
                break;
        }
    }

    public interface OnProjectSelectedListener {
        void onProjectSelected(Uri projectUri, View view);
    }

    public class ProjectListLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
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
}
