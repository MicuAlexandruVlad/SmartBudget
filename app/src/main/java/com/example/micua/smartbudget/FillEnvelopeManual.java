package com.example.micua.smartbudget;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FillEnvelopeManual extends AppCompatActivity {
    private TextView howToFillEnv, date, leftMoneyTV, sweepEnvelopeTV;
    private EditText amount;
    private CheckBox checkBox;
    private Spinner scheduleSpinner;
    private Button next;
    private String spinnerChoice, fillMethodVal = "Fill ALL Envelopes", updatedBudget;
    private ListView month, year;
    private AdapterProgressBar monthAdapter, annualAdapter;
    private LinearLayout monthHolder, annualHolder;
    private int choiceHit, expenses, initialExpenses, numEnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelope_manual);

        final Intent intent = getIntent();
        final List<Envelope> monthly, annual;
        monthly = new ArrayList<>();
        annual = new ArrayList<>();
        getData(monthly, annual, intent);
        monthAdapter = new AdapterProgressBar(FillEnvelopeManual.this, R.layout.list_item_progress_bar, monthly);
        annualAdapter = new AdapterProgressBar(FillEnvelopeManual.this, R.layout.list_item_progress_bar, annual);
        numEnv = getNumEnv(monthly, annual);

        String title = "Fill Envelopes";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        howToFillEnv = (TextView) findViewById(R.id.tv_how_to_fill_env);
        date = (TextView) findViewById(R.id.tv_date);
        leftMoneyTV = (TextView) findViewById(R.id.tv_left_money);
        sweepEnvelopeTV = (TextView) findViewById(R.id.tv_sweep_envelope);
        amount = (EditText) findViewById(R.id.et_amount);
        scheduleSpinner = (Spinner) findViewById(R.id.spinner_schedule);
        checkBox = (CheckBox) findViewById(R.id.cb_schedule);
        next = (Button) findViewById(R.id.btn_next_fill_manual);
        month = (ListView) findViewById(R.id.lv_monthly_fill);
        year = (ListView) findViewById(R.id.lv_annual_fill);
        monthHolder = (LinearLayout) findViewById(R.id.ll_month_hold_fill);
        annualHolder = (LinearLayout) findViewById(R.id.ll_annual_hold_fill);

        setCurrentDate();
        initialExpenses = computeExpenses(monthly, annual, 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schedule_time_period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheduleSpinner.setAdapter(adapter);

        scheduleSpinner.setVisibility(View.GONE);
        spinnerChoice = "";
        sweepEnvelopeTV.setVisibility(View.GONE);
        leftMoneyTV.setVisibility(View.GONE);

        month.setAdapter(monthAdapter);
        year.setAdapter(annualAdapter);
        changeListViewSize(monthHolder, monthly);
        changeListViewSize(annualHolder, annual);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FillEnvelopeManual.this, MainScreens.class);
                transferData(monthly, annual, intent1);
                startActivity(intent1);
            }
        });

        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (initialExpenses != Integer.valueOf(amount.getText().toString())) {
                    initialExpenses = Integer.valueOf(amount.getText().toString());
                    showSweepEnvelope(initialExpenses, expenses, monthly, annual);
                    amount.clearFocus();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scheduleSpinner.setVisibility(View.VISIBLE);
                    spinnerChoice = "Once";
                }
                else {
                    scheduleSpinner.setVisibility(View.GONE);
                    spinnerChoice = "";
                }
            }
        });

        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerChoice = parent.getSelectedItem().toString();
                Toast.makeText(FillEnvelopeManual.this, spinnerChoice, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerChoice = "";
            }
        });

        howToFillEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FillEnvelopeManual.this);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(FillEnvelopeManual.this, android.R.layout.select_dialog_singlechoice);

                adapter.add("Fill ALL Envelopes");
                adapter.add("Fill Individually");

                builder.setIcon(R.drawable.logo)
                        .setTitle("How do you want to fund your Envelopes?")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = adapter.getItem(which);
                                TextView fillMethod = (TextView) findViewById(R.id.tv_fill_method);
                                if (name != null && name.equalsIgnoreCase("fill individually")) {
                                    setChoice(monthly, annual, 1);
                                    monthAdapter.notifyDataSetChanged();
                                    annualAdapter.notifyDataSetChanged();
                                    fillMethod.setText(name);
                                    fillMethodVal = fillMethod.getText().toString();
                                }
                                else {
                                    setChoice(monthly, annual, 2);
                                    monthAdapter.notifyDataSetChanged();
                                    annualAdapter.notifyDataSetChanged();
                                    fillMethod.setText(name);
                                    fillMethodVal = fillMethod.getText().toString();
                                }
                            }
                        });
                builder.create().show();
            }
        });

            month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final CustomEnvDialog dialog1 = new CustomEnvDialog(FillEnvelopeManual.this);
                    final int auxPos = position;
                    dialog1.setEnvData(monthly.get(position).getName(), monthly.get(position).getBudget());
                    dialog1.create();
                    dialog1.show();
                    dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            updatedBudget = dialog1.getNewBudget();
                            int choice = dialog1.getChoice();
                            monthly.get(auxPos).setChoice(dialog1.getChoice());
                            if (!updatedBudget.equalsIgnoreCase("0")) {
                                monthly.get(auxPos).setUpdatedBudget(Integer.valueOf(updatedBudget));
                                expenses = computeExpenses(monthly, annual, 1);
                                showSweepEnvelope(initialExpenses, expenses, monthly, annual);
                            }
                            if (choice == 2)
                                choiceHit += 1;
                            if (choiceHit <= numEnv)
                                expenses += Integer.valueOf(monthly.get(position).getBudget());
                            showSweepEnvelope(initialExpenses, expenses, monthly, annual);
                            monthAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final CustomEnvDialog dialog1 = new CustomEnvDialog(FillEnvelopeManual.this);
                    final int auxPos = position;
                    dialog1.setEnvData(annual.get(position).getName(), annual.get(position).getBudget());
                    dialog1.create();
                    dialog1.show();
                    dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            annual.get(auxPos).setChoice(dialog1.getChoice());
                            updatedBudget = dialog1.getNewBudget();
                            int choice = dialog1.getChoice();
                            if (!updatedBudget.equalsIgnoreCase("0")) {
                                annual.get(auxPos).setUpdatedBudget(Integer.valueOf(updatedBudget));
                                expenses = computeExpenses(monthly, annual, 1);

                            }
                            if (choice == 2)
                                choiceHit += 1;
                            if (choiceHit <= numEnv)
                                expenses += Integer.valueOf(monthly.get(position).getBudget());
                            showSweepEnvelope(initialExpenses, expenses, monthly, annual);
                            annualAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    private void setCurrentDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        date.setText(formattedDate);
    }

    private void getData(List<Envelope> monthly, List<Envelope> annual, Intent intent) {
        int sizeA = intent.getIntExtra("sizea", 0);
        int sizeM = intent.getIntExtra("sizem", 0);

        for (int i = 0; i < sizeA; i++) {
            annual.add(new Envelope(intent.getStringExtra("annual name " + i), intent.getStringExtra("annual budget " + i)));
        }

        for (int i = 0; i < sizeM; i++) {
            monthly.add(new Envelope(intent.getStringExtra("month name " + i), intent.getStringExtra("month budget " + i)));
        }
    }

    private void changeListViewSize(LinearLayout layout, List<Envelope> envelopes) {
        int size = envelopes.size();
        layout.getLayoutParams().height = 270 * size;
        layout.setLayoutParams(layout.getLayoutParams());
    }

    private int getNumEnv(List<Envelope> month, List<Envelope> year) {
        return month.size() + year.size();
    }

    private int computeExpenses(List<Envelope> month, List<Envelope> year, int decision) {
        int expenses = 0;
        if (decision == 1) {
            expenses = 0;
            for (int i = 0; i < month.size(); i++) {
                expenses += month.get(i).getUpdatedBudget();
            }
            for (int i = 0; i < year.size(); i++) {
                expenses += year.get(i).getUpdatedBudget();
            }
            return expenses;
        }
        for (int i = 0; i < month.size(); i++) {
            expenses += Integer.valueOf(month.get(i).getBudget());
        }
        for (int i = 0; i < year.size(); i++) {
            expenses += Integer.valueOf(year.get(i).getBudget());
        }

        amount.setText(expenses + "");

        return expenses;
    }

    private void setChoice(List<Envelope> month, List<Envelope> year, int choice) {
        int sizeM = month.size();
        int sizeY = year.size();

        for (int i = 0; i < sizeM; i++) {
            month.get(i).setChoice(choice);
        }
        for (int i = 0; i < sizeY; i++) {
            year.get(i).setChoice(choice);
        }
    }

    private void showSweepEnvelope(int initialExpenses, int expenses, List<Envelope> month, List<Envelope> year) {

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(FillEnvelopeManual.this, android.R.layout.select_dialog_singlechoice);

        for (int i = 0; i < month.size(); i++) {
            adapter.add(month.get(i).getName());
        }

        for (int i = 0; i < year.size(); i++) {
            adapter.add(year.get(i).getName());
        }

        if (expenses == initialExpenses) {
            sweepEnvelopeTV.setVisibility(View.GONE);
            leftMoneyTV.setVisibility(View.GONE);
        }

        if (expenses < initialExpenses) {
            sweepEnvelopeTV.setVisibility(View.VISIBLE);
            leftMoneyTV.setVisibility(View.VISIBLE);

            leftMoneyTV.setText("Sweep the remaining " + Math.abs(expenses - initialExpenses) + " into:");

            sweepEnvelopeTV.setText(adapter.getItem(0));
            sweepEnvelopeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FillEnvelopeManual.this);
                    builder.setTitle("Envelopes")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sweepEnvelopeTV.setText(adapter.getItem(which));
                                }
                            });
                    builder.create().show();
                }
            });
        }

        if (expenses > initialExpenses) {
            sweepEnvelopeTV.setVisibility(View.VISIBLE);
            leftMoneyTV.setVisibility(View.VISIBLE);

            leftMoneyTV.setText("Remove the overage of " + Math.abs(expenses - initialExpenses) + " from:");

            sweepEnvelopeTV.setText(adapter.getItem(0));
            sweepEnvelopeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FillEnvelopeManual.this);
                    builder.setTitle("Envelopes")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(FillEnvelopeManual.this, adapter.getItem(which), Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
            });
        }
    }

    private void transferData(List<Envelope> month, List<Envelope> year, Intent intent) {
        int sizeM = month.size(), sizeA = year.size();

        for (int i = 0; i < sizeA; i++) {
            intent.putExtra("final annual name " + i, year.get(i).getName());
            intent.putExtra("final annual budget " + i, year.get(i).getBudget());
        }

        for (int i = 0; i < sizeM; i++) {
            intent.putExtra("final month name " + i, month.get(i).getName());
            intent.putExtra("final month budget " + i, month.get(i).getBudget());
        }

        intent.putExtra("sizemf", sizeM);
        intent.putExtra("sizeaf", sizeA);
    }


}
