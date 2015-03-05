package net.gavinpower.twangr.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.gavinpower.Models.User;
import net.gavinpower.Security.AESEncrypt;
import net.gavinpower.twangr.R;
import net.gavinpower.twangr.TwangR;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.Security.AESEncrypt.generateKeyFromPassword;
import static net.gavinpower.Security.AESEncrypt.generateSalt;
import static net.gavinpower.Security.AESEncrypt.encrypt;
import static net.gavinpower.Security.AESEncrypt.generateKey;
import static net.gavinpower.Security.AESEncrypt.saltString;
import static net.gavinpower.Security.AESEncrypt.PASSWORD;
import static net.gavinpower.Security.AESEncrypt.PASSWORD_BASED_KEY;
import static net.gavinpower.Security.AESEncrypt.key;


public class RegisterActivity extends Activity {

    net.gavinpower.twangr.TwangR TwangR;

    private EditText editUserName;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private EditText editEmail;
    private EditText editRealName;
    private EditText editNickName;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editRealName = (EditText) findViewById(R.id.editRealName);
        editNickName = (EditText) findViewById(R.id.editNickName);

        loading = new ProgressDialog(this);

        TwangR = (TwangR)getApplication();

        try {
            if (PASSWORD_BASED_KEY) {
                String salt = saltString(generateSalt());
                key = AESEncrypt.keys(PASSWORD);
            } else {
                key = generateKey();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void onResume()
    {
        super.onResume();
        TwangR.setActivity(this);
    }

    public void register(View registerButton)
    {
        String userName = editUserName.getText().toString();
        String userPassword = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String userEmail = editEmail.getText().toString();
        String userRealName = editRealName.getText().toString();
        String userNickName = editNickName.getText().toString();
        boolean validated = true;
        String errorText = "";

        if(userName.length() < 4)
        {
            validated = false;
            errorText = "UserName must be longer than 4 characters!";
        }
        else if(userPassword.length() < 6)
        {
            validated = false;
            errorText = "Password must be longer than 6 characters!";
        }
        else if(!userPassword.equals(confirmPassword))
        {
            validated = false;
            errorText = "The password and confirmation are different!";
        }
        else if (!userEmail.contains("@") || !userEmail.contains("."))
        {
            validated = false;
            errorText = "Email address is invalid";
        }
        else if(userRealName.length() < 1)
        {
            validated = false;
            errorText = "Your real name is required";
        }

        if(validated) {
            try {
                /*userPassword = encrypt(userPassword, key).toString();*/
                loading.show();
                HubConnection.register(userName, userPassword,  userRealName, userEmail, userNickName);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            final String error = errorText;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(currentActivity, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void registerSuccess(User user)
    {
        loading.dismiss();
        currentUser = user;
        startActivity(new Intent(this, MainActivity.class));
    }

    public void registerFailure(final String reason)
    {
        loading.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(currentActivity, reason, Toast.LENGTH_LONG).show();
            }
        });

    }


}
