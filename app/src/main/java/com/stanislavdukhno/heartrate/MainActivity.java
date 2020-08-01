package com.stanislavdukhno.heartrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PermissionInfoCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.service.voice.VoiceInteractionSession;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView timerView;
    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = findViewById(R.id.timerView);
        buttonStart = findViewById(R.id.buttonStart);
    }

    public void StartTimer(View view) {

        Context context = MainActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);

        String FirstNameValue = myPreferences.getString("FirstNameValue", "");
        String LastNameValue = myPreferences.getString("LastNameValue", "");
        String TeamNumberValue = myPreferences.getString("TeamNumberValue", "");

        if (FirstNameValue == "" || LastNameValue == "" || TeamNumberValue == ""){
            Toast.makeText(getApplicationContext(), "Заполните профиль!", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonStart.setClickable(false);
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timerView.setText("0");
                OpenSendResultActivity();
                buttonStart.setClickable(true);
            }
        }.start();
    }

    private void OpenSendResultActivity(){
        Intent intent = new Intent(this, SendResultActivity.class);
        startActivity(intent);
    }


    public void OpenSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
