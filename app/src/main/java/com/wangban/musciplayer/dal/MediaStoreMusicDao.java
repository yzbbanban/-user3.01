package com.wangban.musciplayer.dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.wangban.musciplayer.util.Music;

public class MediaStoreMusicDao implements IDao<Music> {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public MediaStoreMusicDao(Context context) {
        super();
        setContext(context);
    }

    @Override
    public List<Music> getDate() {
        List<Music> musics = new ArrayList<Music>();

        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {"_id", // 0
                "_data", // 1
                "_size", // 2
                "title", // 3
                "duration", // 4
                "album_artist", // 5
                "album", // 6
                "artist" // 7
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor c = cr.query(uri, projection, selection, selectionArgs,
                sortOrder);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Music music = new Music();
            music.setId(c.getLong(0));
            music.setPath(c.getString(1));
            music.setSize(c.getInt(2));
            music.setTitle(c.getString(3));
            music.setDuration(c.getInt(4));
            music.setAlbumArtist(c.getString(5));
            music.setAlbum(c.getString(6));
            music.setArtist(c.getString(7));
            musics.add(music);
        }

        c.close();

        return musics;
    }
}

