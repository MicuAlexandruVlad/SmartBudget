package com.example.micua.smartbudget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class CustomEnvDialog extends Dialog{
    private String envName;
    private int budget, choice;
    private String updatedBudget;
    public CustomEnvDialog(@NonNull Context context) {
        super(context);
    }

    public void setEnvData(String name, String budget) {
        this.envName = name;
        this.budget = Integer.valueOf(budget);
    }
    public String getNewBudget() {
        return updatedBudget;
    }

    public int getChoice() {
        return choice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.envelope_budget_dialog);

        updatedBudget = "0";

        Button done = (Button) findViewById(R.id.btn_done);
        final CheckBox noActionCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op1);
        final CheckBox envelopeAmountCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op2);
        final CheckBox specificAmountCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op3);
        TextView budgetAmountTV = (TextView) findViewById(R.id.tv_set_to_budget_amount);
        TextView title = (TextView) findViewById(R.id.tv_dialog_title);
        final EditText specificAmountET = (EditText) findViewById(R.id.et_custom_amount);

        specificAmountET.setVisibility(View.GONE);
        noActionCB.setChecked(false);
        envelopeAmountCB.setChecked(false);
        specificAmountCB.setChecked(false);
        title.setText(envName);
        budgetAmountTV.setText("Set to Budget Amount (" + budget + ")");

        specificAmountCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    specificAmountET.setVisibility(View.VISIBLE);
                    noActionCB.setChecked(false);
                    envelopeAmountCB.setChecked(false);
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
                    envelopeAmountCB.setChecked(false);

                }
            }
        });

        envelopeAmountCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    noActionCB.setChecked(false);
                    specificAmountCB.setChecked(false);

                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificAmountCB.isChecked())
                    updatedBudget = specificAmountET.getText().toString();
                if (noActionCB.isChecked())
                    choice = 1;
                if (envelopeAmountCB.isChecked())
                    choice = 2;
                if (specificAmountCB.isChecked())
                    choice = 3;
                dismiss();
            }
        });
    }


}
