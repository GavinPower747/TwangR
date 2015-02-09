package net.gavinpower.twangr;

import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.gavinpower.Security.AESEncrypt;
import net.gavinpower.SignalR.Connection;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static net.gavinpower.Security.AESEncrypt.generateKeyFromPassword;
import static net.gavinpower.Security.AESEncrypt.generateSalt;
import static net.gavinpower.Security.AESEncrypt.keyString;
import static net.gavinpower.Security.AESEncrypt.decryptString;
import static net.gavinpower.Security.AESEncrypt.encrypt;
import static net.gavinpower.Security.AESEncrypt.generateKey;
import static net.gavinpower.Security.AESEncrypt.keys;
import static net.gavinpower.Security.AESEncrypt.saltString;


public class LoginActivity extends ActionBarActivity {

    private Application TwangR;
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
            AESEncrypt.CipherTextIvMac passwordCrypt = encrypt(password, key);
            connection.login(username, passwordCrypt.toString());
        }
        catch(UnsupportedEncodingException | GeneralSecurityException ex )
        {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
