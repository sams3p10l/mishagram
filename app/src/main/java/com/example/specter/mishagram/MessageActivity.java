package com.example.specter.mishagram;

import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		int delay = 0, period = 10000;

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
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				messageAdapter.removeMessage(i);
				messageAdapter.notifyDataSetChanged();

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
						msgToSend.put("receiver", receiver);
						msgToSend.put("data", message.getText().toString());

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
			String data = ml.getJSONObject(i).getString("data");

			messageList[iter++] = new Message(0, sender, data);
		}

		return messageList;
	}
}
