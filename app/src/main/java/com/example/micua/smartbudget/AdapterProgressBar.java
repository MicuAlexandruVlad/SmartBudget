package com.example.micua.smartbudget;


import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class AdapterProgressBar extends ArrayAdapter<Envelope> {

    public AdapterProgressBar(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterProgressBar(Context context, int resource, List<Envelope> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_progress_bar, null);
        }

        Envelope e = getItem(position);

        if (e != null) {
            TextView envelopeName = (TextView) v.findViewById(R.id.tv_name_envelope);
            TextView envelopeBudget = (TextView) v.findViewById(R.id.tv_budget_for_envelope);
            TextView description = (TextView) v.findViewById(R.id.tv_choice_description);
            ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb_progress);
            pb.setMax(Integer.valueOf(e.getBudget()));
            pb.setProgress(0);

            if (envelopeBudget != null) {
                envelopeBudget.setText(e.getBudget());
            }

            if (envelopeName != null) {
                envelopeName.setText(e.getName());
            }

            if (e.getChoice() == 1) {
                description.setText("No Action");
                pb.setProgress(0);
            }
            else
                if (e.getChoice() == 2) {
                    description.setText("Set to Budget Amount of " + e.getBudget());
                    pb.setProgress(pb.getMax());
                }
                else
                    if (e.getChoice() == 3) {
                        description.setText("Set to Specific Amount of " + e.getUpdatedBudget());
                        pb.setProgress(e.getUpdatedBudget());
                    }
        }

        return v;
    }


}
