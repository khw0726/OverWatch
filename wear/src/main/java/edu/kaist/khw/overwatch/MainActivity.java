package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView asdf = null;
    public static final String TAG_APPNAME = "TAG_APPNAME";
    public static final String TAG_NOTICONTENT = "TAG_NOTICONTENT";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asdf = findViewById(R.id.text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.wtf("ASDF", "connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.wtf("ASDF", "dataChanged");
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/asdf") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    launchNotification(dataMap.getString(TAG_APPNAME), dataMap.getString(TAG_NOTICONTENT));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectionSuspended(int a) {
        return;
    }

    @Override
    public void onConnectionFailed(ConnectionResult a){
        return;
    }

    // Our method to update the count
    private void launchNotification(String appName, String notiContent) {
//        Log.wtf("ASDF", c);
//        asdf.setText(c);
        Intent intent = new Intent(this, NotificationViewActivity.class);
        intent.putExtra(TAG_APPNAME, appName);
        intent.putExtra(TAG_NOTICONTENT, notiContent);
        startActivity(intent);
        return;
    }

}