package com.cyberw01f.kalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Profile extends AppCompatActivity {

    TextView heightInput,weightInput;
    Button  updateButton,loadButton;
    Spinner unitSpinner, genderSpinner,ageSpinner;
    Boolean flip=false;
    SharedPreferences profilePrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        heightInput = (TextView) findViewById(R.id.heightInput);
        weightInput = (TextView) findViewById(R.id.weightInput);
        updateButton = (Button) findViewById(R.id.updateButton);
        loadButton = (Button) findViewById(R.id.loadButton);
        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
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

        // on click load profile data from shared prefs
        loadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                profilePrefs = getSharedPreferences(getString(R.string.profile_fileName),Context.MODE_PRIVATE);
                Toast.makeText(getBaseContext(), "Profile Loaded Successfully !",
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
            }
        });

        // on click take inputs and call kalc methods
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Boolean noInput=false; // to throw error if any input is missing

                // get all text inputs while checking for nulls
                if(heightInput.getText()==null || heightInput.getText().toString().equals("")){
                    noInput=true;
                }

                if(weightInput.getText()==null || weightInput.getText().toString().equals("")){
                    noInput=true;
                }

                if (noInput) {
                    Toast.makeText(getBaseContext(), "Missing input !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    profilePrefs = getSharedPreferences(getString(R.string.profile_fileName), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = profilePrefs.edit();
                    editor.putString(getString(R.string.height_pref),heightInput.getText().toString());
                    editor.putString(getString(R.string.weight_pref),weightInput.getText().toString());
                    editor.putInt(getString(R.string.unit_pref),unitSpinner.getSelectedItemPosition());
                    editor.putInt(getString(R.string.age_pref),ageSpinner.getSelectedItemPosition());
                    editor.putInt(getString(R.string.gender_pref),genderSpinner.getSelectedItemPosition());
                    editor.commit();
                    Toast.makeText(getBaseContext(), "Profile Updated Successfully !",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
