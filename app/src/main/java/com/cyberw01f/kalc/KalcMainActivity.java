package com.cyberw01f.kalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class KalcMainActivity extends AppCompatActivity {

    // instances for widgets in the main activity
    TextView heightInput,weightInput;
    Button  calculateButton, historyButton;
    Spinner unitSpinner, genderSpinner,ageSpinner, activitySpinner, tryingToSpinner;
    Boolean flip=false;
    SharedPreferences profilePrefs;

    // variables for calculation
    double uHeight=0.0, uWeight=0.0, bmr=0.0, tdee=0.0, bmi=0.0;
    int age=0,gValue=0;
    //boolean female=false,unitSwitch=false;

    private double kalcBmr(double height, double weight, int age, int gender){

        if (gender==1)
            return (double) Math.round((655 + (9.6 * weight) + (1.8 * height)) - (4.7 * age));
        else {
            return (double) Math.round((66 + (13.7 * weight) + (5 * height)) - (6.8 * age));
        }
    }

    private double kalcBmi(double height, double weight){

        double tempBmi = uWeight/uHeight/uHeight*(100.0*100.0);
        return Math.round(tempBmi*100.0)/100.0;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kalc_main);


        // link all instances to widgets
        heightInput = (TextView) findViewById(R.id.heightInput);
        weightInput = (TextView) findViewById(R.id.weightInput);
        calculateButton = (Button) findViewById(R.id.calculateButton);
        historyButton = (Button) findViewById(R.id.historyButton);
        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        activitySpinner = (Spinner) findViewById(R.id.activitySpinner);
        tryingToSpinner = (Spinner) findViewById(R.id.tryingToSpinner);
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);


        // populate age values
        List ages = new ArrayList<Integer>();
        for (int i = 18; i <= 100; i++) {
            ages.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, ages);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        ageSpinner.setAdapter(spinnerArrayAdapter);



        // change height and weight hints according to selection

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==1) {
                    heightInput.setHint("Inches");
                    weightInput.setHint("Pounds");
                    flip = true;
                }
                else {
                    heightInput.setHint("Centimeters");
                    weightInput.setHint("Kilograms");
                    flip=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            flip=false;
            }

        });

        profilePrefs = getSharedPreferences(getString(R.string.profile_fileName),Context.MODE_PRIVATE);
        Toast.makeText(getBaseContext(), "Data loaded from saved profile !",
                Toast.LENGTH_SHORT).show();
        if(profilePrefs.contains(getString(R.string.height_pref))){
            String value = profilePrefs.getString(getString(R.string.height_pref),"0");
            heightInput.setText(value);
        }

        if(profilePrefs.contains(getString(R.string.weight_pref))){
            String value = profilePrefs.getString(getString(R.string.weight_pref),"0");
            weightInput.setText(value);
        }

        if(profilePrefs.contains(getString(R.string.unit_pref))){
            int value = profilePrefs.getInt(getString(R.string.unit_pref),0);
            unitSpinner.setSelection(value);
        }

        if(profilePrefs.contains(getString(R.string.gender_pref))){
            int value = profilePrefs.getInt(getString(R.string.gender_pref),0);
            genderSpinner.setSelection(value);
        }
        if(profilePrefs.contains(getString(R.string.age_pref))){
            int value = profilePrefs.getInt(getString(R.string.age_pref),0);
            ageSpinner.setSelection(value);
        }
        // on click take inputs and call kalc methods
        calculateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Boolean noInput=false; // to throw error if any input is missing

                // get all text inputs while checking for nulls
                if(heightInput.getText()==null || heightInput.getText().toString().equals("")){
                    uHeight=0.0;
                    noInput=true;
                } else
                    uHeight = Double.parseDouble(heightInput.getText().toString());

                if(weightInput.getText()==null || weightInput.getText().toString().equals("")){
                    uWeight=0.0;
                    noInput=true;
                } else
                    uWeight = Double.parseDouble(weightInput.getText().toString());


                    age = Integer.parseInt(ageSpinner.getSelectedItem().toString());

                // assign int value acc to gender
                if(genderSpinner.getSelectedItem().toString().equals("Male"))
                    gValue=0;
                else
                    gValue=1;

                if (noInput) {
                    Toast.makeText(getBaseContext(), "Missing input !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // call kalc methods after proper unit conversion
                    if (unitSpinner.getSelectedItem().toString().equals("Imperial")) {
                        uHeight = uHeight * 2.54;
                        uWeight = uWeight / 2.2;
                        bmr = kalcBmr(uHeight, uWeight, age, gValue);
                        bmi = kalcBmi(uHeight, uWeight);
                    } else {
                        bmr = kalcBmr(uHeight, uWeight, age, gValue);
                        bmi = kalcBmi(uHeight, uWeight);
                    }

                    // calculate tdee according to activity level
                    if (activitySpinner.getSelectedItem().toString().equals("Sedentary")) {
                        tdee = Math.round(1.2 * bmr);
                    } else if (activitySpinner.getSelectedItem().toString().equals("Lightly active")) {
                        tdee = Math.round(1.375 * bmr);
                    } else if (activitySpinner.getSelectedItem().toString().equals("Moderately active")) {
                        tdee = Math.round(1.55 * bmr);
                    } else if (activitySpinner.getSelectedItem().toString().equals("Very active")) {
                        tdee = Math.round(1.725 * bmr);
                    } else if (activitySpinner.getSelectedItem().toString().equals("Extremely active")) {
                        tdee = Math.round(1.9 * bmr);
                    }


                    String ttmsg = tryingToSpinner.getSelectedItem().toString();
                    //Create bundle and put our data into it
                    Bundle bundle = new Bundle();
                    bundle.putDouble("bmr", bmr);
                    bundle.putDouble("tdee", tdee);
                    bundle.putDouble("bmi", bmi);
                    bundle.putDouble("weight",uWeight);
                    bundle.putString("tt",ttmsg);

                    Intent i = new Intent(KalcMainActivity.this, Results.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }

            }



        });

    historyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(KalcMainActivity.this,History.class);
                startActivity(i);

                Toast.makeText(getBaseContext(), "History under construction !",
                        Toast.LENGTH_SHORT).show();
            }


        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_all) {
            heightInput.setText("");
            weightInput.setText("");
            ageSpinner.setSelection(0);
            unitSpinner.setSelection(0);
            genderSpinner.setSelection(0);
            activitySpinner.setSelection(0);
            tryingToSpinner.setSelection(0);
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this,aboutMe.class);
            startActivity(i);
        }

        if (id == R.id.action_edit_profile){
            Intent i = new Intent(this,Profile.class);
            startActivity(i);
        }

        if(id == R.id.codeLink){
            Intent i = new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://github.com/cyberw01f/Kalc"));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}
