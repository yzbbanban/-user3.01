package com.wangban.musciplayer.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.wangban.musciplayer.adpter.MusicPlayerAdapter;
import com.wangban.musciplayer.app.MusicApplication;
import com.wangban.musciplayer.dal.MusicIDaoFactory;
import com.wangban.musciplayer.ui.MusicActivity;
import com.wangban.musciplayer.util.CommandUtil;
import com.wangban.musciplayer.util.Consts;
import com.wangban.musciplayer.util.Music;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

public class MusicService extends BaseService implements Consts {
    private MediaPlayer player;
    private MusicApplication app;
    private List<Music> musics;
    private int currentMusicIndex;
    private int pausePosition;
    private InnerBroadCastReceiver receiver;
    private Intent intent;
    private boolean looper;
    private MusicThread musicThread;
    private int playState = 3;
    private boolean autoPlay = false;
    private Music music;
    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        app = (MusicApplication) getApplication();
        musics = app.getMusics();
        //startThread();
        music=new Music();
        setBroadcastReceiver();
        InnerOnCompletionListener listener = new InnerOnCompletionListener();
        player.setOnCompletionListener(listener);

    }

    private class InnerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            stopThread();
            Log.i(TAG, "autoplay: 怎么回事啊!!!!");
            autoPlay = true;
            next();

        }
    }

    private void setBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        receiver = new InnerBroadCastReceiver();
        filter.addAction(ACTION_PLAY_OR_PAUSE);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PREVIOUS);
        filter.addAction(ACTION_ITEM_PLAY);
        filter.addAction(ACTION_SEEKTO);
        filter.addAction(ACTION_PROGRESS_CHANGE);
        filter.addAction(ACTION_RESTART);
        filter.addAction(ACTION_STOP);
        filter.addAction(ACTION_MEDIA_PLAY_STATE);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        //Log.i("supergirl", "destorys11111111");
        unregisterReceiver(receiver);
        looper = false;
        stopThread();
        player.release();
        player = null;
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private class MusicThread extends Thread {
        @Override
        public void run() {
            intent = new Intent();
            intent.setAction(Consts.ACTION_THERAD_UODATA);


            while (looper) {
                if (player != null && player.isPlaying()) {

                    //Log.i("supergirl", "thread1111111");

                    int percent = player.getCurrentPosition() * 100 / player.getDuration();
                    intent.putExtra(EXTRA_PERCENT, percent);
                    intent.putExtra(EXTRA_CURRENT_POSITION,
                            player.getCurrentPosition());
                    sendBroadcast(intent);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private class InnerBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PLAY_OR_PAUSE.equals(action)) {
                if (player.isPlaying()) {
                    //Log.i("supergirl", "pause_state");
                    pause();
                } else {
                    play();
                    //Log.i("supergirl", "play_state");
                }

            } else if (ACTION_NEXT.equals(action)) {
                next();
                //Log.i("supergirl", "next_state");

            } else if (ACTION_PREVIOUS.equals(action)) {
                previous();
                //Log.i("supergirl", "pervious_state");

            } else if (ACTION_ITEM_PLAY.equals(action)) {
                int position = intent.getIntExtra(EXTRA_ITEM_PLAY, 0);
                play(position);

            } else if (ACTION_SEEKTO.equals(action)) {
                int position = intent.getIntExtra(EXTRA_SEEKTO, 0);
                //Log.i("superman", "" + position);
                int currentTime = position * player.getDuration() / 100;
                //Log.i("superman", "" + CommandUtil.getTime(currentTime));
                player.seekTo(currentTime);

            } else if (ACTION_PROGRESS_CHANGE.equals(action)) {
                int progress = intent.getIntExtra(EXTRA_CHANGE_PROGRESS, 0);
                //Log.i("superman", "" + progress);
                intent.setAction(ACTION_CHANGE_SEEK_TIME);
                int progressTime = progress * player.getDuration() / 100;
                pausePosition = progressTime;
                intent.putExtra(EXTRA_CHANGE_SEEK_TIME, progressTime);
                sendBroadcast(intent);
            } else if (ACTION_MEDIA_PLAY_STATE.equals(action)) {

                if (1 == intent.getIntExtra(EXTRA_PLAY_STATE_REPEAT, 0)) {
                    playState = 1;
                    //Log.i("supergirl", "playstate"+playState);
                } else if (2 == intent.getIntExtra(EXTRA_PLAY_STATE_RANDOWM, 0)) {
                    playState = 2;
                    //Log.i("supergirl", "playstate"+playState);
                } else if (3 == intent.getIntExtra(EXTRA_PLAY_STATE_RECYCLE, 0)) {
                    playState = 3;
                    //Log.i("supergirl", "playstate"+playState);
                }

            } else if (ACTION_RESTART.equals(action))

            {
                //Log.i("superman", "restart");
                startThread();
            } else if (ACTION_STOP.equals(action))

            {
                //Log.i("superman", "stop activity");
                stopThread();
            }

        }

    }

    public void play() {
        try {
            //stopThread();
            player.reset();
            player.setDataSource(musics.get(currentMusicIndex).getPath());
            // player.prepareAsync();
            player.prepare();
            player.seekTo(pausePosition);
            player.start();
            //Log.i(TAG, "play: " + currentMusicIndex);
            startThread();
            music.setIsplay(true);
            Log.i(TAG, "playis: " + music.isplay());
            musics.get(currentMusicIndex).setIsplay(true);

            Log.i(TAG, "musicService: isplay2222222" );
            // send change pause image
            intent = new Intent();
            intent.setAction(ACTION_PAUSE_STATE);
            intent.putExtra(EXTRA_CURRENT_INDEX, currentMusicIndex);
            intent.putExtra(EXTRA_CURRENT_DURATION, player.getDuration());
            intent.putExtra(EXTRA_CURRENT_POSITION, player.getCurrentPosition());
            sendBroadcast(intent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(int position) {
        currentMusicIndex = position;
        pausePosition = 0;
        stopThread();
        play();
    }

    public void pause() {
        player.pause();
        looper = false;
        pausePosition = player.getCurrentPosition();
        // send pause state change play image
        intent = new Intent();
        intent.setAction(ACTION_PLAY_STATE);
        sendBroadcast(intent);
        stopThread();

    }

    public void next() {
        Log.i(TAG, "autoPlay:" + autoPlay);
        Log.i(TAG, "platState:" + playState);
        Log.i(TAG, "currentIndex11:" + currentMusicIndex);
        if (playState == 3) {
            //recycle
            currentMusicIndex++;
            if (currentMusicIndex > musics.size()) {
                currentMusicIndex = 0;
            }

        } else if (playState == 2) {
            //randowm
            currentMusicIndex = new Random().nextInt(musics.size());
        } else if (playState == 1) {
            //repeat
            Log.i(TAG, "next: 怎么回事啊!!!!");
            if (autoPlay) {
                currentMusicIndex++;
                currentMusicIndex--;
            }else{
                currentMusicIndex++;
            }
        }
        Log.i(TAG, "currentIndex22:" + currentMusicIndex);
        pausePosition = 0;
        stopThread();
        play();

    }

    public void previous() {
        currentMusicIndex--;
        if (currentMusicIndex < 0) {
            currentMusicIndex = musics.size() - 1;
        }
        pausePosition = 0;
        stopThread();
        play();
    }

    private void startThread() {
        if (musicThread == null) {
            musicThread = new MusicThread();
            looper = true;
            musicThread.start();
        }
    }

    private void stopThread() {
        if (musicThread != null) {
            looper = false;
            //Log.i("supergirl", "111111111111111111");
            musicThread = null;
        }
    }

}
