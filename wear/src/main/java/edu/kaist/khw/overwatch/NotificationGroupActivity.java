package edu.kaist.khw.overwatch;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationGroupActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_group_activity);
        FrameLayout f1 = findViewById(R.id.priority1_frame);
        FrameLayout f2 = findViewById(R.id.priority2_frame);
        FrameLayout f3 = findViewById(R.id.priority3_frame);
        FrameLayout[] frameLayouts = {f1, f2, f3};
        TextView priority1TextView = findViewById(R.id.priority1_text);
        TextView priority2TextView = findViewById(R.id.priority2_text);
        TextView priority3TextView = findViewById(R.id.priority3_text);
        TextView[] textViews = {priority1TextView, priority2TextView, priority3TextView};

        TextView priority1Name = findViewById(R.id.priority1_name);
        TextView priority2Name = findViewById(R.id.priority2_name);
        TextView priority3Name = findViewById(R.id.priority3_name);
        TextView[] names = {priority1Name, priority2Name, priority3Name};

        ImageView priority1ImageView = findViewById(R.id.priority1_img);
        ImageView priority2ImageView = findViewById(R.id.priority2_img);
        ImageView priority3ImageView = findViewById(R.id.priority3_img);
        ImageView[] imageViews = {priority1ImageView, priority2ImageView, priority3ImageView};

        Map<String, Integer> map = new HashMap<>();
        try {
            for (JSONObject noti : MainActivity.notis) {
                String group = noti.getString("group");
                if(group.length() == 0){
                    continue;
                }
                if(map.containsKey(group)){
                    map.put(group, map.get(group) + 1);
                } else {
                    map.put(group, 1);
                }
            }
            int i = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int len = entry.getValue();
                if (key.equals("msg")) {
                    frameLayouts[i].setVisibility(View.VISIBLE);
                    textViews[i].setText(Integer.toString(len));
                    imageViews[i].setImageResource(R.drawable.kakaotalk);
                    names[i].setVisibility(View.GONE);
                    i++;
                } else if (key.equals("mail")) {
                    frameLayouts[i].setVisibility(View.VISIBLE);
                    textViews[i].setText(Integer.toString(len));
                    imageViews[i].setImageResource(R.drawable.mail);
                    names[i].setVisibility(View.GONE);
                    i++;
                } else if (key.length() != 0) {
                    frameLayouts[i].setVisibility(View.VISIBLE);
                    textViews[i].setText(Integer.toString(len));
                    imageViews[i].setImageResource(R.drawable.face);
                    names[i].setVisibility(View.VISIBLE);
                    names[i].setText(key.substring(0, 3));
                    i++;
                }
            }
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
