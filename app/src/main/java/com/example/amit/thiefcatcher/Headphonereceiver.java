package com.example.amit.thiefcatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

/**
 * Created by amit on 20/8/16.
 */
public class Headphonereceiver extends BroadcastReceiver {
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
}
