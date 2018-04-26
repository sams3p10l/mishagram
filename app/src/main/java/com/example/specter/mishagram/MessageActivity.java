package com.example.specter.mishagram;

import android.content.Intent;
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

public class MessageActivity extends AppCompatActivity
{
	public Button logout, send;
	public EditText message;
	public TextView headline;
	public ListView list;
	public int senderID = 0, receiverID = 0;
	private DatabaseHelper dbH = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		logout = findViewById(R.id.logout_message);
		send = findViewById(R.id.send);
		message = findViewById(R.id.type_message);
		headline = findViewById(R.id.contact_name);
		list = findViewById(R.id.message_list);

		Bundle receivedBundle = getIntent().getExtras();
		if (receivedBundle != null)
		{
			headline.setText(receivedBundle.getString("name"));
			senderID = receivedBundle.getInt("senderID");
			receiverID = receivedBundle.getInt("receiverID");
		}

		final MessageAdapter messageAdapter = new MessageAdapter(this);
		final Message[] messageList = dbH.readMessages(senderID, receiverID);

		if(messageList != null)
			for(Message messageIterator : messageList)
				messageAdapter.addMessage(messageIterator);

		list.setAdapter(messageAdapter);

		logout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(MessageActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		send.setOnClickListener(new View.OnClickListener()
		{
			//TODO: Sender, receiver
			@Override
			public void onClick(View view)
			{
				Message tempMsg = new Message(0, String.valueOf(senderID), String.valueOf(receiverID), message.getText().toString());
				dbH.insertMessage(tempMsg);
				messageAdapter.addMessage(tempMsg);
				message.setText("");
			}
		});

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
}
