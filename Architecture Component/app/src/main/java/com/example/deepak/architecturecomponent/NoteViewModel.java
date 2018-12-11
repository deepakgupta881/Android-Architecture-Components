package com.example.deepak.architecturecomponent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<Note>> listAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        listAllNotes = noteRepository.getAllNotes();
    }

    public void insertNotes(Note note) {
        noteRepository.insert(note);
    }

    public void updateNotes(Note note) {
        noteRepository.update(note);
    }

    public void deleteNotes(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAllNotes() {
        noteRepository.deleteAll();
    }

    public LiveData<List<Note>> getAllNotes() {
        return listAllNotes;
    }

}
