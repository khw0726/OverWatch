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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends WearableActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView asdf = null;
    public static ArrayList<JSONObject> notis = null;
    public static final String TAG_JSON = "TAG_JSON";
    public static final String TAG_NOTIS = "TAG_NOTIS";
//    public static final String TAG_NOTICONTENT = "TAG_NOTICONTENT";

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
        notis = new ArrayList<>();
        setAmbientEnabled();
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
                if (item.getUri().getPath().compareTo("/individualView") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    launchNotification(dataMap.getString(TAG_JSON));
                }
                else if(item.getUri().getPath().compareTo("/groupView") == 0) {
                    Intent intent = new Intent(this, NotificationGroupActivity.class);
                    intent.putExtra("setTimer", false);
                    startActivity(intent);
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

    private int getPriority(JSONObject n) {
        //TODO: implement compute priority
        return 1;
    }

    // Our method to update the count
    private void launchNotification(String json) {
//        Log.wtf("ASDF", c);
//        asdf.setText(c);

        try {
            JSONObject noti = new JSONObject(json);
            noti.put("priority", getPriority(noti));
            notis.add(noti);

            Intent intent = new Intent(this, NotificationViewActivity.class);
            intent.putExtra(TAG_JSON, noti.toString());
//          intent.putExtra(TAG_NOTICONTENT, notiContent);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return;
    }

}