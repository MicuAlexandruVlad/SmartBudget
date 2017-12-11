package com.example.micua.smartbudget;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FillEnvelopesMaster extends AppCompatActivity {
    private EditText receivedFrom, amount, unallocatedDescription;
    private Button fromUnallocated, fromNewIncome;
    private CheckBox schedule;
    private Spinner fillMethod, account;
    private LinearLayout frommNewIncomeHolder, envelopesHolder, fromUnallocatedHolder, monthHolder,
            yearHolder, monthUnallocatedHolder, yearUnallocatedHolder;
    private ListView envelopesMonth, envelopesYear, envelopesUnallocatedMonth, envelopesUnallocatedYear;
    private Intent returnIntent;
    private List<Envelope> month, year;
    private AdapterNoProgressBar envelopesAdapterMonth, envelopesAdapterYear;
    private String fillChoice = "Unallocated", receivedFromValue, unallocatedDescriptionValue, date, dateUnallocated;
    private int option = 1;
    private float amountValue;
    private boolean isScheduled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelopes_master);

        returnIntent = getIntent();

        String title = "Fill Envelopes";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        month = new ArrayList<>();
        year = new ArrayList<>();
        getData(month, year, returnIntent);
        envelopesAdapterMonth = new AdapterNoProgressBar(FillEnvelopesMaster.this, R.layout.list_item_no_bar, month);
        envelopesAdapterYear = new AdapterNoProgressBar(FillEnvelopesMaster.this, R.layout.list_item_no_bar, year);


        fromUnallocated = (Button) findViewById(R.id.btn_from_unallocated_master);
        fromNewIncome = (Button) findViewById(R.id.btn_from_new_income_master);
        fillMethod = (Spinner) findViewById(R.id.spinner_fill_method_master);
        account = (Spinner) findViewById(R.id.spinner_account_master);
        frommNewIncomeHolder = (LinearLayout) findViewById(R.id.ll_from_new_income_holder);
        envelopesHolder = (LinearLayout) findViewById(R.id.ll_envelopes_holder_master);
        fromUnallocatedHolder = (LinearLayout) findViewById(R.id.ll_unallocated_holder_master);
        monthHolder = (LinearLayout) findViewById(R.id.ll_month_hold_master);
        yearHolder = (LinearLayout) findViewById(R.id.ll_year_hold_master);
        monthUnallocatedHolder = (LinearLayout) findViewById(R.id.ll_month_hold_unallocated_master);
        yearUnallocatedHolder = (LinearLayout) findViewById(R.id.ll_year_hold_unallocated_master);
        envelopesMonth = (ListView) findViewById(R.id.lv_month_master);
        envelopesYear = (ListView) findViewById(R.id.lv_year_master);
        envelopesUnallocatedMonth = (ListView) findViewById(R.id.lv_month__unallocated_master);
        envelopesUnallocatedYear = (ListView) findViewById(R.id.lv_year_unallocated_master);
        receivedFrom = (EditText) findViewById(R.id.et_income_name_master);
        amount = (EditText) findViewById(R.id.et_amount_master);
        unallocatedDescription = (EditText) findViewById(R.id.et_income_description_master);
        schedule = (CheckBox) findViewById(R.id.cb_schedule_master);

        envelopesYear.setAdapter(envelopesAdapterYear);
        envelopesMonth.setAdapter(envelopesAdapterMonth);
        envelopesUnallocatedYear.setAdapter(envelopesAdapterYear);
        envelopesUnallocatedMonth.setAdapter(envelopesAdapterMonth);

        monthHolder.getLayoutParams().height = 230 * month.size();
        monthHolder.setLayoutParams(monthHolder.getLayoutParams());
        yearHolder.getLayoutParams().height = 230 * year.size();
        yearHolder.setLayoutParams(yearHolder.getLayoutParams());
        monthUnallocatedHolder.getLayoutParams().height = 230 * month.size();
        monthUnallocatedHolder.setLayoutParams(monthUnallocatedHolder.getLayoutParams());
        yearUnallocatedHolder.getLayoutParams().height = 230 * year.size();
        yearUnallocatedHolder.setLayoutParams(yearUnallocatedHolder.getLayoutParams());

        List<String> choices = new ArrayList<>();
        choices.add("Keep Unallocated");
        choices.add("Fill Each Envelope");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_custom_spinner, choices);

        fillMethod.setAdapter(adapter);
        List<String> accountValue = new ArrayList<>();
        accountValue.add("My Account");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_custom_spinner, accountValue);
        account.setAdapter(adapter1);

        envelopesMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                dialogMaster.create();
                dialogMaster.show();
            }
        });

        envelopesYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                dialogMaster.create();
                dialogMaster.show();
            }
        });

        fillMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).equalsIgnoreCase("fill each envelope")) {
                    envelopesHolder.setVisibility(View.VISIBLE);
                    fillChoice = "Fill";
                }
                else {
                    envelopesHolder.setVisibility(View.GONE);
                    fillChoice = "Unallocated";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromUnallocatedHolder.setVisibility(View.GONE);
        envelopesHolder.setVisibility(View.GONE);

        fromUnallocated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromUnallocated.setBackground(getResources().getDrawable(R.drawable.ripple_color_accent));
                fromNewIncome.setBackground(getResources().getDrawable(R.drawable.ripple_color_gray));
                frommNewIncomeHolder.setVisibility(View.GONE);
                fromUnallocatedHolder.setVisibility(View.VISIBLE);
                option = 2;
            }
        });

        fromNewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromNewIncome.setBackground(getResources().getDrawable(R.drawable.ripple_color_accent));
                fromUnallocated.setBackground(getResources().getDrawable(R.drawable.ripple_color_gray));
                frommNewIncomeHolder.setVisibility(View.VISIBLE);
                fromUnallocatedHolder.setVisibility(View.GONE);
                option = 1;
            }
        });



    }

    private void getData(List<Envelope> monthly, List<Envelope> annual, Intent data) {
        int sizeA = data.getIntExtra("sizeaf", 0);
        int sizeM = data.getIntExtra("sizemf", 0);

        for (int i = 0; i < sizeA; i++) {
            annual.add(new Envelope(data.getStringExtra("final annual name " + i), data.getStringExtra("final annual budget " + i)));
        }

        for (int i = 0; i < sizeM; i++) {
            monthly.add(new Envelope(data.getStringExtra("final month name " + i), data.getStringExtra("final month budget " + i)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fill_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_done_fill_master){
            if (option == 1) {
                if (fillChoice.equalsIgnoreCase("unallocated")) {
                    int pass1 = 0, pass2 = 0;
                    if (!receivedFrom.getText().toString().equalsIgnoreCase("")) {
                        receivedFromValue = receivedFrom.getText().toString();
                        pass1 = 1;
                    }
                    else {
                        Toast.makeText(this, "Enter a valid description", Toast.LENGTH_SHORT).show();
                        pass1 = 0;
                    }
                    if (!amount.getText().toString().equalsIgnoreCase("")) {
                        amountValue = Float.valueOf(amount.getText().toString());
                        pass2 = 1;
                    }
                    else {
                        Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                        pass2 = 0;
                    }
                    isScheduled = schedule.isChecked();
                    returnIntent.putExtra("schedule 1", isScheduled);
                    returnIntent.putExtra("received from", receivedFromValue);
                    returnIntent.putExtra("amount", amountValue);
                    returnIntent.putExtra("choice", fillChoice);
                    returnIntent.putExtra("op", option);
                    if (pass1 == 1 && pass2 == 1) {
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
                else {
                    int pass1 = 0, pass2 = 0;
                    if (!receivedFrom.getText().toString().equalsIgnoreCase("")) {
                        receivedFromValue = receivedFrom.getText().toString();
                        pass1 = 1;
                    }
                    else {
                        pass1 = 0;
                        Toast.makeText(this, "Enter a valid description", Toast.LENGTH_SHORT).show();
                    }
                    if (!amount.getText().toString().equalsIgnoreCase("")) {
                        amountValue = Float.valueOf(amount.getText().toString());
                        pass2 = 1;
                    }
                    else {
                        Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                        pass2 = 0;
                    }
                    isScheduled = schedule.isChecked();
                    returnIntent.putExtra("schedule 1", isScheduled);
                    returnIntent.putExtra("received from", receivedFromValue);
                    returnIntent.putExtra("amount", amountValue);
                    returnIntent.putExtra("op", option);
                    if (pass1 == 1 && pass2 == 1) {
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
            }
            return true;
        }
        if (id == R.id.menu_help_fill_master) {
            startActivity(new Intent(FillEnvelopesMaster.this, FillMasterHelp.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
