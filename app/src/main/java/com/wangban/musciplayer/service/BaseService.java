package com.wangban.musciplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class BaseService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// 
		return null;
	}
	
	
}
