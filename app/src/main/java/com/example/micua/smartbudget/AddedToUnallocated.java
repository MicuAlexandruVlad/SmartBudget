package com.example.micua.smartbudget;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class AddedToUnallocated extends AppCompatActivity {
    private EditText name, account, amount, date, note;
    private Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_to_unallocated);

        returnIntent = getIntent();

        name = (EditText) findViewById(R.id.et_transaction_name_added);
        amount = (EditText) findViewById(R.id.et_transaction_amount_added);
        account = (EditText) findViewById(R.id.et_transaction_account_added);
        date = (EditText) findViewById(R.id.et_transaction_date_added);
        note = (EditText) findViewById(R.id.et_transaction_note_added);

        name.setText(returnIntent.getStringExtra("name"));
        amount.setText(returnIntent.getFloatExtra("amount", 0) + "");
        account.setText(returnIntent.getStringExtra("account"));
        date.setText(returnIntent.getStringExtra("date"));
        note.setText(returnIntent.getStringExtra("note"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_unallocated_transactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_done_added) {
            if (isChanged()) {
                returnIntent.putExtra("pos", returnIntent.getIntExtra("pos", 0));
                returnIntent.putExtra("delete", false);
                returnIntent.putExtra("changed", true);
                returnIntent.putExtra("name", name.getText().toString());
                returnIntent.putExtra("date", date.getText().toString());
                returnIntent.putExtra("amount", Float.valueOf(amount.getText().toString()));
                returnIntent.putExtra("note", note.getText().toString());
                returnIntent.putExtra("account", account.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            else {
                returnIntent.putExtra("changed", false);
                returnIntent.putExtra("delete", false);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            return true;
        }

        if (item.getItemId() == R.id.btn_bin_added) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddedToUnallocated.this);
            builder.setTitle(R.string.delete_diaog_added)
                    .setIcon(R.drawable.warning)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            returnIntent.putExtra("delete", true);
                            returnIntent.putExtra("pos", returnIntent.getIntExtra("pos", 0));
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    });
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isChanged() {
        if (name.getText().toString().equals(returnIntent.getStringExtra("name")) &&
                Float.valueOf(amount.getText().toString()) == returnIntent.getFloatExtra("amount", 0) &&
                date.getText().toString().equals(returnIntent.getStringExtra("date")) &&
                account.getText().toString().equals(returnIntent.getStringExtra("account")) &&
                note.getText().toString().equals(returnIntent.getStringExtra("note")))
            return false;
        return true;
    }
}
