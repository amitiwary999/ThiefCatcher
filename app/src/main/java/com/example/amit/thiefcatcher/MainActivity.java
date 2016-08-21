package com.example.amit.thiefcatcher;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    private static final String FENCE_RECEIVER_ACTION = "FENCE_RECEIVE";
    private GoogleApiClient mGoogleApiClient;
    MediaPlayer oursong;
    private Headphonereceiver receiver;
  //  private HeadphoneFenceBroadcastReceiver fenceReceiver;
    private PendingIntent mFencePendingIntent;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        receiver=new Headphonereceiver();
        //fenceReceiver = new HeadphoneFenceBroadcastReceiver();
        Intent intent = new Intent(FENCE_RECEIVER_ACTION);

        mFencePendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                10001,
                intent,
                0);
        txt=(TextView)findViewById(R.id.txt);
    }
    private void registerFences() {
        // Create a fence.
        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence("headphoneFenceKey", headphoneFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                           // Log.i(Tag, "Fence was successfully registered.");
                            txt.setText("Ok");
                        } else {
                           // Log.e(TAG, "Fence could not be registered: " + status);
                            txt.setText("Wrong");
                        }
                    }
                });

    }

    private void unregisterFence() {
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence("headphoneFenceKey")
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                //Log.i(TAG, "Fence " + "headphoneFenceKey" + " successfully removed.");
                txt.setText("Fence " + "headphoneFenceKey" + " successfully removed.");
            }

            @Override
            public void onFailure(@NonNull Status status) {
               // Log.i(TAG, "Fence " + "headphoneFenceKey" + " could NOT be removed.");
                txt.setText("Fence " + "headphoneFenceKey" + " could NOT be removed.");
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerFences();
        registerReceiver(receiver, new IntentFilter(FENCE_RECEIVER_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterFence();
        unregisterReceiver(receiver);
    }

}
/*class HeadphoneFenceBroadcastReceiver extends BroadcastReceiver {
MediaPlayer song;
    int check=0;
   // private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);

       // Log.d(TAG, "Fence Receiver Received");

        if (TextUtils.equals(fenceState.getFenceKey(), "headphoneFenceKey")) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                  //  Log.i(TAG, "Fence > Headphones are plugged in.");
                    if(check==1){
                        song.release();
                    }

                    break;
                case FenceState.FALSE:
                   // Log.i(TAG, "Fence > Headphones are NOT plugged in.");
                    song=MediaPlayer.create(context,R.raw.sng);
                    song.start();
                    check=1;
                    break;
                case FenceState.UNKNOWN:
                   // Log.i(TAG, "Fence > The headphone fence is in an unknown state.");
                    song=MediaPlayer.create(context,R.raw.sng);
                    song.start();
                    check=1;
                    break;
            }
        }

    }

}*/

