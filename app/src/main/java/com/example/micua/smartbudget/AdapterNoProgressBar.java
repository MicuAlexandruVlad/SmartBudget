package com.example.micua.smartbudget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class AdapterNoProgressBar extends ArrayAdapter<Envelope> {

    public AdapterNoProgressBar(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterNoProgressBar(Context context, int resource, List<Envelope> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_no_bar, null);
        }

        Envelope e = getItem(position);

        if (e != null) {
            TextView envelopeName = (TextView) v.findViewById(R.id.tv_name_envelope);
            TextView envelopeBudget = (TextView) v.findViewById(R.id.tv_budget_for_envelope);
            TextView description = (TextView) v.findViewById(R.id.tv_choice_description);

            if (envelopeBudget != null) {
                envelopeBudget.setText(e.getBudget());
            }

            if (envelopeName != null) {
                envelopeName.setText(e.getName());
            }

            if (e.getChoice() == 1) {
                description.setText("No Change");
            }
            else
                if (e.getChoice() == 2)
                    description.setText("Add Specific Amount:");
                else
                    if (e.getChoice() == 3)
                        description.setText("Set to Specific Amount:");
        }

        return v;
    }


}
