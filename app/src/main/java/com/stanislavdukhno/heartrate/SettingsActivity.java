package com.stanislavdukhno.heartrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public TextView FirstNameValue;
    public TextView LastNameValue;
    public TextView TeamNumberValue;
    public TextView GUIDValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FirstNameValue = findViewById(R.id.FirstNameValue);
        LastNameValue = findViewById(R.id.LastNameValue);
        TeamNumberValue = findViewById(R.id.TeamNumberValue);
        GUIDValue = findViewById(R.id.GUIDValue);

        Context context = SettingsActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);

        FirstNameValue.setText(myPreferences.getString("FirstNameValue", ""));
        LastNameValue.setText(myPreferences.getString("LastNameValue", ""));
        TeamNumberValue.setText(myPreferences.getString("TeamNumberValue", ""));
        GUIDValue.setText(myPreferences.getString("SportsmanId", ""));

    }

    public void SaveSettings(View view) {

        Context context = SettingsActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();

        editor.putString("FirstNameValue", FirstNameValue.getText().toString());
        editor.putString("LastNameValue", LastNameValue.getText().toString());
        editor.putString("TeamNumberValue", TeamNumberValue.getText().toString());

        editor.commit();
    }
}
