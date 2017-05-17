package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tobias on 10.02.2017.
 */

public class Result extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Set Typeface to Heveticaneue
        TextView headline = (TextView) findViewById(R.id.resultHeadline);
        headline.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
        TextView holes = (TextView) findViewById(R.id.holes);
        holes.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));


        final TextView result = (TextView) findViewById(R.id.resultNumber);
        result.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
        Log.d("Detectionresult", "" + Long.toString((Detection.result)));
        Log.d("ForceExit", ""+CalculationAcitivity.forceExit);
        Log.d("Finished", ""+Detection.calculationDone);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.setText(Long.toString(Detection.result));
            }
        });


        //Define OnclickListener for Button
        final Button done = (Button) findViewById(R.id.done);
        done.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Result.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Start Settings Activity
                                PelletMillSettings.dieDiameter = null;
                                PelletMillSettings.dieArcLength = null;
                                PelletMillSettings.dieNumber = null;
                                Intent intent = new Intent(Result.this, PelletMillSettings.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
