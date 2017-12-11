package edu.kaist.khw.overwatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_JSON = "TAG_JSON";
    private static final String TAG_NOTICONTENT = "TAG_NOTICONTENT";
    private static GoogleApiClient mGoogleApiClient = null;
    private String payload = "";
//    private static final String payload = "{\"posttime\":1512987020357,\"package\":\"com.kakao.talk\",\"android.title\":\"이태경\",\"android.text\":\"일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데\",\"android.bigText\":\"일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데 일단 내가 보기엔 카톡만 저런데\"}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button mButton = (Button) findViewById(R.id.button2);
        final Button mButton2 = findViewById(R.id.button);
        final Button mButton3 = findViewById(R.id.button3);
        final Button loadButton = findViewById(R.id.loadButton);
        final EditText fileNameEditText = findViewById(R.id.fileName);
        final TextView payloadTextView = findViewById(R.id.payload);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG_JSON, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG_JSON, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG_JSON, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();


        loadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String fileName = fileNameEditText.getText().toString();
                payload = loadJSONFromAsset(getApplicationContext(), fileName);
                payloadTextView.setText(payload);
            }
        });
        Log.wtf("WTF", payload);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/groupView");
                double asdf = Math.random() * 1000000;
                putDataMapReq.getDataMap().putString(TAG_JSON, payload);
                putDataMapReq.getDataMap().putString(TAG_NOTICONTENT, "I hate you" + Double.toString(asdf));
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                putDataReq.setUrgent();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(final DataApi.DataItemResult result) {
                        if(result.getStatus().isSuccess()) {
                            Log.d(TAG_JSON, "Data item set: " + result.getDataItem().getUri());
                        }
                    }
                });
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/baselineView");
                double asdf = Math.random() * 1000000;
                putDataMapReq.getDataMap().putString(TAG_JSON, payload);
                putDataMapReq.getDataMap().putString(TAG_NOTICONTENT, "I hate you" + Double.toString(asdf));
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                putDataReq.setUrgent();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(final DataApi.DataItemResult result) {
                        if(result.getStatus().isSuccess()) {
                            Log.d(TAG_JSON, "Data item set: " + result.getDataItem().getUri());
                        }
                    }
                });
            }
        });
        mButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/individualView");
                double asdf = Math.random() * 1000000;
                putDataMapReq.getDataMap().putString(TAG_JSON, payload);
                putDataMapReq.getDataMap().putString(TAG_NOTICONTENT, "I hate you" + Double.toString(asdf));
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                putDataReq.setUrgent();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(final DataApi.DataItemResult result) {
                        if(result.getStatus().isSuccess()) {
                            Log.d(TAG_JSON, "Data item set: " + result.getDataItem().getUri());
                        }
                    }
                });
            }
        });
    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
