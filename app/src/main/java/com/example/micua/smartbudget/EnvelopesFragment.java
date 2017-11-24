package com.example.micua.smartbudget;


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

import java.util.ArrayList;
import java.util.List;

public class EnvelopesFragment extends Fragment{
    private LinearLayout envelopesHolderMonth, envelopesHolderYear;
    private ListView envelopesMonth, envelopesYear;
    private ScrollView parentLayout;
    private List<Envelope> month, year;
    private EnvelopesFinalAdapter envelopesAdapterMonth, envelopesAdapterYear;
    private FloatingActionButton addTransaction;
    private TextView totalExpenseTV, monthExpenseTv, annualExpenseTV, unallocatedAmountTV;
    public static final int ADD_TRANSACTION_ID = 6;

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

        Intent extras = getActivity().getIntent();
        getData(month, year, extras);
        totalExpenseTV.setText("All Envelopes: " + computeCosts(month, year)[0]);
        monthExpenseTv.setText(computeCosts(month, year)[1] + "");
        annualExpenseTV.setText(computeCosts(month, year)[2] + "");
        envelopesAdapterMonth = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, month);
        envelopesAdapterYear = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, year);

        envelopesMonth.setAdapter(envelopesAdapterMonth);
        envelopesYear.setAdapter(envelopesAdapterYear);

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
            //TODO case R.id.menu_fill_envelopes:
            case R.id.menu_help: startActivity(new Intent(getActivity(), EnvelopesHelp.class));
            case R.id.menu_edit_envelopes: startActivity(new Intent(getActivity(), SetupBudget.class));
            case R.id.menu_settings:Intent intent =  new Intent(getActivity(), Settings.class);
                                    intent.putExtra("restarted", false); startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
            total += Integer.valueOf(month.get(i).getBudget());
            annual += Integer.valueOf(year.get(i).getBudget());
        }
        return new int[]{total, monthly, annual};
    }
}
