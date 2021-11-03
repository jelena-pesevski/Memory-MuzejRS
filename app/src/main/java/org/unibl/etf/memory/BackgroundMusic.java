package org.unibl.etf.memory;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusic extends Service {

    private MediaPlayer mediaPlayer;

    public BackgroundMusic() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mediaPlayer=MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}