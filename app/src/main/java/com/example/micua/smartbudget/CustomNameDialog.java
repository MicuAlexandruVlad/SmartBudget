package com.example.micua.smartbudget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomNameDialog extends Dialog{
    private Button cancel, ok;
    private EditText name;
    private String nameText = "";

    public CustomNameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_name_dialog);

        name = (EditText) findViewById(R.id.et_custom_dialog_device_name);
        cancel = (Button) findViewById(R.id.btn_custom_dialog_cancel);
        ok = (Button) findViewById(R.id.btn_custom_dialog_ok);

        //name.setText(nameText);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = name.getText().toString();
                dismiss();
            }
        });
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
        name.setText(nameText);
    }
}
