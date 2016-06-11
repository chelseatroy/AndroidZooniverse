package com.chelseatroy.androidzooniverse;

import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

public class GetProjectsResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public GetProjectsResultReceiver() {
        super(null);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.receive(resultCode, resultData);
        }
    }

   public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        void receive(int resultCode, Bundle resultData);
    }
}