package org.coursera.potlatch4u.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PhotoUploadService extends Service {
    private final int MAX_THREADS = 3;

    private ExecutorService mExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // not a bind service
        return null;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PhotoUploadService.class);

        return intent;
    }

}
