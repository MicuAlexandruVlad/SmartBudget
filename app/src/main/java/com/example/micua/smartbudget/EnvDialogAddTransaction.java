package com.example.micua.smartbudget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EnvDialogAddTransaction extends Dialog{
    private String envName, finalName;
    private int choice, budget;
    private String retractBudget;
    private int ans = 0;
    public EnvDialogAddTransaction(@NonNull Context context) {
        super(context);
    }

    public void setEnvData(String name) {
        this.envName = name;
    }
    public String getRetractedBudget() {
        return retractBudget;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.env_add_transaction);

        retractBudget = "0";

        Button done = (Button) findViewById(R.id.btn_done_add_transaction);
        final CheckBox noActionCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op1_add_transaction);
        final CheckBox specificAmountCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op3_add_transaction);
        final TextView title = (TextView) findViewById(R.id.tv_dialog_title_add_transaction);
        final EditText specificAmountET = (EditText) findViewById(R.id.et_custom_amount_add_transaction);

        specificAmountET.setVisibility(View.GONE);
        noActionCB.setChecked(false);
        specificAmountCB.setChecked(false);
        title.setText(envName);
        budget = getBudget();

        specificAmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int number = Integer.parseInt(charSequence.toString());
                    budget += number;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number;
                if (editable.toString().equals(""))
                    number = 0;
                else
                    number = Integer.parseInt(editable.toString());
                budget -= number;
                title.setText(getName() + " [" + budget + "] left");
            }
        });

        specificAmountCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    specificAmountET.setVisibility(View.VISIBLE);
                    noActionCB.setChecked(false);
                }
                else
                    specificAmountET.setVisibility(View.GONE);
            }
        });

        noActionCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    specificAmountCB.setChecked(false);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificAmountCB.isChecked())
                    retractBudget = specificAmountET.getText().toString();
                if (noActionCB.isChecked())
                    choice = 1;
                if (specificAmountCB.isChecked())
                    choice = 3;
                finalName = title.getText().toString();
                dismiss();
            }
        });
    }
    private int getBudget() {
        int budget = 0;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = pattern.matcher(envName);
        while (m.find()) {
            budget = Integer.valueOf(m.group(1));
        }
        return budget;
    }

    private String getName() {
        return envName.split(" ")[0];
    }

    public String getFinalName() {
        return finalName;
    }
}
