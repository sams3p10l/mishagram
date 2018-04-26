package com.example.specter.mishagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ContactsActivity extends AppCompatActivity
{
	public Button logout;
	public ListView list;
	private DatabaseHelper dbH = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		logout = findViewById(R.id.logout_button);
		list = findViewById(R.id.contact_list);
		final SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPref", 0);

		ContactAdapter contactAdapter = new ContactAdapter(this);

		Contact[] contactList = dbH.readContacts();

		for(Contact contactIterator : contactList)
			if(contactIterator.getId() != pref.getInt("contact_id", 0))
				contactAdapter.addContact(contactIterator);

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
