package com.example.micua.smartbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UnallocatedTransactions extends AppCompatActivity {
    private float totalUnallocated = 0;
    private TextView totalUnallocatedTV;
    private ListView unallocatedTransactions;
    private AdapterUnallocated adapterUnallocated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unallocated_transactions);

        totalUnallocatedTV = (TextView) findViewById(R.id.tv_total_value_unallocated);
        unallocatedTransactions = (ListView) findViewById(R.id.lv_unallocated);


        Intent intent = getIntent();
        List<Transaction> transactions = new ArrayList<>();
        getTransactions(transactions, intent);
        adapterUnallocated = new AdapterUnallocated(this, R.layout.list_item_unallocated, transactions);
        unallocatedTransactions.setAdapter(adapterUnallocated);
        totalUnallocatedTV.setText(totalUnallocated + "");



    }

    private void getTransactions(List<Transaction> transactions, Intent intent) {
        int size = intent.getIntExtra("size", 0);
        for (int i = 0; i < size; i++) {
            String date = intent.getStringExtra("date" + i);
            String name = intent.getStringExtra("name" + i);
            String account = intent.getStringExtra("account" + i);
            float value = intent.getFloatExtra("value" + i, 0);
            totalUnallocated += value;
            float totalValue = intent.getFloatExtra("total value" + i, 0);
            transactions.add(new Transaction(date, name, account, value));
            transactions.get(i).setTotalValue(totalValue);
        }
    }
}
