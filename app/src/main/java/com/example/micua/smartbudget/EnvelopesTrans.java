package com.example.micua.smartbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EnvelopesTrans extends AppCompatActivity {

    private TextView name, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envelopes_trans);

        Intent intent = getIntent();
        int size = intent.getIntExtra("transSize", 0);
        String nameVal = intent.getStringExtra("name");
        List<String> descr = new ArrayList<>();
        List<Float> budget = new ArrayList<>();
        List<Float> totalBudget = new ArrayList<>();
        List<String> date = new ArrayList<>();

        name = (TextView) findViewById(R.id.tv11_env);
        total = (TextView) findViewById(R.id.tv_total_value_env);
        ListView lv = (ListView) findViewById(R.id.lv_env);
        name.setText(intent.getStringExtra("name"));
        total.setText(intent.getFloatExtra("budget", 0) + "");

        List<Transaction> transactions = new ArrayList<>();
        Toast.makeText(this,intent.getStringExtra(nameVal + " name 1"), Toast.LENGTH_SHORT).show();
        for (int i = 1; i < size; i++) {
            //TODO: fix data not received properly
            descr.add(intent.getStringExtra(nameVal + " name " + i));
            budget.add(intent.getFloatExtra(nameVal + " budgetAdded " + i, 0));
            date.add(intent.getStringExtra(nameVal + " date " + i));
            totalBudget.add(intent.getFloatExtra(nameVal + " total " +i, 0));
        }

        for (int i = 0; i < descr.size(); i++) {
            transactions.add(new Transaction(date.get(i), descr.get(i), "", budget.get(i)));
            transactions.get(i).setTotalValue(totalBudget.get(i));
        }
        AdapterUnallocated adapterUnallocated = new AdapterUnallocated(this, R.layout.list_item_unallocated, transactions);
        lv.setAdapter(adapterUnallocated);
    }
}
