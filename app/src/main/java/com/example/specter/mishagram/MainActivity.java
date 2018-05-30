package com.example.specter.mishagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public EditText username, password;
    public Button login, register;
    public Bundle bundle = new Bundle();

    private DatabaseHelper dbH = new DatabaseHelper(this);
    private HttpHelper hh;
    private Handler handler;

    public static String BASE_URL = "http://18.205.194.168:80";
    public static String REGISTER_URL = BASE_URL + "/register";
    public static String LOGIN_URL = BASE_URL + "/login";
    public static String GET_CONTACT_URL = BASE_URL + "/contacts";
    public static String SEND_MSG_URL = BASE_URL + "/message";
    public static String LOGOUT_URL = BASE_URL + "/logout";
    public static String NOTIFICATION_URL = BASE_URL + "/getfromservice";

    private boolean permission = false;
    private boolean buttonReady, usernameCheck, passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        hh = new HttpHelper(this);

        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

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

    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        final SharedPreferences.Editor pref = getApplicationContext().getSharedPreferences("sharedPref", 0).edit();

        if (id == R.id.login)
        {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            pref.putString("usernameLogin", username.getText().toString());
            pref.apply();
            checkIfUserPresent(intent);
        }
        else if (id == R.id.register)
        {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void checkIfUserPresent(final Intent intent)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                JSONObject loginContact = new JSONObject();
                try
                {
                    loginContact.put("username", username.getText().toString());
                    loginContact.put("password", password.getText().toString());

                    final String success = hh.postJSONObject(LOGIN_URL, loginContact);

                    if(success.equals("successful"))
                        permission = true;

                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(permission){
                            startActivity(intent);
                            finish();
                        }
                            Toast.makeText(MainActivity.this, "Login status: " + success, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException | IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed()         //override da se back button ponasa kao home button nakon logouta
    {                                   //umesto da se vrati na contacts activity
        moveTaskToBack(true);
    }
}
