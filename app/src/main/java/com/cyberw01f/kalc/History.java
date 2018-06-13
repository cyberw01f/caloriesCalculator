package com.cyberw01f.kalc;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;


import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int size = 0;
        String[] record = new String[2];
        List<String[]> records = new ArrayList<>();
        TableLayout historyTable = (TableLayout) findViewById(R.id.historyTable);

        try {
            FileInputStream fileIn=openFileInput("history_data");
            Scanner scanner = new Scanner(fileIn);
            scanner.useDelimiter(",");


            while(scanner.hasNext()) {
                record = scanner.nextLine().split(",");
                records.add(record);
                size++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                }


        for (int i = 0; i < size; i++)
        {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams txtViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(txtViewParams);

            if(i%2!=0){
                tableRow.setBackgroundColor(Color.LTGRAY);
            }

            String[] rows = records.get(i);

            TextView dateView = new TextView(this);
            dateView.setText(rows[0]);
            dateView.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(dateView);

            TextView intakeView = new TextView(this);
            intakeView.setText(rows[1]);
            intakeView.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(intakeView);

            TextView weightView = new TextView(this);
            weightView.setText(rows[2]);
            weightView.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(weightView);

            historyTable.addView(tableRow);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.history_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_history) {

            try {
                deleteFile("history_data");
                super.recreate();
                Toast.makeText(getBaseContext(), "History deleted !",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }

        if (id == R.id.action_export_history){
            Toast.makeText(getBaseContext(), "History cannot be exported for now !",
                    Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }
}
