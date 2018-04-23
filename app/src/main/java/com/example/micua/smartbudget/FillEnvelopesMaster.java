package com.example.micua.smartbudget;

import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FillEnvelopesMaster extends AppCompatActivity {
    private EditText receivedFrom, amount, unallocatedDescription;
    private TextView leftUnallocatedTV, usedThisFillTV;
    private Button fromUnallocated, fromNewIncome;
    private CheckBox schedule;
    private Spinner fillMethod, account;
    private LinearLayout frommNewIncomeHolder, envelopesHolder, fromUnallocatedHolder, monthHolder,
            yearHolder, monthUnallocatedHolder, yearUnallocatedHolder;
    private ListView envelopesMonth, envelopesYear, envelopesUnallocatedMonth, envelopesUnallocatedYear;
    private Intent returnIntent;
    private List<Envelope> month, year;
    private AdapterNoProgressBar envelopesAdapterMonth, envelopesAdapterYear;
    private String fillChoice = "Unallocated", receivedFromValue, unallocatedDescriptionValue;
    private int option = 1;
    private float amountValue, unallocatedAmount;
    private boolean isScheduled;
    private List<Transaction> transactionsFromUnallocated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelopes_master);

        returnIntent = getIntent();
        unallocatedAmount = returnIntent.getFloatExtra("unallocated amount", 0);

        String title = "Fill Envelopes";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        month = new ArrayList<>();
        year = new ArrayList<>();
        getData(month, year, returnIntent);
        getEnvTransactions(returnIntent);
        envelopesAdapterMonth = new AdapterNoProgressBar(FillEnvelopesMaster.this, R.layout.list_item_no_bar, month);
        envelopesAdapterYear = new AdapterNoProgressBar(FillEnvelopesMaster.this, R.layout.list_item_no_bar, year);


        usedThisFillTV = (TextView) findViewById(R.id.tv_used_this_fill_master);
        leftUnallocatedTV = (TextView) findViewById(R.id.tv_amount_left_unallocated_master);
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

        leftUnallocatedTV.setText(unallocatedAmount + "");
        usedThisFillTV.setText("0.00 Used this Fill");

        envelopesMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                final int pos = i;
                dialogMaster.setEnvData(month.get(i).getName(), month.get(i).getBudget());
                dialogMaster.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        int choice = dialogMaster.getChoice();
                        month.get(pos).setChoice(choice);
                        envelopesAdapterMonth.notifyDataSetChanged();
                    }
                });
                dialogMaster.create();
                dialogMaster.show();
            }
        });

        envelopesYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                final int pos = i;
                dialogMaster.setEnvData(year.get(i).getName(), year.get(i).getBudget());
                dialogMaster.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        int choice = dialogMaster.getChoice();
                        year.get(pos).setChoice(choice);
                        envelopesAdapterYear.notifyDataSetChanged();
                    }
                });
                dialogMaster.create();
                dialogMaster.show();
            }
        });

        envelopesUnallocatedMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                final int pos = i;
                dialogMaster.setEnvData(month.get(i).getName(), month.get(i).getBudget());
                dialogMaster.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        int choice = dialogMaster.getChoice();
                        month.get(pos).setChoice(choice);
                        unallocatedDescriptionValue = unallocatedDescription.getText().toString();
                        float currentTotalBudget = 0;
                        switch (choice) {
                            //TODO: solve this shit
                            case 2: {
                                currentTotalBudget = previousSum(month.get(pos).getBudgetTrans()) + Float.valueOf(dialogMaster.getNewBudget());
                                usedThisFillTV.setText((Float.valueOf(usedThisFillTV.getText().toString().split(" ")[0])
                                        + Float.valueOf(dialogMaster.getNewBudget())) + " Used this Fill");
                                float val = (Float.valueOf(leftUnallocatedTV.getText().toString()) -
                                        Float.valueOf(usedThisFillTV.getText().toString().split(" ")[0]));
                                leftUnallocatedTV.setText(val + "");
                                if (val < 0)
                                    leftUnallocatedTV.setTextColor(getResources().getColor(R.color.red));
                                month.get(pos).addNameTrans(unallocatedDescriptionValue);
                                month.get(pos).addDateTrans(getCurrentDate());
                                month.get(pos).addBudgetTrans(Float.valueOf(dialogMaster.getNewBudget()));
                                month.get(pos).addTotalBudget(currentTotalBudget);
                            }
                        }
                        envelopesAdapterMonth.notifyDataSetChanged();
                    }
                });
                dialogMaster.create();
                dialogMaster.show();
            }
        });

        envelopesUnallocatedYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CustomEnvDialogMaster dialogMaster = new CustomEnvDialogMaster(FillEnvelopesMaster.this);
                final int pos = i;
                dialogMaster.setEnvData(year.get(pos).getName(), year.get(pos).getBudget());
                dialogMaster.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        int choice = dialogMaster.getChoice();
                        float currentTotalBudget;
                        switch (choice) {
                            case 2: {currentTotalBudget = previousSum(year.get(pos).getBudgetTrans()) + Float.valueOf(dialogMaster.getNewBudget());
                                     year.get(pos).addTotalBudget(currentTotalBudget);
                                     year.get(pos).addDateTrans(getCurrentDate());
                                     year.get(pos).addBudgetTrans(Float.valueOf(dialogMaster.getNewBudget()));
                                     year.get(pos).addNameTrans("Fill from Unallocated");}
                        }
                    }
                });
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
                resetChoice(month, year);
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
                resetChoice(month, year);
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
            if (option == 2) {
                for (int i = 0; i < month.size(); i++) {
                    sendTransactions(month.get(i).getName(), month.get(i).getDateTrans(), month.get(i).getNameTrans(),
                            month.get(i).getTotalBudget(), month.get(i).getBudgetTrans(), returnIntent);
                }
                for (int i = 0; i < year.size(); i++) {
                    sendTransactions(year.get(i).getName(), year.get(i).getDateTrans(), year.get(i).getNameTrans(),
                            year.get(i).getTotalBudget(), year.get(i).getBudgetTrans(), returnIntent);
                }
                returnIntent.putExtra("op", option);
                returnIntent.putExtra("left unallocated", leftUnallocatedTV.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            return true;
        }
        if (id == R.id.menu_help_fill_master) {
            startActivity(new Intent(FillEnvelopesMaster.this, FillMasterHelp.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetChoice(List<Envelope> month, List<Envelope> year) {
        for (int i = 0; i < month.size(); i++) {
            month.get(i).setChoice(1);
        }
        for (int i = 0; i < year.size(); i++) {
            year.get(i).setChoice(1);
        }
        envelopesAdapterMonth.notifyDataSetChanged();
        envelopesAdapterYear.notifyDataSetChanged();
    }

    private String getCurrentDate() {
        DateFormat f = new SimpleDateFormat("dd/MM");
        Date date = new Date();
        return f.format(date);
    }

    private float previousSum(List<Float> floats) {
        float s = 0;
        for (int i = 0; i < floats.size(); i++) {
            s += floats.get(i);
        }
        return s;
    }

    private void getEnvTransactions(Intent intent) {
        String name = intent.getStringExtra("env");
        for (int i = 0; i < month.size(); i++) {
            if (month.get(i).getName().equals(name) && month.size() != 1) {
                for (int j = 1; j < intent.getIntExtra("transSize", 2); j++) {
                    month.get(i).addTotalBudget(intent.getFloatExtra(name + " total " + j, 0));
                    month.get(i).addBudgetTrans(intent.getFloatExtra(name + " budgetAdded " + j, 0));
                    month.get(i).addNameTrans(intent.getStringExtra(name + " name " + j));
                    month.get(i).addDateTrans(intent.getStringExtra(name + " date " + j));
                }
            }
        }
        for (int i = 0; i < year.size(); i++) {

            if (year.get(i).getName().equals(name) && year.size() != 1) {
                for (int j = 1; j < intent.getIntExtra("transSize", 2); j++) {
                    year.get(i).addTotalBudget(intent.getFloatExtra(name + " total " + j, 0));
                    year.get(i).addBudgetTrans(intent.getFloatExtra(name + " budgetAdded " + j, 0));
                    year.get(i).addNameTrans(intent.getStringExtra(name + " name " + j));
                    year.get(i).addDateTrans(intent.getStringExtra(name + " date " + j));
                }
            }
        }

    }

    private void sendTransactions(String envName, List<String> date, List<String> name, List<Float> total, List<Float> budgetAdded, Intent intent) {
        intent.putExtra(envName + " transSize", date.size());
        for (int i = 0; i < date.size(); i++) {
            intent.putExtra(envName + " date " + i,date.get(i));
            intent.putExtra(envName + " name " + i,name.get(i));
            intent.putExtra(envName + " budgetAdded " + i,total.get(i));
            intent.putExtra(envName + " total " + i,budgetAdded.get(i));
        }
    }
}
