package com.example.specter.mishagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener
{
	public Button logout, send, refresh;
	public EditText message;
	public TextView headline;
	public ListView list;

	private DatabaseHelper dbH = new DatabaseHelper(this);
	private HttpHelper hh;
	private Handler handler;
	private MessageAdapter messageAdapter;
	private String receiver;
	private volatile JSONArray messageListJSON;
	private NativeEncryption crypter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		int delay = 0, period = 30000;

		logout = findViewById(R.id.logout_message);
		send = findViewById(R.id.send);
		message = findViewById(R.id.type_message);
		headline = findViewById(R.id.contact_name);
		list = findViewById(R.id.message_list);
		refresh = findViewById(R.id.refreshBtn);

		messageAdapter = new MessageAdapter(this);
		handler = new Handler();
		Timer timer = new Timer();
		hh = new HttpHelper(this);
		logout.setOnClickListener(this);
		send.setOnClickListener(this);
		refresh.setOnClickListener(this);
		crypter = new NativeEncryption();

		Bundle receivedBundle = getIntent().getExtras();
		if (receivedBundle != null)
		{
			receiver = receivedBundle.getString("name");
		}

		headline.setText(receiver);

		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				refresh();
			}
		}, delay, period);

		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l)
			{ //TODO: pomeri ovo u onLongClickListener klasu
				new Thread(new Runnable() {
					JSONObject toDelete = new JSONObject();
					SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPref", 0);
					@Override
					public void run() {
						try {
							Message getMsg = (Message) messageAdapter.getItem(i);
							if(getMsg.getSender().equals(pref.getString("usernameLogin", "")))
							{
								toDelete.put("receiver", receiver);
								toDelete.put("sender", pref.getString("usernameLogin", ""));
								toDelete.put("data", getMsg.getMsg());
							}
							else
							{
								toDelete.put("receiver", pref.getString("usernameLogin", ""));
								toDelete.put("sender", receiver);
								toDelete.put("data", getMsg.getMsg());
							}

							final String success = hh.deleteMessage(toDelete);

							handler.post(new Runnable() {
								@Override
								public void run() {
									refresh();
									Toast.makeText(MessageActivity.this, "Message deletion: " + success, Toast.LENGTH_SHORT).show();
								}
							});
						} catch (JSONException | IOException e) {
							e.printStackTrace();
						}
					}
				}).start();

				return true;
			}
		});
		
		message.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				if(message.getText().toString().length() > 0)
					send.setEnabled(true);
				else
					send.setEnabled(false);
			}

			@Override
			public void afterTextChanged(Editable editable)
			{

			}
		});
	}

	@Override
	public void onClick(View view)
	{
		int id = view.getId();

		if(id == R.id.send)
		{
			new Thread(new Runnable()
			{
				JSONObject msgToSend = new JSONObject();
				@Override
				public void run()
				{
					try
					{
						String msgData = message.getText().toString();
						String encryptedData = crypter.cryption(msgData);

						msgToSend.put("receiver", receiver);
						msgToSend.put("data", encryptedData);

						hh.postJSONObject(MainActivity.SEND_MSG_URL, msgToSend);

						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								message.setText("");
								refresh();
							}
						});
					} catch (JSONException | IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		else if(id == R.id.logout_message)
		{
			Intent intent = new Intent(MessageActivity.this, MainActivity.class);
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						hh.postLogout();		//TODO: status check
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
			startActivity(intent);
			finish();
		}
		else if(id == R.id.refreshBtn)
		{
			refresh();
		}
	}

	private void refresh()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					messageListJSON = hh.getJSONMessage(receiver);
					final Message[] messageList = formMessageListFromJSON(messageListJSON);

					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							messageAdapter.clearMessageList();
							for (Message messageIterator : messageList)
								messageAdapter.addMessage(messageIterator);
							list.setAdapter(messageAdapter);
						}
					});
				} catch (IOException | JSONException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Message[] formMessageListFromJSON(JSONArray ml) throws JSONException
	{
		Message[] messageList = new Message[ml.length()];
		int iter = 0;

		for(int i = 0; i < ml.length(); i++)
		{
			String sender = ml.getJSONObject(i).getString("sender");
			String encData = ml.getJSONObject(i).getString("data");
			String data = crypter.cryption(encData);

			messageList[iter++] = new Message(0, sender, data);
		}

		return messageList;
	}
}
