package com.example.deepak.architecturecomponent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener {
    private static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        FloatingActionButton fbAddNotes = findViewById(R.id.btn_add_note);
        RecyclerView recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModel.deleteNotes(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {

                //update our recyclerView
                noteAdapter.setNotesList(notes);
            }
        });
        fbAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNotesActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
        noteAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNotesActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNotesActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddEditNotesActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(priority, title, desc);
            noteViewModel.insertNotes(note);
            Toast.makeText(MainActivity.this, "note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNotesActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(MainActivity.this, "note cant be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNotesActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNotesActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddEditNotesActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(priority, title, desc);
            note.setId(id);
            noteViewModel.updateNotes(note);
            Toast.makeText(MainActivity.this, "note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "note not saved", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddEditNotesActivity.class);
        intent.putExtra(AddEditNotesActivity.EXTRA_TITLE, note.getTitle());
        intent.putExtra(AddEditNotesActivity.EXTRA_DESC, note.getDescription());
        intent.putExtra(AddEditNotesActivity.EXTRA_PRIORITY, note.getPriority());
        intent.putExtra(AddEditNotesActivity.EXTRA_ID, note.getId());
        startActivityForResult(intent, EDIT_NOTE_REQUEST);

    }
}
