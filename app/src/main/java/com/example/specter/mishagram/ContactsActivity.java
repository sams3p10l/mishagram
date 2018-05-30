package com.example.specter.mishagram;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class ContactsActivity extends AppCompatActivity implements ServiceConnection
{
	public Button logout;
	public ListView list;
	private ContactAdapter contactAdapter;
	private HttpHelper hh;
	private Handler handler;
	private volatile JSONArray contactListJSON;
	private MyBinder mService = null;
	protected NotificationCompat.Builder notBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		logout = findViewById(R.id.logout_button);
		list = findViewById(R.id.contact_list);
		contactAdapter = new ContactAdapter(this);
		hh = new HttpHelper(this);
		handler = new Handler();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					contactListJSON = hh.getJSONContacts();
					final Contact[] contactList = formContactListFromJSON(contactListJSON);

					handler.post(new Runnable()
					{
						SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPref", 0);
						@Override
						public void run()
						{
							for (Contact contactIterator : contactList){
								if(!pref.getString("usernameLogin", "").equals(contactIterator.getUsername()))
									contactAdapter.addContact(contactIterator); }
							list.setAdapter(contactAdapter);
						}
					});

				} catch (IOException | JSONException e)
				{
					e.printStackTrace();
				}
			}
		}).start();

		logout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							hh.postLogout();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}).start();
				startActivity(intent);
				finish();
			}
		});

		openService();

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(this);
	}

	private Contact[] formContactListFromJSON(JSONArray cl) throws JSONException
	{
		Contact[] contactList = new Contact[cl.length()];
		int iter = 0;

		for(int i = 0; i < cl.length(); i++)
		{
			String username = cl.getJSONObject(i).getString("username");
			contactList[iter++] = new Contact(0, username);
		}

		return contactList;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		mService = (MyBinder) MyBinder.Stub.asInterface(service);
		try
		{
			mService.setCallback(new CallbackFunction());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name)
	{
		mService = null;
	}

	private void openService()
	{
		Intent intent = new Intent(this, NotificationService.class);
		bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	private Notification buildNotification()
	{
		Intent intent = new Intent(this, ContactsActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		notBuilder = new NotificationCompat.Builder(this)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText("imas majke mi")
				.setSmallIcon(R.drawable.baseline_send_black_24dp)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true);

		return notBuilder.build();
	}

	private class CallbackFunction extends CallbackInterface.Stub
	{
		private String serverResponse;
		private NotificationManagerCompat notManager = NotificationManagerCompat.from(ContactsActivity.this);

		@Override
		public void onCallbackCall() throws RemoteException
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						serverResponse = hh.newMessageCheck();
						Log.d("CALLBACK", serverResponse);

						if(serverResponse.equals("true"))
						{
							notManager.notify(0, buildNotification());
						}
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
