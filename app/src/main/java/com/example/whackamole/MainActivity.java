package com.example.whackamole;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnRanking;
    EditText editTextUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        btnPlay = findViewById(R.id.btnPlay);
        btnRanking = findViewById(R.id.btnRanking);
        editTextUserId = findViewById(R.id.editTextId);
        // SoundPlayer 초기화
        SoundPlayer.initSounds(this);
    }

    public void startGame(View view) {
        // 입력한 아이디 가져오기
        String userId = editTextUserId.getText().toString().trim();

        // 게임 액티비티로 아이디 전달
        if (!TextUtils.isEmpty(userId)){
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("userId", userId);
            SoundPlayer.playButtonClickSound();
            startActivity(intent);
            finish();
        }
        //ID 입력이 없음.
        else{
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentDate);
            userId = (formattedDate != null) ? "???:" + formattedDate : "???";
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("userId", userId);
            SoundPlayer.playButtonClickSound();
            startActivity(intent);
            finish();
        }
    }

    public void rankingActivity(View view) {
        SoundPlayer.playButtonClickSound();
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
        finish();
    }
}