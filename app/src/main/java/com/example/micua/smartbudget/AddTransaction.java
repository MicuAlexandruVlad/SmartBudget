package com.example.micua.smartbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddTransaction extends AppCompatActivity {
    private Spinner envelopes, accounts;
    private EditText amount, payee;
    private int amountVal;
    private String payeeVal;
    private ListView envelopesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        envelopes = (Spinner) findViewById(R.id.spinner_envelope_add_transaction);
        accounts = (Spinner) findViewById(R.id.spinner_account_add_transaction);
        amount = (EditText) findViewById(R.id.et_amount_add_transaction);
        payee = (EditText) findViewById(R.id.et_payee_add_transaction);


        List<String> envelopesValues = getData(getIntent());
        List<String> accountsValues = new ArrayList<>();
        accountsValues.add("My Account [" + getIntent().getIntExtra("total amount", 0) + "]");
        ArrayAdapter<String> envelopesAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.tv_custom_spinner, envelopesValues);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.tv_custom_spinner, accountsValues);
        envelopes.setAdapter(envelopesAdapter);
        accounts.setAdapter(accountsAdapter);

        envelopes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
            case R.id.menu_done_add_transaction: endActivity();
            default:return super.onOptionsItemSelected(item);
        }
    }

    private void endActivity() {
        amountVal = Integer.valueOf(amount.getText().toString());
        payeeVal = payee.getText().toString();
        finish();
    }
}
