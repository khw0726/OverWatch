package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class NotificationGroupActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_group_activity);

        // Enables Always-on
        setAmbientEnabled();
    }
}
