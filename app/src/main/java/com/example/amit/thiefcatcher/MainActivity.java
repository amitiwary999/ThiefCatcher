package com.example.amit.thiefcatcher;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences myPrefs;
   // private SharedPreferences.Editor editor;
    private static final String FENCE_RECEIVER_ACTION = "FENCE_RECEIVE";
    private GoogleApiClient mGoogleApiClient;
    private Switch myswitch;
    private Headphonereceiver receiver;
  // private HeadphoneFenceBroadcastReceiver fenceReceiver;
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
       // fenceReceiver = new HeadphoneFenceBroadcastReceiver();
        Intent intent = new Intent(FENCE_RECEIVER_ACTION);

        mFencePendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                10001,
                intent,
                0);
        myswitch=(Switch)findViewById(R.id.switch1);
        SharedPreferences myPrefs=getSharedPreferences("com.example.amit.thiefcatcher",MODE_PRIVATE);
        myswitch.setChecked(myPrefs.getBoolean("isChecked",true));
        myswitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=getSharedPreferences("com.example.amit.thiefcatcher",MODE_PRIVATE).edit();
                editor.putBoolean("isChecked",myswitch.isChecked());
                editor.commit();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                moveTaskToBack(true);

                return true;
        }
        return false;
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
                            Toast.makeText(MainActivity.this,"Successfully registered headphone",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this,"Headphone not registered ",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

  /*  private void unregisterFence() {
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
    }*/
    @Override
    protected void onStart() {
        super.onStart();
        registerFences();
        registerReceiver(receiver, new IntentFilter(FENCE_RECEIVER_ACTION));
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        unregisterFence();
        unregisterReceiver(fenceReceiver);
    }*/

}


