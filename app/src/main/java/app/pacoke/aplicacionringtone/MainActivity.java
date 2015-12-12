package app.pacoke.aplicacionringtone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Factories.IntentFactory;
import SettingsApp.SharedSettings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedSettings.initaliceParameters();

        Intent intent = IntentFactory.createIntent(this,setting_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
