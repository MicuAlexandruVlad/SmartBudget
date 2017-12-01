package com.example.micua.smartbudget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTransaction extends AppCompatActivity {
    //TODO: Check if the total retracted amount is not bigger than the amountVal - DONE
    private Spinner envelopes, accounts;
    private EditText amount, payee;
    private int amountVal;
    private String payeeVal;
    private Intent returnIntent;
    private List<String> envelopesValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        returnIntent = getIntent();

        envelopes = (Spinner) findViewById(R.id.spinner_envelope_add_transaction);
        accounts = (Spinner) findViewById(R.id.spinner_account_add_transaction);
        amount = (EditText) findViewById(R.id.et_amount_add_transaction);
        payee = (EditText) findViewById(R.id.et_payee_add_transaction);


        envelopesValues = new ArrayList<>();
        envelopesValues.add("Select Envelope");
        envelopesValues.addAll(getData(getIntent()));
        List<String> accountsValues = new ArrayList<>();
        accountsValues.add("My Account [" + getIntent().getIntExtra("total amount", 0) + "]");
        final ArrayAdapter<String> envelopesAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.tv_custom_spinner, envelopesValues);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.tv_custom_spinner, accountsValues);
        envelopes.setAdapter(envelopesAdapter);
        accounts.setAdapter(accountsAdapter);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                amountVal = Integer.valueOf(amount.getText().toString());
            }
        });

        envelopes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final EnvDialogAddTransaction dialog = new EnvDialogAddTransaction(AddTransaction.this);
                final int auxPosition = i;
                if (i != 0) {
                    dialog.setEnvData(envelopesValues.get(i));
                    dialog.create();
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String title = dialog.getFinalName();
                            int retractedValue = Integer.valueOf(dialog.getRetractedBudget());
                            envelopesValues.remove(auxPosition);
                            envelopesAdapter.insert(title, auxPosition);
                            envelopesAdapter.notifyDataSetChanged();
                            int val = amountVal - retractedValue;
                            if (retractedValue < amountVal)
                                Toast.makeText(AddTransaction.this, "You have " + val + " to split among " +
                                        "envelopes", Toast.LENGTH_SHORT).show();
                            if (retractedValue > amountVal)
                                Toast.makeText(AddTransaction.this, "The inserted value is " +
                                        "higher than how much you spent", Toast.LENGTH_SHORT).show();
                            amountVal -= retractedValue;
                            if (val == 0)
                                Toast.makeText(AddTransaction.this, "Well Done !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<String> getData(Intent data) {
        int totalSize = data.getIntExtra("total size", 0);
        List<String> values = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            values.add(data.getStringExtra("option envelope " + i) + " left");
        }
        return values;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help_add_transaction: startActivity(new Intent(AddTransaction.this, HelpAddTransaction.class));
            case R.id.menu_done_add_transaction: endActivity(envelopesValues);
            default:return super.onOptionsItemSelected(item);
        }
    }

    private void endActivity(List<String> values) {
        if (!amount.getText().toString().equalsIgnoreCase("")) {
            amountVal = Integer.valueOf(amount.getText().toString());
            payeeVal = payee.getText().toString();
            transferData(returnIntent, values);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else
            Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
    }

    private void transferData(Intent intent, List<String> values) {
        values.remove(0);
        for (int i = 0; i < values.size(); i++) {
            intent.putExtra("value " + i, values.get(i));
        }
    }
}
