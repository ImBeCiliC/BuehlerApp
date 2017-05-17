package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Tobias on 10.02.2017.
 */

public class CalculationAcitivity extends Activity {

    public static boolean forceExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_calculation);

        //Setting Typeface to Heveticaneue
        TextView calculation = (TextView) findViewById(R.id.computingView);
        calculation.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                ProgressBar calculationProgress = (ProgressBar) findViewById(R.id.calculationProgress);
                int i = 0, count = 10;
                while(!forceExit){
                    calculationProgress.setProgress((int)((i  / (float) count) * 100));
                    i++;
                    calculationFinished();
                    Log.d("Detectio", " " + Detection.calculationDone);
                    try{
                        Thread.sleep(1500);
                    } catch (InterruptedException ignore){
                        Log.d("InterruptedException", "InterruptedException");
                    }
                }
                forceExit = false;
                Intent resultIntent = new Intent(CalculationAcitivity.this, Result.class);
                startActivity(resultIntent);
            }
        }).start();

    }

    public void calculationFinished(){
        if(Detection.calculationDone){
            forceExit = true;
            Log.d("CalculationFinished", " " + forceExit);
        }
    }
}
