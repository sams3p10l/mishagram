package com.example.specter.mishagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ContactsActivity extends AppCompatActivity
{
	public Button logout;
	public ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		logout = findViewById(R.id.logout_button);
		list = findViewById(R.id.contact_list);

		ContactAdapter contactAdapter = new ContactAdapter(this);

		contactAdapter.addContact(new Contact("Misa Misic"));
		contactAdapter.addContact(new Contact("Laza Lazic"));
		contactAdapter.addContact(new Contact("Pera Peric"));

		list.setAdapter(contactAdapter);

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


	}
}
