package com.wangban.musciplayer.app;

import java.util.List;

import com.wangban.musciplayer.dal.IDao;
import com.wangban.musciplayer.dal.MusicIDaoFactory;
import com.wangban.musciplayer.util.Music;


import android.app.Application;
import android.content.Context;

public class MusicApplication extends Application {
	private List<Music> musics;
	@Override
	public void onCreate() {
		
//		IDao<Music> iDao = MusicIDaoFactory.newMusicDao();
//		musics = iDao.getDate();
		musics = MusicIDaoFactory.newMusicDao(this).getDate();
	}

	public List<Music> getMusics() {
		return musics;
	}

}
