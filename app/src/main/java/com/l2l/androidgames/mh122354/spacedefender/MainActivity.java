package com.l2l.androidgames.mh122354.spacedefender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs=getSharedPreferences("HighScores",MODE_PRIVATE);

        TextView textFastestTime= (TextView)findViewById(R.id.highScoreTextView);
        long fastestTime=prefs.getLong("fastestTime",1000000);
        textFastestTime.setText("Fastest Time: "+fastestTime);

         Button playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
        finish();
    }
    // If the player hits the back button, quit the app

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return true;

        }

        return false;

    }
}
