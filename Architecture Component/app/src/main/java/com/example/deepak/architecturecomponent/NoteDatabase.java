package com.example.deepak.architecturecomponent;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase noteDatabaseInstance;
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getNoteDatabaseInstance(Context context) {

        if (noteDatabaseInstance == null) {
            noteDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class,
                    "note_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return noteDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(noteDatabaseInstance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        public PopulateAsyncTask(NoteDatabase noteDatabase) {
            this.noteDao = noteDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note(1, "A", "Apple"));
            noteDao.insert(new Note(2, "B", "Banana"));
            noteDao.insert(new Note(3, "C", "Cow"));
            return null;
        }

    }

}
