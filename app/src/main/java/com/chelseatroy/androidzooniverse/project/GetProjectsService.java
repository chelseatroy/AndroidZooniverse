package com.chelseatroy.androidzooniverse.project;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.chelseatroy.androidzooniverse.BuildConfig;
import com.chelseatroy.androidzooniverse.RequestManager;
import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GetProjectsService extends IntentService {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_INTERRUPTED_ERROR = 1;
    public static final int RESULT_CODE_SERVER_ERROR = 2;

    public static final String EXTRA_COUNT = "count";
    public static final String EXTRA_STATUS_CODE = "statusCode";
    public static final String EXTRA_MESSAGE = "message";

    public GetProjectsService() {
        super(GetProjectsService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestManager requestManager = RequestManager.getInstance(this);
        RequestQueue requestQueue = requestManager.getRequestQueue();
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new GetProjectsRequest(future);
        requestQueue.add(request);

        ResultReceiver receiver = intent.getParcelableExtra(ProjectListFragment.EXTRA_RECEIVER);
        try {
            String response = future.get();
            GetProjects getProjects = new Gson().fromJson(response, GetProjects.class);
            for (Project project : getProjects.projects) {
                ContentValues values = new ContentValues();
                values.put(ZooniverseContract.Projects._ID, project.id);
                values.put(ZooniverseContract.Projects.TITLE, project.title);
                values.put(ZooniverseContract.Projects.DESCRIPTION, project.description);
                getContentResolver().insert(ZooniverseContract.Projects.CONTENT_URI, values);
            }
            Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_COUNT, getProjects.projects.size());

            receiver.send(RESULT_CODE_OK, resultData);
        } catch (InterruptedException e) {
            Bundle resultData = new Bundle();
            resultData.putString(EXTRA_MESSAGE, e.getMessage());

            receiver.send(RESULT_CODE_INTERRUPTED_ERROR, resultData);
        } catch (ExecutionException e) {
            Bundle resultData = new Bundle();
            ServerError serverError = (ServerError) e.getCause();
            resultData.putInt(EXTRA_STATUS_CODE, serverError.networkResponse.statusCode);

            receiver.send(RESULT_CODE_SERVER_ERROR, resultData);
        }
    }

    public static class GetProjectsRequest extends StringRequest {
        public GetProjectsRequest(RequestFuture<String> future) {
            super(BuildConfig.PROJECTS_URL, future, future);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("Accept", "application/vnd.api+json; version=1");
            return params;
        }
    }

    public static class GetProjects {
        public List<Project> projects;
    }

    public static class Project {
        public int id;
        public String title;
        public String description;
    }
}
