package com.test.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class AIDLService extends Service {

    private static final String TAG = "AIDLService";
    private int progress = 0;
    final RemoteCallbackList<IGreetCallback> mCallbacks = new RemoteCallbackList <IGreetCallback>();

    IGreetBinder.Stub stub = new IGreetBinder.Stub() {
        @Override
        public String greet(String someone) throws RemoteException {
            Log.i(TAG, "greet() called");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        progress++;
                        callback(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return "hello, " + someone;
        }

        @Override
        public void registerCallback(IGreetCallback cb) throws RemoteException {
            if (cb != null) {
                mCallbacks.register(cb);
            }
        }

        @Override
        public void unregisterCallback(IGreetCallback cb) throws RemoteException {
            if (cb != null) {
                mCallbacks.register(cb);
            }
        }
    };

    void callback(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).greetBack("hi person"+val);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind() called");
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind() called");
        return true;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() called");
    }
}
