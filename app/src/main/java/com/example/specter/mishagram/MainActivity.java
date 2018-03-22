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
    Bundle bundle = new Bundle();
    boolean buttonReady, usernameCheck, passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

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
                buttonReady = usernameCheck && passwordCheck;

                if(buttonReady)
                    login.setEnabled(true);
                else
                    login.setEnabled(false);
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
                buttonReady = usernameCheck && passwordCheck;

                if(buttonReady)
                    login.setEnabled(true);
                else
                    login.setEnabled(false);
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                bundle.putString("user", username.getText().toString());
                bundle.putString("pass", password.getText().toString());

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()         //override da se back button ponasa kao home button nakon logouta
    {                                   //umesto da se vrati na contacts activity
        moveTaskToBack(true);
    }

}
