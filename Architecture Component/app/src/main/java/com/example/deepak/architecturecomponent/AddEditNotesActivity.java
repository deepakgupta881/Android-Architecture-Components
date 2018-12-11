package com.example.deepak.architecturecomponent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNotesActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etDesc;
    private NumberPicker numberPickerPriority;
    public static final String EXTRA_TITLE = "com.example.deepak.architecturecomponent.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.example.deepak.architecturecomponent.EXTRA_DESC";
    public static final String EXTRA_PRIORITY = "com.example.deepak.architecturecomponent.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.example.deepak.architecturecomponent.EXTRA_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        numberPickerPriority = findViewById(R.id.number_picker);
        numberPickerPriority.setMaxValue(10);
        numberPickerPriority.setMinValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            etTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            etDesc.setText(intent.getStringExtra(EXTRA_DESC));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else
            setTitle("Add Note");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveNote() {
        if (etTitle.getText().toString().trim().isEmpty() || etDesc.getText().toString().trim().isEmpty()) {
            Toast.makeText(AddEditNotesActivity.this, "Please insert title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, etTitle.getText().toString().trim());
        intent.putExtra(EXTRA_DESC, etDesc.getText().toString().trim());
        intent.putExtra(EXTRA_PRIORITY, numberPickerPriority.getValue());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
