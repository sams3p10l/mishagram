package com.example.specter.mishagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class ContactsActivity extends AppCompatActivity
{
	public Button logout;
	public ListView list;
	private ContactAdapter contactAdapter;
	private HttpHelper hh;
	private Handler handler;
	private volatile JSONArray contactListJSON;

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
}
