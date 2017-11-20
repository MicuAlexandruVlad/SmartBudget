package com.example.micua.smartbudget;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class AddEnvelope extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private EditText envelopeName, budgetET;
    private Button save;
    private String item;
    private TextView budgetPerMonth, launchDatePicker;
    private int spinnerValue;
    private java.util.Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_envelope);

        final Intent returnIntent = getIntent();

        String title = "AddEnvelope";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        calendar = java.util.Calendar.getInstance();
        spinner = (Spinner) findViewById(R.id.spinner_add_envelope);
        envelopeName = (EditText) findViewById(R.id.et_envelope_name);
        budgetET = (EditText) findViewById(R.id.et_budget_amount);
        save = (Button) findViewById(R.id.btn_save_envelope);
        budgetPerMonth = (TextView) findViewById(R.id.tv_budget_per_month);
        launchDatePicker = (TextView) findViewById(R.id.tv_launch_date_picker);

        item = "monthly";

        int usersChoice = spinner.getSelectedItemPosition();
        SharedPreferences sharedPref = getSharedPreferences("FileName",0);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("userChoiceSpinner",usersChoice);
        prefEditor.commit();

        spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if(spinnerValue != -1) {
            // set the selected value of the spinner
            spinner.setSelection(spinnerValue);
        }

        budgetET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && item.equalsIgnoreCase("every year")) {
                    budgetPerMonth.setText(Integer.valueOf(budgetET.getText().toString()) / 12 + " per month");
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.envelope_time_period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (returnIntent.getStringExtra("decision") != null) {
            envelopeName.setText(returnIntent.getStringExtra("envelope name"));
            budgetET.setText(returnIntent.getStringExtra("budget"));
            spinner.setSelection(returnIntent.getIntExtra("spinner value", 0));
        }

        spinner.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String envelope = envelopeName.getText().toString();
                String budget = budgetET.getText().toString();

                if (Integer.valueOf(budget) <= 0)
                    Toast.makeText(AddEnvelope.this, "Enter a valid budget", Toast.LENGTH_SHORT).show();
                else {
                    returnIntent.putExtra("budget", budget);
                    returnIntent.putExtra("factor", returnIntent.getStringExtra("factor"));
                    returnIntent.putExtra("envelope", envelope);
                    returnIntent.putExtra("spinner value", spinnerValue);
                    returnIntent.putExtra("spinner position", item);
                    if (returnIntent.getStringExtra("decision") != null) {
                        returnIntent.putExtra("position", returnIntent.getStringExtra("pos"));
                    }
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(java.util.Calendar.YEAR, year);
                calendar.set(java.util.Calendar.MONTH, month);
                calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        launchDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEnvelope.this, date, calendar.get(java.util.Calendar.YEAR),
                        calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getSelectedItem().toString();
        if (item.equalsIgnoreCase("every year"))
            budgetPerMonth.setText(Integer.valueOf(budgetET.getText().toString()) / 12 + " per month");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        java.text.SimpleDateFormat sdf = null;
        sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
        launchDatePicker.setText(sdf.format(calendar.getTime()));

    }

}
