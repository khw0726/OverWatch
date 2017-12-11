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
        final boolean isGroupView = intent.getBooleanExtra("isGroupView",false);
//        String notiContent = intent.getStringExtra(MainActivity.TAG_NOTICONTENT);

//        mAppNameView.setText(appName);
//        mNotiContentView.setText(notiContent);

        try {
            JSONObject noti = new JSONObject(json);
            mAppNameView.setText(noti.getString("title"));
            mNotiContentView.setText(noti.getString("text"));
            String p = noti.getString("package");
            if(p.equals("kakao")){
                mAppIconView.setImageResource(R.drawable.kakaotalk);
            } else if(p.equals("slack")){
                mAppIconView.setImageResource(R.drawable.slack);
            } else if(p.equals("mail")){
                mAppIconView.setImageResource(R.drawable.mail);
            } else  {
                mAppIconView.setImageResource(R.drawable.ic_full_sad);
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

            public void onFinish() {
                if(isGroupView){
                    Intent i = new Intent(getApplicationContext(), NotificationGroupActivity.class);
                    i.putExtra("setTimer", true);
                    startActivity(i);
                }
                finish();


            }
        }.start();

        // Enables Always-on
        setAmbientEnabled();
    }
}
