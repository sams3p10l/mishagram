package com.example.specter.mishagram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity
{

	public EditText username, password, email, firstName, lastName;
	public Button register;
	public CalendarView calendar;
	protected boolean usernameCheck, passwordCheck, emailCheck, buttonReady;

	private HttpHelper hh;
	private Handler handler;

	@SuppressLint("SimpleDateFormat")
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeractivity);

		username = findViewById(R.id.username_register);
		password = findViewById(R.id.password_register);
		email = findViewById(R.id.email_register);
		firstName = findViewById(R.id.first_name);
		lastName = findViewById(R.id.last_name);
		register = findViewById(R.id.button_register);
		calendar = findViewById(R.id.calendar);
		Spinner spinner = findViewById(R.id.gender_spinner);

		String selectedDate = "31/07/1996";

		hh = new HttpHelper(this);
		handler = new Handler();

		username.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				usernameCheck = username.getText().toString().length() > 0;
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				buttonReady = usernameCheck && passwordCheck && emailCheck;

				if (buttonReady)
					register.setEnabled(true);
				else
					register.setEnabled(false);
			}
		});

		password.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				passwordCheck = password.getText().toString().length() >= 6;
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				buttonReady = usernameCheck && passwordCheck && emailCheck;

				if (buttonReady)
					register.setEnabled(true);
				else
					register.setEnabled(false);

			}
		});

		email.addTextChangedListener(new TextWatcher()
		{
			boolean monkeyChecker()
			{
				return email.getText().toString().indexOf('@') >= 0;
			}

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				emailCheck = email.getText().toString().length() > 0 && monkeyChecker();
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				buttonReady = usernameCheck && passwordCheck && emailCheck;

				if (buttonReady)
					register.setEnabled(true);
				else
					register.setEnabled(false);

			}
		});

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,		//gender list pull
				R.array.gender_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		calendar.setMaxDate(calendar.getDate());												//max date = current date
		try
		{
			calendar.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);			//default date = birthday
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		register.setOnClickListener(new View.OnClickListener()
		{
			boolean permission = false;

			@Override
			public void onClick(View view)
			{
				final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						JSONObject registerContact = new JSONObject();
						try {
							registerContact.put("username", username.getText().toString());
							registerContact.put("password", password.getText().toString());
							registerContact.put("email", email.getText().toString());

							final String success = hh.postJSONObject(MainActivity.REGISTER_URL, registerContact);

							if (success.equals("successful"))
								permission = true;

							handler.post(new Runnable()
							{
								@Override
								public void run()
								{
									Toast.makeText(RegisterActivity.this, "Register status: " + success, Toast.LENGTH_LONG).show();
									if(permission){
										startActivity(intent);
										finish();
									}
								}
							});
						} catch (JSONException | IOException e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

	}
}
