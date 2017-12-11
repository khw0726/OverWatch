package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationGroupActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_group_activity);
        TextView priority1TextView = findViewById(R.id.priority1_text);
        TextView priority2TextView = findViewById(R.id.priority2_text);
        TextView priority3TextView = findViewById(R.id.priority3_text);
        int a = 0, b = 0, c = 0;
        try {
            for (JSONObject noti : MainActivity.notis) {
                switch(noti.getInt("priority")){
                    case 1:
                        a++;
                        break;
                    case 2:
                        b++;
                        break;
                    case 3:
                        c++;
                        break;
                }
            }
            priority1TextView.setText(Integer.toString(a));
            priority2TextView.setText(Integer.toString(b));
            priority3TextView.setText(Integer.toString(c));
        } catch (JSONException e){
            e.printStackTrace();
        }
        boolean setTimer = getIntent().getBooleanExtra("setTimer", false);
        if(setTimer){
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished){
                    return;
                }

                public void onFinish(){
                    finish();
                }
            }.start();
        }
        // Enables Always-on
        setAmbientEnabled();
    }
}
