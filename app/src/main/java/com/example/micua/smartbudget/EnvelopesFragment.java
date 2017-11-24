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

import java.util.ArrayList;
import java.util.List;

public class EnvelopesFragment extends Fragment{
    private LinearLayout envelopesHolderMonth, envelopesHolderYear;
    private ListView envelopesMonth, envelopesYear;
    private ScrollView parentLayout;
    private List<Envelope> month, year;
    private EnvelopesFinalAdapter envelopesAdapterMonth, envelopesAdapterYear;
    private FloatingActionButton addTransaction;

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

        month = new ArrayList<>();
        year = new ArrayList<>();

        Intent extras = getActivity().getIntent();
        getData(month, year, extras);
        envelopesAdapterMonth = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, month);
        envelopesAdapterYear = new EnvelopesFinalAdapter(getActivity(), R.layout.list_item_envelope_final, year);

        envelopesMonth.setAdapter(envelopesAdapterMonth);
        envelopesYear.setAdapter(envelopesAdapterYear);

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
}
