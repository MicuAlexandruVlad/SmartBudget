package com.example.micua.smartbudget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.ETC1;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SetBudget extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner time;
    private Button save;
    public static final int REQ_CODE = 1;
    private EditText income;
    private String item;
    private int spinnerPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        SharedPreferences sharedPreferences = this.getPreferences(CONTEXT_RESTRICTED);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences spinnerSaving = getSharedPreferences("spinner state",0);

        String title = "Income";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        final Intent returnIntent = getIntent();

        time = (Spinner) findViewById(R.id.spinner_time_period);
        save = (Button) findViewById(R.id.btn_save);
        income = (EditText) findViewById(R.id.et_income);



        income.setText(sharedPreferences.getString(getString(R.string.income_set_budget), "0"));





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);

        time.setSelection(spinnerSaving.getInt("spinnerPos", spinnerPos));

        time.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(income.getText().toString()) <= 0)
                    Toast.makeText(SetBudget.this, "Please enter your income", Toast.LENGTH_SHORT).show();
                else {
                    editor.putString(getString(R.string.income_set_budget), income.getText().toString());
                    editor.apply();
                    returnIntent.putExtra("income", income.getText().toString());
                    returnIntent.putExtra("period", item);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        spinnerPos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences spinnerSaving = getSharedPreferences("spinner state",0);

        SharedPreferences.Editor editor = spinnerSaving.edit();
        editor.putInt("spinnerPos", spinnerPos);
        editor.commit();
    }
}
