package net.gavinpower.twangr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.gavinpower.Models.User;
import net.gavinpower.Security.AESEncrypt;
import net.gavinpower.SignalR.Connection;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static net.gavinpower.Security.AESEncrypt.generateKeyFromPassword;
import static net.gavinpower.Security.AESEncrypt.generateSalt;
import static net.gavinpower.Security.AESEncrypt.encrypt;
import static net.gavinpower.Security.AESEncrypt.generateKey;
import static net.gavinpower.Security.AESEncrypt.saltString;


public class LoginActivity extends Activity {

    private TwangR TwangR;
    private Connection connection;

    private EditText Username;
    private EditText Password;

    private static boolean PASSWORD_BASED_KEY = true;
    private static final String PASSWORD = "hZSS9pXiSuGSAe+O7W5jPNvsq9xmlxOfw+qJgb6wXBE=";

    private AESEncrypt.SecretKeys key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.UserNameEdit);
        Password = (EditText) findViewById(R.id.passwordEdit);

        TwangR = (TwangR) getApplication();
        TwangR.setActivity(this);
        connection = TwangR.getConnection();

        try {
            if (PASSWORD_BASED_KEY) {
                String salt = saltString(generateSalt());
                key = generateKeyFromPassword(PASSWORD, salt);
            }
            else {
                key = generateKey();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void Login(View loginButton)
    {
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        try
        {
            if(!username.equals("GavinAdmin")) { // GavinAdmin is a seeded account to test login before the implementation of registration
                password = encrypt(password, key).toString();
            }

            connection.login(username, password);
        }
        catch(UnsupportedEncodingException | GeneralSecurityException ex )
        {
            ex.printStackTrace();
        }
    }

    public void loginSuccess(User user)
    {
        TwangR.setCurrentUser(user);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void loginFailure(String status)
    {
        switch(status)
        {
            case "PasswordIncorrect": status = "Your password is incorrect please try again"; break;
            case "UserNotFound": status = "Incorrect username or password please try again"; break;
        }

        Toast toast = Toast.makeText(this, status, Toast.LENGTH_LONG);
        toast.show();
    }


}
