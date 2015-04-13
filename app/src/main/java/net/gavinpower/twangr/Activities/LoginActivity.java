package net.gavinpower.twangr.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.gavinpower.Models.User;
import net.gavinpower.twangr.R;
import net.gavinpower.twangr.TwangR;

import microsoft.aspnet.signalr.client.Action;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.Security.AESEncrypt.generateKeyFromPassword;
import static net.gavinpower.Security.AESEncrypt.generateSalt;
import static net.gavinpower.Security.AESEncrypt.generateKey;
import static net.gavinpower.Security.AESEncrypt.saltString;
import static net.gavinpower.Security.AESEncrypt.PASSWORD;
import static net.gavinpower.Security.AESEncrypt.PASSWORD_BASED_KEY;
import static net.gavinpower.Security.AESEncrypt.key;


public class LoginActivity extends Activity {

    private net.gavinpower.twangr.TwangR TwangR;

    private EditText Username;
    private EditText Password;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getPreferences(MODE_PRIVATE);

        Username = (EditText) findViewById(R.id.UserNameEdit);
        Password = (EditText) findViewById(R.id.passwordEdit);

        TwangR = (TwangR) getApplication();
        currentActivity = this;

        if(prefs.getInt("UserID", -1) == -1) {

            try {
                if (PASSWORD_BASED_KEY) {
                    String salt = saltString(generateSalt());
                    key = generateKeyFromPassword(PASSWORD, salt);
                } else {
                    key = generateKey();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else
        {
            HubConnection.getUserById(prefs.getInt("UserID", -1)).done(new Action<User>()
            {
                @Override
                public void run(User user)
                {
                    loginSuccess(user);
                }
            });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        currentActivity = this;
    }


    public void Login(View loginButton)
    {
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        try
        {
            if(!username.equals("GavinAdmin")) { // GavinAdmin is a seeded account to test login before the implementation of registration
                //password = encrypt(password, key).toString();
            }

            HubConnection.login(username, password);
        }
        catch(Exception ex )
        {
            ex.printStackTrace();
        }
    }

    public void register(View registerButton)
    {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void loginSuccess(User user)
    {
        if(prefs.getInt("UserID", -1) == -1)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("UserID", user.getUserId());
            editor.commit();
        }

        currentUser = user;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loginFailure(final String status)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "";
                switch(status)
                {
                    case "PasswordIncorrect": message = "Your password is incorrect please try again"; break;
                    case "UserNotFound": message = "Incorrect username or password please try again"; break;
                }
                Toast toast = Toast.makeText(currentActivity, message, Toast.LENGTH_LONG);
                toast.show();
            }
        });


    }


}
