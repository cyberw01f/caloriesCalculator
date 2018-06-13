package com.cyberw01f.kalc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Results extends AppCompatActivity {


    // instances for widgets in the result activity
    TextView bmrValue,tdeeValue,bmiValue,bmiDetail,tryingToView;
    Button historyButton,logButton;
    double bmi=0.0,uWeight=0.0;
    int bmr=0,tdee=0,inCalories=0;
    String ttmsg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bmrValue = (TextView) findViewById(R.id.bmrValue);
        tdeeValue = (TextView) findViewById(R.id.tdeeValue);
        bmiValue = (TextView) findViewById(R.id.bmiValue);
        bmiDetail = (TextView) findViewById(R.id.bmiDetail);
        historyButton = (Button) findViewById(R.id.historyButton2);
        logButton = (Button) findViewById(R.id.logButton);
        tryingToView = (TextView) findViewById(R.id.tryingToView);

        // get data from intent
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null){

            bmr = (int)extrasBundle.getDouble("bmr");
            tdee = (int)extrasBundle.getDouble("tdee");
            bmi = extrasBundle.getDouble("bmi");
            uWeight = Math.round(extrasBundle.getDouble("weight")*100.0)/100.0;
            ttmsg = extrasBundle.getString("tt");


        }

        String msg;
        if (bmi < 18.5) {
            msg = "Under Weight";
        } else if (bmi < 24.9) {
            msg =  "Normal Weight";
        } else if (bmi < 29.9) {
            msg = "Over Weight";
        } else if (bmi < 40) {
            msg = "Obese";
        } else {
            msg = "an inhuman";
            bmi=0.0;
        }

        String ttmsg_new;

        if (ttmsg.equals("Loose weight")){
            inCalories = tdee - 500;
            if(inCalories<bmr) inCalories=bmr;
            ttmsg_new  = "To loose weight you should limit your intake to " + inCalories + " calories";
        }else if (ttmsg.equals("Gain weight")){
            inCalories = tdee + 500;
            ttmsg_new = "To gain weight you should increase your intake to " + inCalories + " calories";
        }else if (ttmsg.equals("Maintain weight")){
            inCalories=tdee;
            ttmsg_new = "To maintain weight your intake should be " + inCalories + " calories";
        } else
            ttmsg_new = "Something went wrong";

        // populate results
        bmrValue.setText(""+bmr);
        tdeeValue.setText(""+tdee);
        bmiValue.setText(""+bmi);
        bmiDetail.setText(msg);
        tryingToView.setText(ttmsg_new);


        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                //LocalDateTime now = LocalDateTime.now();
                long millis=System.currentTimeMillis();
                java.sql.Date date=new java.sql.Date(millis);
                try {
                    FileOutputStream fileout=openFileOutput("history_data", MODE_APPEND);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                    outputWriter.write(date+",");
                    outputWriter.write(inCalories+",");
                    outputWriter.write(uWeight+"\n");
                    outputWriter.close();

                    //display file saved message
                    Toast.makeText(getBaseContext(), "Data saved successfully!",
                            Toast.LENGTH_SHORT).show();
                    logButton.setEnabled(false);
                    logButton.setAlpha(0.5f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
            );

        historyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(Results.this,History.class);
                startActivity(i);

                Toast.makeText(getBaseContext(), "History under construction !",
                        Toast.LENGTH_SHORT).show();
            }


        });
    }
}
