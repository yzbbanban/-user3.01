package com.wangban.musciplayer.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.wangban.musciplayer.ui.MainActivity;
import com.wangban.musciplayer.ui.MusicActivity;
import com.wangban.musciplayer.util.Music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class MusicLocalDao implements IDao<Music> {



    @Override
    public List<Music> getDate() {
        List<Music> musics = new ArrayList<Music>();
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File musicDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            if (musicDir.exists()) {
                File[] files = musicDir.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            String musicTitle = files[i].getName();
                            if (musicTitle.toLowerCase(Locale.CHINA).endsWith(".mp3")) {
                                Music music = new Music();
                                music.setTitle(musicTitle.substring(0,
                                        musicTitle.length() - 4));
                                music.setPath(files[i].getAbsolutePath());
                                musics.add(music);
                            }
                        }
                    }
                }
            }
        }

        return musics;
    }

}
