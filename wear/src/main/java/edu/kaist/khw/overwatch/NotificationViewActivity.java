package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationViewActivity extends WearableActivity {

    private TextView mAppNameView, mNotiContentView;
    private ImageView mAppIconView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        mAppNameView = (TextView) findViewById(R.id.appName);
        mNotiContentView = findViewById(R.id.notiContent);
        mAppIconView = findViewById(R.id.appIcon);
        Intent intent = getIntent();

        String json = intent.getStringExtra(MainActivity.TAG_JSON);
//        String notiContent = intent.getStringExtra(MainActivity.TAG_NOTICONTENT);

//        mAppNameView.setText(appName);
//        mNotiContentView.setText(notiContent);

        try {
            JSONObject noti = new JSONObject(json);
            mAppNameView.setText(noti.getString("android.title"));
            mNotiContentView.setText(noti.getString("android.bigText"));
            if(noti.getString("package").equals("com.kakao.talk")){
                mAppIconView.setImageResource(R.drawable.kakaotalk);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        v.vibrate(300);
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished){
                return;
            }

            public void onFinish(){
                Intent i = new Intent(getApplicationContext(), NotificationGroupActivity.class);
                startActivity(i);
                finish();
            }
        }.start();

        // Enables Always-on
        setAmbientEnabled();
    }
}
