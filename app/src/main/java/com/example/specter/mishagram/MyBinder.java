package com.example.specter.mishagram;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

public class MyBinder extends NotificationServiceInterface.Stub
{
	private CallbackInterface mCallback;
	private CallbackCaller caller;

	private class CallbackCaller implements Runnable
	{
		private static final long PERIOD = 5000L;

		private Handler handler = null;
		private boolean isRunning = true;

		public void start()
		{
			handler = new Handler(Looper.getMainLooper());
			handler.postDelayed(this, PERIOD);
		}

		public void stop()
		{
			isRunning = false;
			handler.removeCallbacks(this);
		}

		@Override
		public void run()
		{
			if(!isRunning)
			{
				return;
			}

			try
			{
				mCallback.onCallbackCall();
			} catch (NullPointerException e)
			{

			} catch (RemoteException e)
			{
				Log.e("From Binder", "onCallbackCall zakucao", e);
			}

			handler.postDelayed(this, PERIOD);
		}
	}

	@Override
	public void setCallback(CallbackInterface callback) throws RemoteException
	{
		mCallback = callback;
		caller = new CallbackCaller();
		caller.start();
	}

	public void stop()
	{
		caller.stop();
	}
}
