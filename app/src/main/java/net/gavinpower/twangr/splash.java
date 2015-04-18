package net.gavinpower.twangr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class splash extends Activity {


        private final int SPLASH_DISPLAY_LENGTH = 600;


        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_splash);

                  new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    Intent mainIntent = new Intent(splash.this,Menu.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }