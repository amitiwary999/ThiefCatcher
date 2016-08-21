package com.example.amit.thiefcatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;

/**
 * Created by amit on 20/8/16.
 */
public class Headphonereceiver extends BroadcastReceiver {
    MediaPlayer song;
    int check=0;
    private SharedPreferences myPrefs;
    private static final int MODE_WORLD_READABLE = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String headphoneMode=myPrefs.getString("mode", "not retrieved");
        if(headphoneMode.equals("on")) {

            if (TextUtils.equals(fenceState.getFenceKey(), "headphoneFenceKey")) {
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        if (check == 1) {
                            song.release();
                        }

                        break;
                    case FenceState.FALSE:
                        song = MediaPlayer.create(context, R.raw.sng);
                        song.start();
                        check = 1;
                        break;
                    case FenceState.UNKNOWN:
                        song = MediaPlayer.create(context, R.raw.sng);
                        song.start();
                        check = 1;
                        break;
                }
            }
        }else{
            Toast.makeText(context,"Off mode",Toast.LENGTH_LONG).show();
        }

    }
}
