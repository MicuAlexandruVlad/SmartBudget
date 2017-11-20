package com.example.micua.smartbudget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Envelope>{

    public CustomListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomListAdapter(Context context, int resource, List<Envelope> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        Envelope e = getItem(position);

        if (e != null) {
            TextView envelopeName = (TextView) v.findViewById(R.id.tv_name_envelope);
            TextView envelopeBudget = (TextView) v.findViewById(R.id.tv_budget_for_envelope);


            if (envelopeBudget != null) {
                envelopeBudget.setText(e.getBudget());
            }

            if (envelopeName != null) {
                envelopeName.setText(e.getName());
            }
        }

        return v;
    }
}
