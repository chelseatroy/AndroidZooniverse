package com.chelseatroy.androidzooniverse.project;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chelseatroy.androidzooniverse.R;
import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

public class ProjectListCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ProjectListCursorAdapter(Context context) {
        super(context, null, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.project_list_item, parent, false);
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
