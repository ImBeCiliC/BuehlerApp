package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v13.*;

import org.w3c.dom.Text;

/**
 * Created by Tobi on 09.02.2017.
 */

public class PelletMillSettings extends AppCompatActivity {
    public static Integer dieNumber = null;
    public static Integer dieArcLength = null;
    public static Integer dieDiameter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_settings);

        new Thread(new Runnable() {
            @Override
            public void run() {
                PelletMillSettings.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Set Font for Text and Button
                        Button button = (Button) findViewById(R.id.next);
                        button.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
                        TextView headline = (TextView) findViewById(R.id.settingsHeadline);
                        headline.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));

                        //Get all EditText-Fields
                        EditText dieNumberEditText = (EditText) findViewById(R.id.die_number);
                        dieNumberEditText.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
                        EditText dieArcLengthEditText = (EditText) findViewById(R.id.dieArcLength);
                        dieArcLengthEditText.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));
                        EditText dieDiameterEditText = (EditText) findViewById(R.id.die_diameter);
                        dieDiameterEditText.setTypeface(Heveticaneue.getTypeface(getApplicationContext()));

                        if(dieNumber != null)
                            dieNumberEditText.setText(dieNumber.toString());
                        if(dieArcLength != null)
                            dieArcLengthEditText.setText(dieArcLength.toString());
                        if(dieDiameter != null)
                            dieDiameterEditText.setText(dieDiameter.toString());
                        validateForm();


                        //Create TextWatcher to check when a Edittext is changed
                        TextWatcher watcher = new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                validateForm();
                            }
                        };
                        //give all EditTextFields the TextWatcher as Changelistener
                        dieNumberEditText.addTextChangedListener(watcher);
                        dieArcLengthEditText.addTextChangedListener(watcher);
                        dieDiameterEditText.addTextChangedListener(watcher);
                    }
                });
            }
        }).start();

        //Define OnclickListener for Button
        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PelletMillSettings.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Start camera Activity and Collect Data if Data is Empty don't proceed
                                startCamera();
                            }
                        });
                    }
                }).start();
            }
        });

        final Button questionMark = (Button) findViewById(R.id.questionMark);
        questionMark.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Start InstructionActivity
                        Intent intent = new Intent(PelletMillSettings.this, InstructionsActivity.class);
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if(dieNumber != null) {
            savedInstanceState.putInt("dieNumber", dieNumber);
        } else {
            dieNumber = null;
        }
        if(dieArcLength != null){
            savedInstanceState.putInt("dieArcNumber", dieArcLength);
        } else {
            dieArcLength = null;
        }
        if(dieDiameter != null) {
            savedInstanceState.putInt("dieDiameter", dieDiameter);
        } else {
            dieDiameter = null;
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        dieNumber = savedInstanceState.getInt("dieNumber");
        dieArcLength = savedInstanceState.getInt("dieArcNumber");
        dieDiameter = savedInstanceState.getInt("dieDiameter");
    }

    private void validateForm() {
        //Get Button
        Button next = (Button) findViewById(R.id.next);
        //Get all EditText-Fields
        EditText dieNumberEditText = (EditText) findViewById(R.id.die_number);
        EditText dieArcLengthEditText = (EditText) findViewById(R.id.dieArcLength);
        EditText dieDiameterEditText = (EditText) findViewById(R.id.die_diameter);
        //Check if EditText is Empty if not enable the button and save variables
        if(dieNumberEditText.getText().length()>0){
            dieNumber = Integer.parseInt(dieNumberEditText.getText().toString());
            next.setEnabled(true);
        } else {
            next.setEnabled(false);
        }

        if(dieArcLengthEditText.getText().length()>0) {
            dieArcLength = Integer.parseInt(dieArcLengthEditText.getText().toString());
            next.setEnabled(true);
        } else {
            next.setEnabled(false);
        }

        if(dieDiameterEditText.getText().length()>0){
            dieDiameter = Integer.parseInt(dieDiameterEditText.getText().toString());
            next.setEnabled(true);
        } else {
            next.setEnabled(false);
        }

        if(dieNumberEditText.getText().length()<=0 || dieArcLengthEditText.getText().length()<=0 || dieDiameterEditText.getText().length()<=0){
            next.setEnabled(false);
        }
    }

    //startCamera Activity
    public void startCamera(){
            Intent intent = new Intent(PelletMillSettings.this , CameraActivity.class);
            // --- send data bundle   --- //
//            Bundle sketchBundle = new Bundle();
//            sketchBundle.putInt("dieNumber", dieNumber);
//            sketchBundle.putInt("dieWidth", dieWidth);
//            sketchBundle.putInt("dieDiameter", dieDiameter);
//            intent.putExtras(sketchBundle);
            // --- send to the sketch --- //
            startActivity(intent);
        }
    }

