package com.chelseatroy.androidzooniverse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ProjectListActivity extends AppCompatActivity implements ProjectListFragment.OnProjectSelectedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ProjectListFragment())
                .commit();
    }

    @Override
    public void onProjectSelected(Uri projectUri) {
        Pair<View, String> p1 = Pair.create(findViewById(R.id.title_text), "titleToDetail");
        Pair<View, String> p2 = Pair.create(findViewById(R.id.description_text), "descriptionToDetail");
        ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(this, p1, p2);

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                projectUri,
                this,
                ProjectDetailActivity.class
        );
        startActivity(intent, optionsCompat.toBundle());
    }
}
