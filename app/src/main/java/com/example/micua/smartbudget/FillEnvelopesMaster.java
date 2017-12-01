package com.example.micua.smartbudget;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FillEnvelopesMaster extends AppCompatActivity {
    private Button fromUnallocated, fromNewIncome;
    private Spinner fillMethod, account;
    private LinearLayout frommNewIncomeHolder, envelopesHolder, fromUnallocatedHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelopes_master);

        String title = "Fill Envelopes";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        fromUnallocated = (Button) findViewById(R.id.btn_from_unallocated_master);
        fromNewIncome = (Button) findViewById(R.id.btn_from_new_income_master);
        fillMethod = (Spinner) findViewById(R.id.spinner_fill_method_master);
        account = (Spinner) findViewById(R.id.spinner_account_master);
        frommNewIncomeHolder = (LinearLayout) findViewById(R.id.ll_from_new_income_holder);
        envelopesHolder = (LinearLayout) findViewById(R.id.ll_envelopes_holder_master);
        fromUnallocatedHolder = (LinearLayout) findViewById(R.id.ll_unallocated_holder_master);

        List<String> choices = new ArrayList<>();
        choices.add("Keep Unallocated");
        choices.add("Fill Each Envelope");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_custom_spinner, choices);

        fillMethod.setAdapter(adapter);
        List<String> accountValue = new ArrayList<>();
        accountValue.add("My Account");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_custom_spinner, accountValue);
        account.setAdapter(adapter1);

        fillMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).equalsIgnoreCase("fill each envelope"))
                    envelopesHolder.setVisibility(View.VISIBLE);
                else
                    envelopesHolder.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromUnallocated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromUnallocated.setBackground(getResources().getDrawable(R.drawable.ripple_color_accent));
                fromNewIncome.setBackground(getResources().getDrawable(R.drawable.ripple_color_gray));
                frommNewIncomeHolder.setVisibility(View.GONE);
                fromUnallocatedHolder.setVisibility(View.VISIBLE);
            }
        });

        fromNewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromNewIncome.setBackground(getResources().getDrawable(R.drawable.ripple_color_accent));
                fromUnallocated.setBackground(getResources().getDrawable(R.drawable.ripple_color_gray));
                frommNewIncomeHolder.setVisibility(View.VISIBLE);
                fromUnallocatedHolder.setVisibility(View.GONE);
            }
        });

    }
}
