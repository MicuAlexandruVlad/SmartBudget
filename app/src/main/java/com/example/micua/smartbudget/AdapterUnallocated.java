package com.example.micua.smartbudget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class AdapterUnallocated extends ArrayAdapter<Transaction> {

    public AdapterUnallocated(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterUnallocated(Context context, int resource, List<Transaction> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_unallocated, null);
        }

        Transaction e = getItem(position);

        if (e != null) {
            TextView transactionName = (TextView) v.findViewById(R.id.tv_env_name_unallocated);
            TextView transactionVal = (TextView) v.findViewById(R.id.tv_env_added_val_unallocated);
            TextView transactionTotalVal = (TextView) v.findViewById(R.id.tv_total_val_unallocated);
            TextView transactionDate = (TextView) v.findViewById(R.id.tv_date_unallocated);
            TextView transactionAccount = (TextView) v.findViewById(R.id.tv_account_unallocated);


            if (transactionName != null) {
                transactionName.setText(e.getName());
            }
            if (transactionVal != null) {
                transactionVal.setText(e.getValueAdded() + "");
            }
            if (transactionTotalVal != null) {
                transactionTotalVal.setText(e.getTotalValue() + "");
            }
            if (transactionDate != null) {
                transactionDate.setText(e.getDate());
            }
            if (transactionAccount != null) {
                transactionAccount.setText(e.getAccount());
            }

        }

        return v;
    }


}
