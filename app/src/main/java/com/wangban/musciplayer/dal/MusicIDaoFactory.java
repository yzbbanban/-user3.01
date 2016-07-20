package com.wangban.musciplayer.dal;

import android.content.Context;

import com.wangban.musciplayer.util.Music;

public class MusicIDaoFactory {
	private MusicIDaoFactory(){
		super();
	}
	
	public static IDao<Music> newMusicDao(Context context){
		return new MediaStoreMusicDao(context);
	}
	
}
