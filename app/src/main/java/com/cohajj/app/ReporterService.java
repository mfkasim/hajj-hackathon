package com.cohajj.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ReporterService extends Service {

    public static final String KEY_START_TIME = "key_start_time";
    public static final String STREET_ID = "key_street_id";

    private long startTime;
    private String streetId;


    public ReporterService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime = intent.getLongExtra(KEY_START_TIME,System.currentTimeMillis());
        streetId = intent.getStringExtra(STREET_ID);
        return START_REDELIVER_INTENT;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
