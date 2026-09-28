package com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView textTime;
    private final String TEXT_TIME_BASE = "Remaining Time: ";
    private int remainingTimeInSeconds;
    private final int frames = 4;
    private int currentFrame;
    private int rng;
    private int lastRNG;
    private Random random = new Random();
    public static int SCORE;
    private List<ImageView> moles = new ArrayList<>();
    private TextView textScore;
    private final String TEXT_SCORE_BASE = "Score: ";
    private CountDownTimer timer;
    private final int MOLE_TIMER = 550; //Tempo de Spawn entre toupeiras
    private final int GAME_TIMER = 30000; //Tempo máximo de partida
    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .build();

    private SoundPool soundPool = new SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(5)
            .build();
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        context = this;
        setContentView(R.layout.activity_game);
        SCORE = 0;
        textScore = findViewById(R.id.textScore);
        moles.add(findViewById(R.id.mole0));
        moles.add(findViewById(R.id.mole1));
        moles.add(findViewById(R.id.mole2));
        moles.add(findViewById(R.id.mole3));
        moles.add(findViewById(R.id.mole4));
        moles.add(findViewById(R.id.mole5));
        mediaPlayer = MediaPlayer.create(context, R.raw.bgm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        id = soundPool.load(context, R.raw.hit, 0);
        textTime = findViewById(R.id.textTime);
        remainingTimeInSeconds = GAME_TIMER / 1000;

        new CountDownTimer(GAME_TIMER, 1000){
            public void onTick(long millisUntilFinished){
                remainingTimeInSeconds = (int) (millisUntilFinished / 1000);
                updateRemainingTime();
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                saveScore();

                // 게임이 끝나면 ResultActivity로 전환하고 사용자 아이디를 함께 전달
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void updateRemainingTime() {
        textTime.setText(TEXT_TIME_BASE + remainingTimeInSeconds);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Update();
                new Handler().postDelayed(this, 30);
            }
        }, 30);
    }

    public void Update(){
        currentFrame = (currentFrame + 1) % frames;
        textScore.setText(TEXT_SCORE_BASE + SCORE);

        //lógica de spawn das toupeiras
        if(!isMoleUp()){
            while (rng == lastRNG) { rng = random.nextInt(moles.size()); }
            lastRNG = rng;
            moles.get(rng).setVisibility(View.VISIBLE);

            timer = new CountDownTimer(MOLE_TIMER, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() { moles.get(rng).setVisibility(View.INVISIBLE); }
            };
            timer.start();
        }
    }

    private void saveScore() {

        try {
            FileOutputStream fos = this.openFileOutput("score.txt", Context.MODE_APPEND);
            String strScore = String.valueOf(SCORE).concat("\n");
            fos.write(strScore.getBytes());
            fos.close();
        } catch (FileNotFoundException fnf) {

            fnf.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    private boolean isMoleUp(){
        for(ImageView mole : moles) {
            if (View.VISIBLE == mole.getVisibility()) { return true; }
        } return false;
    }

    public void moleHit(View view){
        SCORE++;
        View moleHit;
        if(view.getId() == R.id.mole0){ moleHit = findViewById(R.id.mole0_hit); }
        else if(view.getId() == R.id.mole1){ moleHit = findViewById(R.id.mole1_hit); }
        else if(view.getId() == R.id.mole2){ moleHit = findViewById(R.id.mole2_hit); }
        else if(view.getId() == R.id.mole3){ moleHit = findViewById(R.id.mole3_hit); }
        else if(view.getId() == R.id.mole4){ moleHit = findViewById(R.id.mole4_hit); }
        else { moleHit = findViewById(R.id.mole5_hit); }
        moleHit.setVisibility(View.VISIBLE);

        new CountDownTimer(40, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {moleHit.setVisibility(View.INVISIBLE);}
        }.start();

        view.setVisibility(View.INVISIBLE);
        timer.cancel();
        soundPool.play(id, 1, 1 , 0, 0, 1);
    }
}
