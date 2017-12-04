package edu.kaist.khw.overwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_APPNAME = "TAG_APPNAME";
    private static final String TAG_NOTICONTENT = "TAG_NOTICONTENT";
    private static GoogleApiClient mGoogleApiClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button mButton = (Button) findViewById(R.id.button2);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG_APPNAME, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG_APPNAME, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG_APPNAME, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        mButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/asdf");
                double asdf = Math.random() * 1000000;
                putDataMapReq.getDataMap().putString(TAG_APPNAME, "asdf" + Double.toString(asdf));
                putDataMapReq.getDataMap().putString(TAG_NOTICONTENT, "I hate you");
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                putDataReq.setUrgent();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(final DataApi.DataItemResult result) {
                        if(result.getStatus().isSuccess()) {
                            Log.d(TAG_APPNAME, "Data item set: " + result.getDataItem().getUri());
                        }
                    }
                });
            }
        });
    }
}
