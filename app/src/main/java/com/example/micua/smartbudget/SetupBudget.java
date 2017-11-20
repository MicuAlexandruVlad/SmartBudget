package com.example.micua.smartbudget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SetupBudget extends AppCompatActivity {
    private Button addEnvelope, next;
    private TextView setupBudget, remainingMoney, envelopesMonth, monthLeftEnvelopes, otherEnv, leftOtherEnv;
    public static final int REQ_CODE = 1;
    private int  budget, numMonthlyEnv, numAnnualEnvelopes;
    private SharedPreferences sharedPreferences;
    private String income = "0", period, budgetAmount;
    private int totalMoney;
    private LinearLayout remainingBalance, monthListHolder;
    private List<Envelope> envelopes, annualEnv;
    private CustomListAdapter adapter, annualAdapter;
    private List<Integer> incomes;
    private ListView monthlyEnvelopes, annualEnvelopes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_budget);

        Intent intent = getIntent();

        String title = "SetupBudget";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        envelopes = new ArrayList<>();
        annualEnv = new ArrayList<>();
        incomes = new ArrayList<>();
        numMonthlyEnv = envelopes.size();
        numAnnualEnvelopes = annualEnv.size();
        adapter = new CustomListAdapter(SetupBudget.this, R.layout.list_item, envelopes);
        annualAdapter = new CustomListAdapter(SetupBudget.this, R.layout.list_item, annualEnv);


        remainingMoney = (TextView) findViewById(R.id.tv_remaining_money);
        addEnvelope = (Button) findViewById(R.id.btn_add_envelope);
        next = (Button) findViewById(R.id.btn_next);
        remainingBalance = (LinearLayout) findViewById(R.id.ll_setup_budget_remaining_balance);
        monthlyEnvelopes = (ListView) findViewById(R.id.lv_monthly);
        envelopesMonth = (TextView) findViewById(R.id.tv_budget_monthly);
        monthLeftEnvelopes = (TextView) findViewById(R.id.tv_envelopes_left);
        monthListHolder = (LinearLayout) findViewById(R.id.scrollView2);
        annualEnvelopes = (ListView) findViewById(R.id.lv_annual);
        otherEnv = (TextView) findViewById(R.id.tv_budget_time_other);
        leftOtherEnv = (TextView) findViewById(R.id.tv_other_envelopes_left);

        envelopesMonth.setText("Monthly (" + numMonthlyEnv + ")");
        otherEnv.setText("Other (" + numAnnualEnvelopes + ")");
        monthLeftEnvelopes.setText(10 - numMonthlyEnv + " of 10 free envelopes left.");
        leftOtherEnv.setText(10 - numAnnualEnvelopes + " of 10 free envelopes left.");

        monthlyEnvelopes.setAdapter(adapter);
        annualEnvelopes.setAdapter(annualAdapter);

        remainingMoney.setText(getDefaults("remaining money", SetupBudget.this));
        income = remainingMoney.getText().toString().split("/")[0];
        incomes.add(0);
        System.out.println(incomes.get(0));

        monthlyEnvelopes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SetupBudget.this, AddEnvelope.class);
                intent.putExtra("decision", "1");
                intent.putExtra("envelope name", envelopes.get(position).getName());
                intent.putExtra("budget", envelopes.get(position).getBudget());
                intent.putExtra("pos", position + "");
                intent.putExtra("spinner value", envelopes.get(position).getSpinnerValue());
                intent.putExtra("factor", "monthly");
                startActivityForResult(intent, 3);
            }
        });

        monthlyEnvelopes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetupBudget.this);
                builder.setTitle("Delete " + envelopes.get(position).getName() + " envelope");
                builder.setMessage("You are about to delete the " + envelopes.get(position).getName() + " envelope."
                        + " Are you sure you want to continue ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = envelopes.get(position).getName();
                        String budget = envelopes.get(position).getBudget();
                        envelopes.remove(position);
                        adapter.notifyDataSetChanged();
                        numMonthlyEnv = envelopes.size();
                        monthListHolder.getLayoutParams().height = 200 * numMonthlyEnv;
                        monthListHolder.setLayoutParams(monthListHolder.getLayoutParams());
                        monthLeftEnvelopes.setText(10 - numMonthlyEnv + " of 10 free envelopes left");
                        envelopesMonth.setText("Monthly (" + numMonthlyEnv + ")");
                        Toast.makeText(SetupBudget.this, name + " envelope has been deleted", Toast.LENGTH_SHORT).show();

                        income = String.valueOf((incomes.get(incomes.size() - 1))
                                + Integer.valueOf(budget));
                        setRemainingMoney();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        annualEnvelopes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SetupBudget.this, AddEnvelope.class);
                intent.putExtra("decision", "1");
                intent.putExtra("envelope name", annualEnv.get(position).getName());
                intent.putExtra("budget", annualEnv.get(position).getBudget());
                intent.putExtra("pos", position + "");
                intent.putExtra("spinner value", annualEnv.get(position).getSpinnerValue());
                intent.putExtra("factor", "annual");
                startActivityForResult(intent, 3);
            }
        });

        annualEnvelopes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String name = annualEnv.get(position).getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(SetupBudget.this);
                builder.setTitle("Delete " + annualEnv.get(position).getName() + " envelope");
                builder.setMessage("You are about to delete the " + annualEnv.get(position).getName() + " envelope."
                        + " Are you sure you want to continue ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        annualEnv.remove(position);
                        annualAdapter.notifyDataSetChanged();
                        numAnnualEnvelopes = annualEnv.size();
                        annualEnvelopes.getLayoutParams().height = 200 * numAnnualEnvelopes;
                        annualEnvelopes.setLayoutParams(annualEnvelopes.getLayoutParams());
                        leftOtherEnv.setText(10 - numAnnualEnvelopes + " of 10 free envelopes left");
                        otherEnv.setText("Other (" + numAnnualEnvelopes + ")");
                        Toast.makeText(SetupBudget.this, name +
                                " envelope has been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        remainingBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetupBudget.this);
                if (remainingMoney.getText().toString().split("/")[1].equalsIgnoreCase("m")) {
                    builder.setMessage("You have " + remainingMoney.getText().toString()
                            + " left over from your income every month. Tap 'Add Envelope' to budget more")
                            .setTitle("You have " + remainingMoney.getText().toString() + " left")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
                else
                    if (remainingMoney.getText().toString().split("/")[1].equalsIgnoreCase("w")) {
                        builder.setMessage("You have " + remainingMoney.getText().toString()
                                + " left over from your income every week. Tap 'Add Envelope' to budget more")
                                .setTitle("You have " + remainingMoney.getText().toString() + " left")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    else {
                        builder.setMessage("You have " + remainingMoney.getText().toString()
                                + " left over from your income every 2 weeks. Tap 'Add Envelope' to budget more")
                                .setTitle("You have " + remainingMoney.getText().toString() + " left")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        addEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SetupBudget.this, AddEnvelope.class);
                startActivityForResult(intent1, 2);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetupBudget.this);
                builder.setIcon(R.drawable.logo)
                        .setTitle("Great! Your budget is set.")
                        .setMessage("Now let's put money in your Envelopes. Then you can spend from them")
                        .setNegativeButton("LET ME DO IT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SetupBudget.this, FillEnvelopeManual.class);
                                intent.putExtra("total budget", budgetAmount);
                                transferData(intent);
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("FILL'EM FOR ME", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(SetupBudget.this, FillEnvelopeAuto.class));
                            }
                        });

                builder.create().show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        period = "monthly";
        if (requestCode == REQ_CODE)
            if (resultCode == RESULT_OK) {

                income = data.getStringExtra("income");
                budgetAmount = data.getStringExtra("income");
                totalMoney = Integer.valueOf(income);

                incomes.add(totalMoney);
                System.out.println(incomes.get(incomes.size() - 1));

                if (incomes.size() > 1)
                    income = String.valueOf(incomes.get(incomes.size() - 2) + incomes.get(incomes.size() - 1)
                            - incomes.get(incomes.size() - 2) - sumEnvelopes(envelopes) - sumEnvelopes(annualEnv));

                period = data.getStringExtra("period");
                Toast.makeText(this, "Income: " + incomes.get(incomes.size() - 1), Toast.LENGTH_SHORT).show();
                if (period.equalsIgnoreCase("monthly")) {
                    remainingMoney.setText(income + "/M");
                    setDefaults("remaining money", income + "/M", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("weekly")) {
                    remainingMoney.setText(income + "/W");
                    setDefaults("remaining money", income + "/@", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("twice a week")) {
                    remainingMoney.setText(income + "/2W");
                    setDefaults("remaining money", income + "/2W", SetupBudget.this);
                }

            }
        if (requestCode == 2)
            if (resultCode == RESULT_OK) {
                budget = Integer.valueOf(data.getStringExtra("budget"));
                String envelopeName = data.getStringExtra("envelope");
                String item = data.getStringExtra("spinner position");
                int spinnerValue = data.getIntExtra("spinner value", -1);

                if (item.equalsIgnoreCase("monthly")) {
                    envelopes.add(new Envelope(envelopeName, data.getStringExtra("budget"), spinnerValue));
                    numMonthlyEnv = envelopes.size();
                    adapter.notifyDataSetChanged();
                }
                else {
                    annualEnv.add(new Envelope(envelopeName, data.getStringExtra("budget"), spinnerValue));
                    numAnnualEnvelopes = annualEnv.size();
                    annualAdapter.notifyDataSetChanged();
                }

                income = String.valueOf(Integer.valueOf(income) - budget);
                incomes.add(Integer.valueOf(income));

                if (period.equalsIgnoreCase("monthly")) {
                    remainingMoney.setText(income + "/M");
                    setDefaults("remaining money", income + "/M", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("weekly")) {
                    remainingMoney.setText(income + "/W");
                    setDefaults("remaining money", income + "/@", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("twice a week")) {
                    remainingMoney.setText(income + "/2W");
                    setDefaults("remaining money", income + "/2W", SetupBudget.this);
                }

                envelopesMonth.setText("Monthly (" + numMonthlyEnv + ")");
                otherEnv.setText("Other (" + numAnnualEnvelopes + ")");
                monthLeftEnvelopes.setText(10 - numMonthlyEnv + " of 10 free envelopes left");
                leftOtherEnv.setText(10 - numAnnualEnvelopes + " of 10 free envelopes left");

                monthListHolder.getLayoutParams().height = 200 * numMonthlyEnv;
                monthListHolder.setLayoutParams(monthListHolder.getLayoutParams());
                annualEnvelopes.getLayoutParams().height = 200 * numAnnualEnvelopes;
                annualEnvelopes.setLayoutParams(annualEnvelopes.getLayoutParams());
            }

        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                int pos = 0;
                String updatedName = null;
                String updatedBudget = null;
                String factor = null;


                pos = Integer.valueOf(data.getStringExtra("position"));
                updatedName = data.getStringExtra("envelope");
                updatedBudget = data.getStringExtra("budget");
                factor = data.getStringExtra("factor");


                if (factor.equalsIgnoreCase("monthly")) {
                    income = calculateIncome(pos, updatedBudget, envelopes);
                    incomes.add(Integer.valueOf(income));
                    envelopes.get(pos).setBudget(updatedBudget);
                    envelopes.get(pos).setName(updatedName);
                    adapter.notifyDataSetChanged();
                }
                else {
                    income = calculateIncome(pos, updatedBudget, annualEnv);
                    incomes.add(Integer.valueOf(income));
                    annualEnv.get(pos).setBudget(updatedBudget);
                    annualEnv.get(pos).setName(updatedName);
                    annualAdapter.notifyDataSetChanged();
                }

                if (period.equalsIgnoreCase("monthly")) {
                    remainingMoney.setText(income + "/M");
                    setDefaults("remaining money", income + "/M", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("weekly")) {
                    remainingMoney.setText(income + "/W");
                    setDefaults("remaining money", income + "/W", SetupBudget.this);
                }

                if (period.equalsIgnoreCase("twice a week")) {
                    remainingMoney.setText(income + "/2W");
                    setDefaults("remaining money", income + "/2W", SetupBudget.this);
                }

            }
        }
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setup_budget_help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setup_budget_help_button:
                startActivity(new Intent(SetupBudget.this, SetupBudgetHelp.class));
                return true;
            case R.id.setup_budget_menu:
                Intent intent1 = new Intent(SetupBudget.this, SetBudget.class);
                startActivityForResult(intent1, REQ_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int sumEnvelopes(List<Envelope> envelopes) {
        int sum = 0;
        for (int i = 0; i < envelopes.size(); i++) {
            sum += Integer.valueOf(envelopes.get(i).getBudget());
        }
        return sum;
    }

    private String calculateIncome(int pos, String updatedBudget, List<Envelope> envelopes) {
        if (Integer.valueOf(updatedBudget) < Integer.valueOf(envelopes.get(pos).getBudget()))
            income = String.valueOf(Integer.valueOf(income) + Integer.valueOf(envelopes.get(pos).getBudget())
                    - Integer.valueOf(updatedBudget));
        else
        if (Integer.valueOf(updatedBudget) == Integer.valueOf(envelopes.get(pos).getBudget()))
            income = income;
        else
            if (Integer.valueOf(updatedBudget) > Integer.valueOf(envelopes.get(pos).getBudget()))
                income = String.valueOf(Integer.valueOf(income) -(Integer.valueOf(updatedBudget)
                        - Integer.valueOf(envelopes.get(pos).getBudget())));

        return income;
    }

    private void setRemainingMoney () {
        if (period.equalsIgnoreCase("monthly")) {
            remainingMoney.setText(income + "/M");
            setDefaults("remaining money", income + "/M", SetupBudget.this);
        }

        if (period.equalsIgnoreCase("weekly")) {
            remainingMoney.setText(income + "/W");
            setDefaults("remaining money", income + "/@", SetupBudget.this);
        }

        if (period.equalsIgnoreCase("twice a week")) {
            remainingMoney.setText(income + "/2W");
            setDefaults("remaining money", income + "/2W", SetupBudget.this);
        }
    }

    private void transferData(Intent intent) {
        int sizeM = envelopes.size(), sizeA = annualEnv.size();

        for (int i = 0; i < sizeA; i++) {
            intent.putExtra("annual name " + i, annualEnv.get(i).getName());
            intent.putExtra("annual budget " + i, annualEnv.get(i).getBudget());
        }

        for (int i = 0; i < sizeM; i++) {
            intent.putExtra("month name " + i, envelopes.get(i).getName());
            intent.putExtra("month budget " + i, envelopes.get(i).getBudget());
        }

        intent.putExtra("sizem", sizeM);
        intent.putExtra("sizea", sizeA);
    }
}
