package com.example.micua.smartbudget;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class EnvelopesFragment extends Fragment{
    private LinearLayout envelopesHolderMonth, envelopesHolderYear;
    private ListView envelopesMonth, envelopesYear;
    private ScrollView parentLayout;
    private List<Envelope> month, year;
    private EnvelopesFinalAdapter envelopesAdapterMonth, envelopesAdapterYear;
    private FloatingActionButton addTransaction;
    private TextView totalExpenseTV, monthExpenseTv, annualExpenseTV, unallocatedAmountTV;
    public static final int ADD_TRANSACTION_ID = 6, FILL_MASTER_REQ_ID = 222;
    private List<Transaction> unallocatedTransactions;
    public static final DateFormat f = new SimpleDateFormat("dd/MM");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        parentLayout = (ScrollView) inflater.inflate(R.layout.fragment_envelopes, container, false);

        addTransaction = (FloatingActionButton) parentLayout.findViewById(R.id.fab_start_add_transaction);
        envelopesHolderMonth = (LinearLayout) parentLayout.findViewById(R.id.ll_frag_month_holder);
        envelopesHolderYear = (LinearLayout) parentLayout.findViewById(R.id.ll_frag_year_holder);
        envelopesMonth = (ListView) parentLayout.findViewById(R.id.lv_frag_month);
        envelopesYear = (ListView) parentLayout.findViewById(R.id.lv_frag_year);
        totalExpenseTV = (TextView) parentLayout.findViewById(R.id.tv_total_expenses_envelopes);
        monthExpenseTv = (TextView) parentLayout.findViewById(R.id.tv_frag_total_cost_month);
        annualExpenseTV = (TextView) parentLayout.findViewById(R.id.tv_frag_total_cost_year);
        unallocatedAmountTV = (TextView) parentLayout.findViewById(R.id.tv_unallocated_frag_envelopes);


        month = new ArrayList<>();
        year = new ArrayList<>();
        unallocatedTransactions = new ArrayList<>();

        Intent extras = getActivity().getIntent();
        getData(month, year, extras);
        totalExpenseTV.setText("All Envelopes: " + computeCosts(month, year)[0]);
        monthExpenseTv.setText(computeCosts(month, year)[1] + "");
        annualExpenseTV.setText(computeCosts(month, year)[2] + "");
        envelopesAdapterMonth = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, month);
        envelopesAdapterYear = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, year);

        envelopesMonth.setAdapter(envelopesAdapterMonth);
        envelopesYear.setAdapter(envelopesAdapterYear);

        unallocatedAmountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UnallocatedTransactions.class);
                sendTransactions(unallocatedTransactions, intent);
                startActivity(intent);
            }
        });

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTransaction.class);
                formatAndTransferData(month, year, intent);
                startActivityForResult(intent, ADD_TRANSACTION_ID);
            }
        });

        return parentLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static Fragment newInstance() {
        return new EnvelopesFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.envelopes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fill_envelopes: Intent intent1 = new Intent(getActivity(), FillEnvelopesMaster.class);
                                           transferData(month, year, intent1);
                                           startActivityForResult(intent1, FILL_MASTER_REQ_ID); return true;
            case R.id.menu_help: startActivity(new Intent(getActivity(), EnvelopesHelp.class)); return true;
            case R.id.menu_edit_envelopes: startActivity(new Intent(getActivity(), SetupBudget.class)); return true;
            case R.id.menu_settings:Intent intent =  new Intent(getActivity(), Settings.class);
                                    intent.putExtra("restarted", false); startActivity(intent); return true;
            default: return super.onOptionsItemSelected(item);
        }
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

    private void formatAndTransferData (List<Envelope> month, List<Envelope> year, Intent intent) {
        int total = month.size() + year.size();
        intent.putExtra("total amount", computeCosts(month, year)[0]);
        intent.putExtra("total size", total);
        int previousVal = 0;
        for (int i = 0; i < month.size(); i++) {
            String value = month.get(i).getName() + " [" + month.get(i).getBudget() + "]";
            intent.putExtra("option envelope " + i, value);
            previousVal = i;
        }
        previousVal++;

        for (int i = 0; i < year.size(); i++) {
            String value = year.get(i).getName() + " [" + year.get(i).getBudget() + "]";
            int index = previousVal + i;
            intent.putExtra("option envelope " + index, value);
        }
    }

    private int[] computeCosts(List<Envelope> month, List<Envelope> year) {
        int total = 0, monthly = 0, annual = 0;
        for (int i = 0; i < month.size(); i++) {
            total += Integer.valueOf(month.get(i).getBudget());
            monthly += Integer.valueOf(month.get(i).getBudget());
        }

        for (int i = 0; i < year.size(); i++) {
            total += Integer.valueOf(year.get(i).getBudget());
            annual += Integer.valueOf(year.get(i).getBudget());
        }
        return new int[]{total, monthly, annual};
    }
    private void updateCostViews() {
        totalExpenseTV.setText(computeCosts(month, year)[0]);
        annualExpenseTV.setText(computeCosts(month, year)[2]);
        monthExpenseTv.setText(computeCosts(month, year)[1]);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILL_MASTER_REQ_ID && resultCode == RESULT_OK) {
            //TODO: take data back from fill envelopes master
            int opID = data.getIntExtra("op", 0);
            if (opID == 1) {
                String receivedFrom = data.getStringExtra("received from");
                float amount = data.getFloatExtra("amount", 0);

                String fillChoice = data.getStringExtra("choice");
                if (fillChoice.equalsIgnoreCase("unallocated")) {
                    unallocatedAmountTV.setText((amount + Float.valueOf(unallocatedAmountTV.getText().toString())) + "");
                    unallocatedTransactions.add(new Transaction(getCurrentDate(), receivedFrom, "My Account", amount));
                }
            }
        }
        if (requestCode == ADD_TRANSACTION_ID && resultCode == RESULT_OK) {
            int size = month.size() + year.size();
            for (int i = 0; i < month.size(); i++) {
                String envName = data.getStringExtra("value " + i).split(" ")[0];
                int changedBudget = getChangedBudget(data.getStringExtra("value " + i));
                if (i < month.size()) {
                    month.remove(i);
                    month.add(i, new Envelope(envName, changedBudget + ""));
                    envelopesAdapterMonth.notifyDataSetChanged();
                }
            }
            int j = 0;
            for (int i = month.size(); i < size; i++) {
                    String envName = data.getStringExtra("value " + i).split(" ")[0];
                    int changedBudget = getChangedBudget(data.getStringExtra("value " + i));
                    year.remove(j);
                    year.add(j, new Envelope(envName, changedBudget + ""));
                    envelopesAdapterYear.notifyDataSetChanged();
                    j++;
                }

        }
        //TODO: Update cost views after returning from AddTransaction
    }

    private int getChangedBudget(String string) {
        int budget = 0;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = pattern.matcher(string);
        while (m.find()) {
            budget = Integer.valueOf(m.group(1));
        }
        return budget;
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

    private String getCurrentDate() {
        Date date = new Date();
        return f.format(date);
    }

    private void sendTransactions(List<Transaction> transactions, Intent intent) {
        intent.putExtra("size", transactions.size());
        for (int i = 0; i < transactions.size(); i++) {
            intent.putExtra("name" + i, transactions.get(i).getName());
            intent.putExtra("date" + i, transactions.get(i).getDate());
            intent.putExtra("account" + i, transactions.get(i).getAccount());
            intent.putExtra("value" + i, transactions.get(i).getValueAdded());
            intent.putExtra("total value" + i, transactions.get(i).getTotalValue());
        }
    }
    //TODO: make function that updates total unallocated value in transactions obj for every transaction added
    private void getCurrentTotal() {}
}
