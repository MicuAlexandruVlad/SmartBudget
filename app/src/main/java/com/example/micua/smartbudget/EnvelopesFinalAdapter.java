package com.example.micua.smartbudget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class EnvelopesFinalAdapter extends ArrayAdapter<Envelope> {
    public EnvelopesFinalAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public EnvelopesFinalAdapter(Context context, int resource, List<Envelope> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_envelope_final, null);
        }

        Envelope e = getItem(position);

        if (e != null) {
            TextView envelopeName = (TextView) v.findViewById(R.id.tv_name_envelope_final);
            TextView envelopeTotalBudget = (TextView) v.findViewById(R.id.tv_total_budget_for_envelope_final);
            ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb_progress_final);
            TextView envelopeBudget = (TextView) v.findViewById(R.id.tv_budget_for_envelope_final);
            pb.setMax(Integer.valueOf(e.getBudget()));
            pb.setProgress(0);

            /*if (e.getChoice() == 1) {
                pb.setProgress(0);
            }
            else
            if (e.getChoice() == 2) {
                pb.setProgress(pb.getMax());
            }
            else
            if (e.getChoice() == 3) {
                pb.setProgress(e.getUpdatedBudget());
            }*/


            if (envelopeBudget != null) {
                envelopeBudget.setText(e.getBudget());
            }

            if (envelopeName != null) {
                envelopeName.setText(e.getName());
            }

            if (envelopeTotalBudget != null) {
                envelopeTotalBudget.setText(e.getBudget());
            }
        }

        return v;
    }
}
