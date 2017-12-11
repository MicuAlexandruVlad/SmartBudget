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

public class CustomEnvDialogMaster extends Dialog{
    private String envName;
    private int budget, choice;
    private String updatedBudget;
    public CustomEnvDialogMaster(@NonNull Context context) {
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
        setContentView(R.layout.envelope_master_dialog);

        updatedBudget = "0";

        Button done = (Button) findViewById(R.id.btn_done_master);
        final CheckBox noActionCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op1_master);
        final CheckBox addToBudgetCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op2_master);
        final CheckBox setToBudgetCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op3_master);
        final CheckBox addSpecificCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op4_master);
        final CheckBox setSpecificCB = (CheckBox) findViewById(R.id.cb_envelope_budget_op5_master);
        TextView addbudgetAmountTV = (TextView) findViewById(R.id.tv_add_budget_amount_master);
        TextView setbudgetAmountTV = (TextView) findViewById(R.id.tv_set_to_amount_master);
        TextView title = (TextView) findViewById(R.id.tv_dialog_title_master);
        final EditText specificAmountET = (EditText) findViewById(R.id.et_custom_amount_master);

        specificAmountET.setVisibility(View.GONE);
        noActionCB.setChecked(false);
        addToBudgetCB.setChecked(false);
        setToBudgetCB.setChecked(false);
        addSpecificCB.setChecked(false);
        setSpecificCB.setChecked(false);
        title.setText(envName);
        //budgetAmountTV.setText("Set to Budget Amount (" + budget + ")");

        addSpecificCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    specificAmountET.setVisibility(View.VISIBLE);
                    noActionCB.setChecked(false);
                    addToBudgetCB.setChecked(false);
                    setToBudgetCB.setChecked(false);
                    setSpecificCB.setChecked(false);
                }
                else
                    specificAmountET.setVisibility(View.GONE);
            }
        });

        setSpecificCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    specificAmountET.setVisibility(View.VISIBLE);
                    noActionCB.setChecked(false);
                    addToBudgetCB.setChecked(false);
                    setToBudgetCB.setChecked(false);
                    addSpecificCB.setChecked(false);
                }
                else
                    specificAmountET.setVisibility(View.GONE);
            }
        });

        noActionCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addToBudgetCB.setChecked(false);
                    setToBudgetCB.setChecked(false);
                    setSpecificCB.setChecked(false);
                    addSpecificCB.setChecked(false);
                }
            }
        });

        addToBudgetCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    noActionCB.setChecked(false);
                    addSpecificCB.setChecked(false);
                    setSpecificCB.setChecked(false);
                    setToBudgetCB.setChecked(false);
                }
            }
        });

        setToBudgetCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    noActionCB.setChecked(false);
                    addSpecificCB.setChecked(false);
                    setSpecificCB.setChecked(false);
                    addToBudgetCB.setChecked(false);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addSpecificCB.isChecked())
                    updatedBudget = specificAmountET.getText().toString();
                if (noActionCB.isChecked())
                    choice = 1;
                if (addToBudgetCB.isChecked())
                    choice = 2;
                if (setToBudgetCB.isChecked())
                    choice = 3;
                if (addSpecificCB.isChecked())
                    choice = 4;
                if (setSpecificCB.isChecked())
                    choice = 5;
                dismiss();
            }
        });
    }


}
