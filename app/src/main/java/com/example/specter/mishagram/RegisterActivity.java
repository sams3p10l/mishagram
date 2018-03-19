package com.example.specter.mishagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity
{

	public EditText username, password, email, firstName, lastName;
	public Button register;
	protected boolean usernameCheck, passwordCheck, emailCheck;

	@Override
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

		username.setOnClickListener(new View.OnClickListener()				//ZASTO NE RADI
		{
			int i = 0;

			@Override
			public void onClick(View view)
			{
				if (++i == 1)
					username.setText("");
			}
		});

		password.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			int i = 0;

			@Override
			public void onFocusChange(View view, boolean b)
			{
				if (++i == 1)
				{
					password.setText("");
					password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}

			}
		});

		firstName.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			int i = 0;

			@Override
			public void onFocusChange(View view, boolean b)
			{
				if (++i == 1)
				{
					firstName.setText("");
				}
			}
		});

		lastName.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			int i = 0;

			@Override
			public void onFocusChange(View view, boolean b)
			{
				if (++i == 1)
				{
					lastName.setText("");
				}
			}
		});

		email.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			int i = 0;

			@Override
			public void onFocusChange(View view, boolean b)
			{
				if (++i == 1)
				{
					email.setText("");
				}
			}
		});

		//TODO: sinhronizuj listenere
		//da li moraju overrideovane funkcije da bleje prazne

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
				usernameCheck = username.getText().toString().length() > 0;
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
				passwordCheck = password.getText().toString().length() >= 6;
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
				emailCheck = email.getText().toString().length() > 0 && monkeyChecker();
			}
		});


		Spinner spinner = findViewById(R.id.gender_spinner);								//gender list pull
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.gender_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		boolean buttonReady = usernameCheck && passwordCheck && emailCheck;
			register.setEnabled(buttonReady);

	}
}
