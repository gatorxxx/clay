package com.example.clay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteRecyclerViewConfig {
    private Context mContext;
    private NoteAdapter mNoteAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Note> noteList) {
        mContext = context;
        mNoteAdapter = new NoteAdapter(noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mNoteAdapter);
    }

    class NoteItemView extends RecyclerView.ViewHolder {
        private TextView mNoteTitle;
        private TextView mNoteContent;
        private TextView mNoteDate;

        public NoteItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.note_list_item, parent, false));
            mNoteTitle = itemView.findViewById(R.id.text_view_main_note_title);
            mNoteContent = itemView.findViewById(R.id.text_view_main_note_content);
            mNoteDate = itemView.findViewById(R.id.text_view_main_note_date);
        }

        public void bind(Note note) {
            mNoteTitle.setText(note.getTitle());
            mNoteContent.setText(note.getContent().toString());
            String currentDate = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(note.getDateCreated());
            mNoteDate.setText(currentDate);
        }
    }

    class NoteAdapter extends RecyclerView.Adapter<NoteItemView> {
        private List<Note> mNoteList;

        public NoteAdapter(List<Note> noteList) {
            this.mNoteList = noteList;
        }

        @NonNull
        @Override
        public NoteItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoteItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteItemView holder, int position) {
            holder.bind(mNoteList.get(position));
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
    }
}
