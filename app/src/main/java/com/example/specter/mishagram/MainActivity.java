package com.example.specter.mishagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public EditText username, password;
    public Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);

        username.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int i = 0;

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

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        //TODO: optimize validity check

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
                if(username.getText().toString().length() == 0)
                    login.setEnabled(false);
                else
                    login.setEnabled(true);
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
                if(password.getText().toString().length() < 6)
                    login.setEnabled(false);
                else
                    login.setEnabled(true);
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("Tag", "Intent started");
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
