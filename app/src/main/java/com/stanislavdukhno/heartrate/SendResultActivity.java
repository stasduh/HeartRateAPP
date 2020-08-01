package com.stanislavdukhno.heartrate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendResultActivity extends AppCompatActivity {

    public Button buttonSendMessage;
    public TextView editText;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_result);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        editText = findViewById(R.id.editText);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        Context context = SendResultActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString("LOG", "");

        editor.commit();
    }

    public void SendMessage(View view) {

        int indexZero = editText.getText().toString().indexOf("0");

        if (indexZero == 0){
            Toast.makeText(getApplicationContext(), "Недопустимые символы!", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonSendMessage.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = URLGenerator();
        new HeartRateTask().execute(url);
    }

    class HeartRateTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return HeartRatePostRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String response){

            if (!response.equals("Success") && response.length() == 36){
                Toast.makeText(getApplicationContext(), "В базе создан новый спортсмен!", Toast.LENGTH_SHORT).show();
                Context context = SendResultActivity.this;
                SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);
                SharedPreferences.Editor editor = myPreferences.edit();

                editor.putString("SportsmanId", response);

                editor.commit();
            }
            if (response.equals("Success")){
                Toast.makeText(getApplicationContext(), "Отправлено!", Toast.LENGTH_SHORT).show();
            }
            if (!response.equals("Success") && response.length() != 36){
                Toast.makeText(getApplicationContext(), "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
                WriteLOG(response);//******
            }
            Intent intent = new Intent(SendResultActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private String HeartRatePostRequest(String url){

        String myresponse = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            myresponse = response.body().string();
        } catch (Exception e) {

            WriteLOG(e.toString());//******
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            return  "Ошибка";
        }
        return myresponse;
    }

    private String URLGenerator(){

        Context context = SendResultActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);

        String FirstNameValue = myPreferences.getString("FirstNameValue", "");
        String LastNameValue = myPreferences.getString("LastNameValue", "");
        String TeamNumberValue = myPreferences.getString("TeamNumberValue", "");
        String SportsmanIdValue = myPreferences.getString("SportsmanId", "");

        String webAddress = "http://stasduh1984-001-site1.atempurl.com/HeartRates/CreateHeartRateValue";
        String SportsmanId = (SportsmanIdValue == "") ? "" : "SportsmanId=" + SportsmanIdValue;
        String FirstName = "FirstName=" + FirstNameValue;
        String LastName = "LastName=" + LastNameValue;
        String TeamNumber = "TeamNumber=" + TeamNumberValue;
        String value = "value=" + editText.getText().toString();

        String URLString = webAddress + "?" + SportsmanId + "&"
                + FirstName + "&" + LastName + "&" + TeamNumber + "&" + value;

        return URLString;
    }

    private void WriteLOG(String information){

        Context context = SendResultActivity.this;
        SharedPreferences myPreferences = context.getSharedPreferences(getString(R.string.UserSettings), MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        String previous = myPreferences.getString("LOG", "");
        editor.putString("LOG", previous + " | " + information);

        editor.commit();
    }

}
