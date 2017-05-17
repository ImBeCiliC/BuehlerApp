package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Tobi on 09.02.2017.
 */

public class IntroSplash extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent starts activity_settings
                Intent settingsIntent = new Intent(IntroSplash.this, PelletMillSettings.class);
                IntroSplash.this.startActivity(settingsIntent);
                IntroSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
