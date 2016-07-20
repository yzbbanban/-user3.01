package com.wangban.musciplayer.ui;

import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import com.wangban.musciplayer.R;
import com.wangban.musciplayer.R.id;
import com.wangban.musciplayer.R.layout;
import com.wangban.musciplayer.R.menu;
import com.wangban.musciplayer.adpter.MusicPlayerAdapter;
import com.wangban.musciplayer.app.MusicApplication;
import com.wangban.musciplayer.service.MusicService;
import com.wangban.musciplayer.util.CommandUtil;
import com.wangban.musciplayer.util.Consts;
import com.wangban.musciplayer.util.Music;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
        Consts, OnSeekBarChangeListener {
    // TODO
    private List<Music> musics;
    private MusicApplication app;
    private TextView tvCurrentMusic;
    private TextView tvDurationTime;
    private TextView tvCurrentTime;
    private ImageButton ibtnPlayOrPause;
    private ImageButton ibtnNext;
    private ImageButton ibtnPrevious;
    private InnerBroadCastReceiver receiver;
    private Intent intent;
    private SeekBar sbProgress;
    private boolean isTrackingTouch;
    private ImageButton ibtnBackMain;
    private ImageButton ibtnRevolve;
    private ImageButton ibtnPlayState;
    private ImageButton ibtnMusicList;
    private Animation animation;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        app = (MusicApplication) getApplication();
        musics = app.getMusics();

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        setListener();

        setBroadcastReceiver();


    }

    private void initView() {
        ibtnPlayOrPause = (ImageButton) findViewById(R.id.ibtn_music_play_or_pause);
        ibtnNext = (ImageButton) findViewById(R.id.ibtn_music_next);
        ibtnPrevious = (ImageButton) findViewById(R.id.ibtn_music_previous);
        tvCurrentMusic = (TextView) findViewById(R.id.tv_current_music);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        tvDurationTime = (TextView) findViewById(R.id.tv_duration_time);
        sbProgress = (SeekBar) findViewById(R.id.sb_progross);
        ibtnBackMain = (ImageButton) findViewById(id.ibtn_back_main);
        ibtnRevolve = (ImageButton) findViewById(id.ibtn_revolve_words);
        ibtnPlayState = (ImageButton) findViewById(id.ibtn_play_state);
        ibtnMusicList = (ImageButton) findViewById(id.ibtn_music_list);
        state = 1;
        ibtnPlayState.setBackgroundResource(R.drawable.recycle_play);
    }

    // TODO
    private void setBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        receiver = new InnerBroadCastReceiver();
        filter.addAction(ACTION_PLAY_STATE);
        filter.addAction(ACTION_PAUSE_STATE);
        filter.addAction(ACTION_THERAD_UODATA);
        filter.addAction(ACTION_CHANGE_SEEK_TIME);
        registerReceiver(receiver, filter);

    }

    private void setListener() {
        ibtnPlayOrPause.setOnClickListener(this);
        ibtnNext.setOnClickListener(this);
        ibtnPrevious.setOnClickListener(this);
        //back to list item
        ibtnBackMain.setOnClickListener(this);

        sbProgress.setOnSeekBarChangeListener(this);
        ibtnPlayState.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        intent = new Intent();
        intent.setAction(ACTION_RESTART);
        sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onRestart();
        intent = new Intent();
        intent.setAction(ACTION_STOP);
        sendBroadcast(intent);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("supergirl", "activity_destory22222222222222222");
        unregisterReceiver(receiver);
        stopService(new Intent(this, MusicService.class));

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //is back button
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setMessage("您确定要退出吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            builder.setNeutralButton("主界面", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
            });
            //set other layout cannot use
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class InnerBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PLAY_STATE.equals(action)) {
                Log.i("supergirl", "pause execute");
                ibtnPlayOrPause
                        .setImageResource(R.drawable.pause_nomal);
                ibtnRevolve.clearAnimation();
            } else if (ACTION_PAUSE_STATE.equals(action)) {
                Log.i("supergirl", "play execute");
                ibtnPlayOrPause
                        .setImageResource(R.drawable.play_nomal);
                // get play broadcast
                int index = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0);
                int duration = intent.getIntExtra(EXTRA_CURRENT_DURATION, 0);
                String MusicTitle = musics.get(index).getTitle();
                Log.i("supergirl", MusicTitle);
                // get current music title
                tvCurrentMusic.setText(MusicTitle);

                // get total time and set time
                tvDurationTime.setText(CommandUtil.getTime(duration));
                // image revove
                imageRevolve();
                ibtnRevolve.startAnimation(animation);

            } else if (Consts.ACTION_THERAD_UODATA.equals(action)) {
                // TODO
                int percent = intent.getIntExtra(Consts.EXTRA_PERCENT, 0);
                int currentPosition = intent.getIntExtra(
                        Consts.EXTRA_CURRENT_POSITION, 0);
                if (!isTrackingTouch) {
                    sbProgress.setProgress(percent);
                    // Log.i("supergirl", "currentposition"+currentPosition);
                    tvCurrentTime.setText(CommandUtil.getTime(currentPosition));
                }

            } else if (ACTION_CHANGE_SEEK_TIME.equals(action)) {
                int current_time = intent
                        .getIntExtra(EXTRA_CHANGE_SEEK_TIME, 0);
                tvCurrentTime.setText(CommandUtil.getTime(current_time));
            }

        }
    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.ibtn_music_play_or_pause:
                intent.setAction(ACTION_PLAY_OR_PAUSE);
                imageRevolve();

                break;
            case R.id.ibtn_music_next:

                intent.setAction(ACTION_NEXT);

                break;
            case R.id.ibtn_music_previous:

                intent.setAction(ACTION_PREVIOUS);

                break;
            case id.ibtn_back_main:

                startActivity(new Intent(MainActivity.this, MusicActivity.class));
                break;

            case id.ibtn_play_state:

                intent.setAction(ACTION_MEDIA_PLAY_STATE);
                if (state == 1) {
                    ibtnPlayState.setBackgroundResource(R.drawable.repeat_play);
                    state = 2;
                    intent.putExtra(EXTRA_PLAY_STATE_REPEAT,1);
                    Log.i("supergirl","repeat");
                }
                else if (state == 2) {
                    ibtnPlayState.setBackgroundResource(R.drawable.randowm_play);
                    state=3;
                    intent.putExtra(EXTRA_PLAY_STATE_RANDOWM,2);
                    Log.i("supergirl","randowm");
                }else if (state==3){
                    ibtnPlayState.setBackgroundResource(R.drawable.recycle_play);
                    state=1;
                    intent.putExtra(EXTRA_PLAY_STATE_RECYCLE,3);
                    Log.i("supergirl","recycle");
                }


                break;
            case id.ibtn_music_list:
                //TODO spinner
                break;

        }
        sendBroadcast(intent);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // user on tracking touch
        if (fromUser) {
            isTrackingTouch = true;
            intent = new Intent();
            intent.setAction(ACTION_PROGRESS_CHANGE);
            intent.putExtra(EXTRA_CHANGE_PROGRESS, progress);
            sendBroadcast(intent);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTrackingTouch = true;

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        intent = new Intent();
        intent.setAction(ACTION_SEEKTO);
        intent.putExtra(EXTRA_SEEKTO, sbProgress.getProgress());
        sendBroadcast(intent);
        isTrackingTouch = false;

    }

    private void imageRevolve() {
        animation = AnimationUtils.loadAnimation(this, R.anim.set_revolve);
        LinearInterpolator i = new LinearInterpolator();
        animation.setInterpolator(i);
        ibtnRevolve.startAnimation(animation);
    }

}
