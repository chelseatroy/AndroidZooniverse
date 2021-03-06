package com.chelseatroy.androidzooniverse.project;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.chelseatroy.androidzooniverse.R;

public class ProjectDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Uri projectUri = getIntent().getData();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ProjectDetailFragment.newInstance(projectUri))
                .commit();
    }
}
