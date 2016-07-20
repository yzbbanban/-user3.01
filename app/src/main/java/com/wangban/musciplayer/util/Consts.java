package com.wangban.musciplayer.util;

public interface Consts {
	//main_activity send broadcast
	String ACTION_PLAY_OR_PAUSE="wangban.intent.action.PLAYPAUSE";
	String ACTION_NEXT="wangban.intent.action.NEXT";
	String ACTION_PREVIOUS="wangban.intent.action.PREVIOUS";
	String ACTION_ITEM_PLAY="wangban.intent.action.ITEMPLAY";
	String ACTION_SEEKTO="wangban.intent.action.SEEKTO";
	String ACTION_PROGRESS_CHANGE="wangban.intent.action.PROGRESSCHANGE";
	String ACTION_RESTART="wangban.intent.action.ACTION_RASTART";
	String ACTION_STOP="wangban.intent.action.STOP";
	String ACTION_MEDIA_PLAY_STATE="wangban.intent.action.PLAYSTARE";

	//music_activity send broadcast
	String ACTION_LOCAL_CURRENT_MUSIC="wangban.intent.action.CURRENTMUSIC";



	//service send broadcast
	String ACTION_PLAY_STATE="wangban.intent.action.PLAYSTATE";
	String ACTION_PAUSE_STATE="wangban.intent.action.PAUSEATATE";
	String ACTION_THERAD_UODATA="wangban.intent.action.UPDATA";
	String ACTION_CHANGE_SEEK_TIME="wangban.intent.action.SEEK_TIME";

	//extra value
	String EXTRA_ITEM_PLAY="item_play";
	String EXTRA_CURRENT_INDEX="current_index";
	String EXTRA_CURRENT_DURATION="current_duration";
	String EXTRA_PERCENT="sbprogress_percent";
	String EXTRA_CURRENT_POSITION="current_position";
	String EXTRA_SEEKTO="seekto";
	String EXTRA_CHANGE_PROGRESS="change_progress";
	String EXTRA_CHANGE_SEEK_TIME="change_seek_to";
	String EXTRA_LOCAL_CURRENT_MUSIC="local_current_music";
	String EXTRA_LOCAL_CURRENT_INDEX="local_current_index";
	String EXTRA_PLAY_STATE_RECYCLE="play_state_recycle";
	String EXTRA_PLAY_STATE_RANDOWM="play_state_randowm";
	String EXTRA_PLAY_STATE_REPEAT="play_state_repeat";
	String EXTRA_ISPLAY="isplay";


	//debug words
	String TAG="supergirl";


}
