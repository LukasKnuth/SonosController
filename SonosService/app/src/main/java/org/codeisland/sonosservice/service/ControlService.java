package org.codeisland.sonosservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import org.codeisland.sonosservice.BuildConfig;
import org.codeisland.sonosservice.network.Api;
import org.codeisland.sonosservice.network.model.Status;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lukas on 30.03.16.
 */
public class ControlService extends Service {

    private static final String LOG_TAG = "SonosService";
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        // initial setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        // register receivers
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new CallReceiver(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Currently not bindable.
        return null;
    }

    /**
     * Listens for changes in the telephony state
     */
    private class CallReceiver extends PhoneStateListener {

        private static final int PREV_STATE_UNSET = -41;
        private int previousState = PREV_STATE_UNSET;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    // User is getting a call:
                    api.callReceived().enqueue(new SimpleCallback());
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // The user started a call:
                    api.callReceived().enqueue(new SimpleCallback());
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (previousState != PREV_STATE_UNSET && previousState == TelephonyManager.CALL_STATE_OFFHOOK){
                        /*
                            The user hung up when the stat changes from OFFHOOK to IDLE.
                            Via http://stackoverflow.com/a/2478028/717341
                        */
                        api.callEnded().enqueue(new SimpleCallback());
                    }
                    break;
            }
            previousState = state;
        }
    }

    private class SimpleCallback implements Callback<Status> {

        @Override
        public void onResponse(Call<Status> call, Response<Status> response) {
            if (!response.isSuccessful()){
                Log.e(LOG_TAG, "Call unsuccessful! (!= 200)");
            } else {
                Status body = response.body();
                if (!body.isOk()){
                    Log.e(LOG_TAG, "Status not OK: " + body.getStatus());
                }
            }
        }

        @Override
        public void onFailure(Call<Status> call, Throwable t) {
            Log.e(LOG_TAG, "Call failed!", t);
        }

    }

}
