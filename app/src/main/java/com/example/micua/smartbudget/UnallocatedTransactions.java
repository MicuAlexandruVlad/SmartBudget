package com.example.micua.smartbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private LinearLayout holder;
    public static final int ADDED_TO_UNALLOCATED_ID = 123;
    private List<Transaction> transactions;
    private Intent returnIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unallocated_transactions);

        totalUnallocatedTV = (TextView) findViewById(R.id.tv_total_value_unallocated);
        unallocatedTransactions = (ListView) findViewById(R.id.lv_unallocated);
        holder = (LinearLayout) findViewById(R.id.ll_unallocated_holder_trans);

        returnIntent = getIntent();
        transactions = new ArrayList<>();
        getTransactions(transactions, returnIntent);
        generateTotalBudget(transactions);

        adapterUnallocated = new AdapterUnallocated(this, R.layout.list_item_unallocated, transactions);
        unallocatedTransactions.setAdapter(adapterUnallocated);
        totalUnallocatedTV.setText(totalUnallocated + "");

        holder.getLayoutParams().height = 270 * transactions.size();
        holder.setLayoutParams(holder.getLayoutParams());

        unallocatedTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(UnallocatedTransactions.this, AddedToUnallocated.class);
                Transaction currentTrans = transactions.get(i);
                intent1.putExtra("name", currentTrans.getName());
                intent1.putExtra("amount", currentTrans.getValueAdded());
                intent1.putExtra("date", currentTrans.getDate());
                intent1.putExtra("account", currentTrans.getAccount());
                intent1.putExtra("note", currentTrans.getNote());
                intent1.putExtra("pos", i);
                startActivityForResult(intent1, ADDED_TO_UNALLOCATED_ID);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDED_TO_UNALLOCATED_ID && resultCode == RESULT_OK) {
            boolean delete = data.getBooleanExtra("delete", false);
            boolean changed = data.getBooleanExtra("changed", false);
            int pos = data.getIntExtra("pos", -1);
            if (delete) {
                transactions.remove(pos);
                generateTotalBudget(transactions);
                adapterUnallocated.notifyDataSetChanged();
            }
            if (changed) {
                transactions.get(pos).setNote(data.getStringExtra("note"));
                transactions.get(pos).setValueAdded(data.getFloatExtra("amount", 0));
                transactions.get(pos).setAccount(data.getStringExtra("account"));
                transactions.get(pos).setDate(data.getStringExtra("date"));
                transactions.get(pos).setName(data.getStringExtra("name"));
                generateTotalBudget(transactions);
                adapterUnallocated.notifyDataSetChanged();
            }
        }
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
            transactions.get(i).setNote(intent.getStringExtra("note" + i));
        }
    }

    private void generateTotalBudget(List<Transaction> transactions) {
        float budget = 0;
        for (int i = 0; i < transactions.size(); i++) {
            budget += transactions.get(i).getValueAdded();
            transactions.get(i).setTotalValue(budget);
        }
        totalUnallocatedTV.setText(budget + "");
    }
    //TODO: update total budget after transaction deletion or on budget changed

    @Override
    public void onBackPressed() {
        sendDataBack(transactions, returnIntent);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void sendDataBack(List<Transaction> transactions, Intent intent) {
        intent.putExtra("size", transactions.size());
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            intent.putExtra("name" + i, transaction.getName());
            intent.putExtra("date" + i, transaction.getDate());
            intent.putExtra("note" + i, transaction.getNote());
            intent.putExtra("amount" + i, transaction.getValueAdded());
            intent.putExtra("account" + i, transaction.getAccount());
        }
    }
}
