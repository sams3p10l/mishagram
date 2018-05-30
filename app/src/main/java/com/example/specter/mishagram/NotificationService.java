package com.example.specter.mishagram;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class NotificationService extends Service
{
	private MyBinder myBinder = null;

	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		if(myBinder == null)
			myBinder = new MyBinder();

		return myBinder;
	}

	public boolean onUnbind(Intent intent)
	{
		myBinder.stop();
		return super.onUnbind(intent);
	}
}
