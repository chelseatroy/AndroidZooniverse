package com.chelseatroy.androidzooniverse.project;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chelseatroy.androidzooniverse.R;

import java.lang.annotation.Target;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ProjectListActivity extends AppCompatActivity implements ProjectListFragment.OnProjectSelectedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ProjectListFragment())
                .commit();
    }

    @Override
    @TargetApi(15)
    public void onProjectSelected(Uri projectUri, View view) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                projectUri,
                this,
                ProjectDetailActivity.class
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> p1 = Pair.create(view.findViewById(R.id.title_text), "titleToDetail");
            Pair<View, String> p2 = Pair.create(view.findViewById(R.id.description_text), "descriptionToDetail");
            ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(this, p1, p2);
            startActivity(intent, optionsCompat.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
