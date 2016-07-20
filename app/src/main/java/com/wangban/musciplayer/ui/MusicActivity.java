package com.wangban.musciplayer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.wangban.musciplayer.R;
import com.wangban.musciplayer.adpter.MusicPlayerAdapter;
import com.wangban.musciplayer.app.MusicApplication;
import com.wangban.musciplayer.dal.MusicLocalDao;
import com.wangban.musciplayer.util.Consts;
import com.wangban.musciplayer.util.Music;

import java.util.List;
import java.util.zip.Inflater;

public class MusicActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, Consts {
    private List<Music> musics;
    private ListView lvMusic;
    private MusicPlayerAdapter musicAdapter;
    private MusicApplication app;
    private Intent intent;
    private ImageButton ibtnMenuList;
    private ImageButton ibtnLocaMusic;
    private PopupMenu musicListMenu;
    private Music music;
    private int localPosition;
    private BroadcastReceiver receiver;

    private int musicPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        lvMusic = (ListView) findViewById(R.id.lv_music_item);
        ibtnLocaMusic = (ImageButton) findViewById(R.id.ibtn_local_music);
        ibtnMenuList = (ImageButton) findViewById(R.id.ibtn_menu_list);

        app = (MusicApplication) getApplication();
        musics = app.getMusics();
        music = new Music();
        musicAdapter = new MusicPlayerAdapter(this, musics);
        lvMusic.setAdapter(musicAdapter);

        lvMusic.setOnItemClickListener(this);
        ibtnMenuList.setOnClickListener(this);
        ibtnLocaMusic.setOnClickListener(this);


        IntentFilter filter = new IntentFilter();
        receiver = new InnerBroadcastReceiver();
        filter.addAction(ACTION_PAUSE_STATE);
        registerReceiver(receiver, filter);


    }

    private class InnerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PAUSE_STATE.equals(action)) {
                musicPosition = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0);
                musicAdapter.setSelectIndex(musicPosition);
                musicAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // lvMusic.setSelection(position);



        musicAdapter.setSelectIndex(position);
        musicAdapter.notifyDataSetChanged();

        Log.i(TAG, "MusicPlay selectIndex:" + musicPosition);
        intent = new Intent();
        intent.setAction(ACTION_ITEM_PLAY);
        intent.putExtra(EXTRA_ITEM_PLAY, position);
        sendBroadcast(intent);


        //Log.i(TAG, "MusicPlay selectIndex:" + );
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_menu_list:
                showMenuList(ibtnMenuList);
                Toast.makeText(MusicActivity.this, "菜单选项", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibtn_local_music:
                intent = new Intent();
                //perpare to solve
                intent.setAction(ACTION_LOCAL_CURRENT_MUSIC);
                intent.putExtra(EXTRA_LOCAL_CURRENT_MUSIC, musics.get(localPosition).getTitle());
                intent.putExtra(EXTRA_LOCAL_CURRENT_INDEX, localPosition);
                startActivity(new Intent(MusicActivity.this, MainActivity.class));


                break;


        }
    }

    private void showMenuList(View view) {
        musicListMenu = new PopupMenu(this, view);
        musicListMenu.getMenuInflater().inflate(R.menu.main, musicListMenu.getMenu());
        //code can also finish mission
        //SubMenu subMenu = musicListMenu.getMenu().addSubMenu("新建列表").setIcon(R.drawable.new_music_list);


        musicListMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MusicActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        musicListMenu.show();

    }
}
