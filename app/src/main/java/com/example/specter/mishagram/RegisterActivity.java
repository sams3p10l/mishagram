package com.example.specter.mishagram;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity
{

	public EditText username, password, email, firstName, lastName;
	public Button register;
	public CalendarView calendar;
	protected boolean usernameCheck, passwordCheck, emailCheck, buttonReady;

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

		final DatabaseHelper dbHelper = new DatabaseHelper(this);

		//TODO: enable register button on bundle receive - without text change

		Bundle receivedBundle = getIntent().getExtras();								//username password transfer bundle
		if (receivedBundle != null)
		{
			username.setText(receivedBundle.getString("user"));
			password.setText(receivedBundle.getString("pass"));
		}

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
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

				Contact newContact = new Contact(0, username.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
				dbHelper.insertContact(newContact);

				startActivity(intent);
				finish();
			}
		});

	}
}
