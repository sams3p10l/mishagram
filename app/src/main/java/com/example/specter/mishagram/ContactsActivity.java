package com.example.specter.mishagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity
{
	public Button logout;
	public TextView contactName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		logout = findViewById(R.id.logout_button);
		contactName = findViewById(R.id.contact_name);

		logout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		contactName.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(ContactsActivity.this, MessageActivity.class);
				startActivity(intent);
			}
		});
	}
}
