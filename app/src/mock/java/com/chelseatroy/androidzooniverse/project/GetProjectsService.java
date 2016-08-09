package com.chelseatroy.androidzooniverse.project;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.chelseatroy.androidzooniverse.provider.ZooniverseContract;

public class GetProjectsService extends IntentService {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_INTERRUPTED_ERROR = 1;
    public static final int RESULT_CODE_SERVER_ERROR = 2;

    private static final String EXTRA_RECEIVER = "receiver";
    public static final String EXTRA_COUNT = "count";
    public static final String EXTRA_STATUS_CODE = "statusCode";
    public static final String EXTRA_MESSAGE = "message";

    public GetProjectsService() {
        super(GetProjectsService.class.getName());
    }

    public static Intent newIntent(Context context, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, GetProjectsService.class);
        intent.putExtra(EXTRA_RECEIVER, resultReceiver);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContentValues values = new ContentValues();
        values.put(ZooniverseContract.Projects._ID, 1);
        values.put(ZooniverseContract.Projects.TITLE, "Snapshot Supernova");
        values.put(ZooniverseContract.Projects.DESCRIPTION, "Help in the hunt for supernovae, live!");
        getContentResolver()
                .insert(ZooniverseContract.Projects.CONTENT_URI, values);

        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_COUNT, 1);
        receiver.send(RESULT_CODE_OK, resultData);
    }
}
