package com.example.specter.mishagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity
{

	public EditText username, password, email;
	public Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeractivity);

		username = findViewById(R.id.username_register);
		password = findViewById(R.id.password_register);
		email = findViewById(R.id.email_register);
		register = findViewById(R.id.button_register);


		username.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				if (username.getText().toString().length() == 0)
					register.setEnabled(false);
				else
					register.setEnabled(true);
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

			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				if (password.getText().toString().length() < 6)
					register.setEnabled(false);
				else
					register.setEnabled(true);
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

			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				if (email.getText().toString().length() == 0 || !monkeyChecker())
					register.setEnabled(false);
				else
					register.setEnabled(true);
			}
		});
		





		Spinner spinner = (Spinner) findViewById(R.id.gender_spinner);								//gender list pull
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.gender_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}
}
