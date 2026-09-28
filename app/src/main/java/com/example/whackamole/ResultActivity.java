package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private DatabaseReference scoresRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_result);
        SoundPlayer.playWinSound();

        // Firebase Realtime Database의 Reference를 가져옴
        scoresRef = FirebaseDatabase.getInstance().getReference().child("scores");

        TextView textResult = findViewById(R.id.textResult);

        // 전달받은 ID 가져오기
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
        // 화면에 점수 표시
        textResult.setText(String.valueOf(GameActivity.SCORE));

        // 결과 액티비티가 시작될 때 Realtime Database에 값을 저장
        writeToDatabase();
    }

    private void writeToDatabase() {
        if (userId != null) {
            // userId가 null이 아닐 때만 Firebase에 데이터 쓰기
            scoresRef.child(userId).setValue(GameActivity.SCORE);
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void mainMenu(View view) {
        SoundPlayer.playButtonClickSound();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}