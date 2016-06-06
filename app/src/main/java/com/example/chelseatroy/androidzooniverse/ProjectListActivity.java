package com.example.chelseatroy.androidzooniverse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
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

                        ListView listView = (ListView) findViewById(android.R.id.list);
                        List<String> strings = new ArrayList<>();
                        for (Project project : projects.projects) {
                            strings.add(project.title);
                        }

                        ListAdapter adapter = new ArrayAdapter<>(
                                ProjectListActivity.this,
                                android.R.layout.simple_list_item_1,
                                strings);

                        listView.setAdapter(adapter);
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

    public static class Projects {
        public List<Project> projects;
    }

    public static class Project {
        public int id;
        public String title;
    }
}
