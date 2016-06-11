package com.chelseatroy.androidzooniverse;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

public class ProjectDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, ProjectDetailFragment.newInstance(getIntent().getData()))
                .commit();
    }

    public static class ProjectDetailFragment extends Fragment {
        private static final String ARG_DATA = "data";
        private static final int PROJECT_LOADER = 0;

        private Uri mData;

        public static Fragment newInstance(Uri data) {
            Bundle args = new Bundle();
            args.putParcelable(ARG_DATA, data);
            Fragment fragment = new ProjectDetailFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mData = getArguments().getParcelable(ARG_DATA);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_project_detail, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();

            getLoaderManager().initLoader(PROJECT_LOADER, null, new ProjectDetailLoaderCallbacks());
        }

        public class ProjectDetailLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        getActivity(),
                        mData,
                        null,
                        null,
                        null,
                        null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                data.moveToFirst();

                View view = getView();
                TextView titleTextView = (TextView) view.findViewById(R.id.title_text);
                titleTextView.setText(data.getString(data.getColumnIndex(ZooniverseContract.Projects.TITLE)));

                TextView textView = (TextView) view.findViewById(R.id.description_text);
                textView.setText(data.getString(data.getColumnIndex(ZooniverseContract.Projects.DESCRIPTION)));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // noop
            }
        }
    }
}
