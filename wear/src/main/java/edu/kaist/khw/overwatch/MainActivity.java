package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
        asdf.setText("???");
//        asdf.setVisibility(View.GONE);
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
                    parseNotifications(dataMap.getString(TAG_JSON), true);
                }
                else if(item.getUri().getPath().compareTo("/groupView") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Intent intent = new Intent(this, NotificationGroupActivity.class);
                    String json = dataMap.getString(TAG_JSON);
                    notis = new ArrayList<>();
                    try {
                        JSONArray notifications = new JSONArray(json);
                        for(int i = 0; i< notifications.length(); i++){
                            JSONObject j = notifications.getJSONObject(i);
                            final JSONArray contents = j.getJSONArray("contents");
                            for(int k=0; k< contents.length(); k++) {
                                JSONObject content = contents.getJSONObject(k);
                                JSONObject newJSON = new JSONObject();
                                newJSON.put("package", j.getString("package"));
                                newJSON.put("group", j.getString("group"));
                                newJSON.put("title", content.getString("title"));
                                newJSON.put("text", content.getString("text"));
                                notis.add(newJSON);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("setTimer", true);
                    startActivity(intent);
                } else if(item.getUri().getPath().compareTo("/baselineView") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    parseNotifications(dataMap.getString(TAG_JSON), false);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    private void parseNotifications(String json, boolean isGroupView) {
        asdf.setText("Hello Round World!");
        notis = new ArrayList<>();
        Log.i("Wear", json);
        try {
            JSONArray notifications = new JSONArray(json);
            Random random = new Random();
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(timer, random, notifications, 0, isGroupView), random.nextInt(20000) + 20000);

        } catch (JSONException e) {
            e.printStackTrace();
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
    private void launchNotification(JSONObject noti, boolean isGroupView) {
//        Log.wtf("ASDF", c);
//        asdf.setText(c);
        notis.add(noti);

        Intent intent = new Intent(this, NotificationViewActivity.class);
        intent.putExtra(TAG_JSON, noti.toString());
        intent.putExtra("isGroupView", isGroupView);
        startActivity(intent);
        return;
    }

    private class MyTimerTask extends TimerTask {
        private final Timer timer;
        private final Random random;
        private final JSONArray notifications;
        private final boolean isGroupView;
        private final int index;
        public MyTimerTask(Timer timer, Random random, JSONArray notifications, int index, boolean isGroupView) {
            this.timer = timer;
            this.random = random;
            this.notifications = notifications;
            this.index = index;
            this.isGroupView = isGroupView;
        }
        private class Counter {
            private int i = 0;
            public Counter(){
                i = 0;
            }
            public void increment(){
                i++;
            }
            public int getI() {
                return i;
            }
        }
        @Override
        public void run() {
            if(index < notifications.length()){
                try {
                    final JSONObject notification = notifications.getJSONObject(index);
                    final JSONArray contents = notification.getJSONArray("contents");
    //                final Counter i = new Counter();
                    for(int i=0; i< contents.length(); i++) {
                        JSONObject content = contents.getJSONObject(i);
                        JSONObject newJSON = new JSONObject();
                        newJSON.put("package", notification.getString("package"));
                        newJSON.put("group", notification.getString("group"));
                        newJSON.put("title", content.getString("title"));
                        newJSON.put("text", content.getString("text"));
    //                    notis.add(newJSON);

                        launchNotification(newJSON, isGroupView);
                        Thread.sleep(10000);
                    }
    //                i.increment();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.schedule(new MyTimerTask(timer, random, notifications, index + 1, isGroupView), 20000 + random.nextInt(20000));
            } else {
                asdf.setText("DONE!");
            }

        }
    }
}