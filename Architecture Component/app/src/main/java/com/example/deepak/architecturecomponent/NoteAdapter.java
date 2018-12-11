package com.example.deepak.architecturecomponent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notesList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        Note note = notesList.get(i);
        noteViewHolder.txtPriority.setText(String.valueOf(note.getPriority()));
        noteViewHolder.txtDescription.setText(note.getDescription());
        noteViewHolder.txtTitle.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int pos) {
        return notesList.get(pos);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtPriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.tv_desc);
            txtPriority = itemView.findViewById(R.id.tv_priority);
            txtTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemClick(notesList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
