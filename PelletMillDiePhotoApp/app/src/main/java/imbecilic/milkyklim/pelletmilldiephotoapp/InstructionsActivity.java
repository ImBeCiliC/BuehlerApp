package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tobias on 09.03.2017.
 */

public class InstructionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_instruction);

        //Set Font for Text and Button
        Button close = (Button) findViewById(R.id.close);
        close.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
        TextView headline = (TextView) findViewById(R.id.settings_instruction);
        headline.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));

        close.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InstructionsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Start PelletMillSettings Activity
                                Intent intent = new Intent(InstructionsActivity.this, PelletMillSettings.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
