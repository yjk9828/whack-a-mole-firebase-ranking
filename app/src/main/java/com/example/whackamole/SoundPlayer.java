package com.example.whackamole;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int buttonClickSound;
    private static int playWinsound;

    public static void initSounds(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        // 효과음 파일 로드
        buttonClickSound = soundPool.load(context, R.raw.button, 1);
    }

    public static void playButtonClickSound() {
        if (soundPool != null) {
            soundPool.play(buttonClickSound, 1, 1, 0, 0, 1);
        }
    }
    public static void playWinSound() {
        if (soundPool != null) {
            soundPool.play(playWinsound, 1, 1, 0, 0, 1);
        }
    }


    public static void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}