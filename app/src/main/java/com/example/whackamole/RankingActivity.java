package com.example.whackamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingActivity extends AppCompatActivity {

    private DatabaseReference scoresRef;
    private TextView txtScore, txtScore1, txtScore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_ranking);

        // Firebase 초기화
        FirebaseApp.initializeApp(this);

        // Firebase Realtime Database의 Reference를 가져옴
        scoresRef = FirebaseDatabase.getInstance().getReference().child("scores");

        txtScore = findViewById(R.id.txtScore);
        txtScore1 = findViewById(R.id.txtScore1);
        txtScore2 = findViewById(R.id.txtScore2);

        // Firebase에서 랭킹 데이터 가져오기
        loadRankingData();
    }

    private void loadRankingData() {
        scoresRef.orderByValue().limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int rank = 1;

                // 반복문을 사용하여 데이터를 가져오고 순위와 값을 표시
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    long score = (long) snapshot.getValue();

                    setScoreText(rank, key, score);
                    rank++;

                    // 랭킹이 3위까지만 표시되도록 설정
                    if (rank > 3) {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
    }

    private void setScoreText(int rank, String key, long score) {
        switch (rank) {
            case 1:
                txtScore.setText("3rd: " + key + ", Score: " + score);
                break;
            case 2:
                txtScore1.setText("2nd: " + key + ", Score: " + score);
                break;
            case 3:
                txtScore2.setText("1st: " + key + ", Score: " + score);
                break;
            default:
                // 랭킹이 3위까지만 표시되도록 설정
                break;
        }
    }

    public void mainMenu(View view){
        SoundPlayer.playButtonClickSound();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}