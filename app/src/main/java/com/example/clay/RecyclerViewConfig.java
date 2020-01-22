package com.example.clay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewConfig {
    private Context mContext;
    private VerseAdapter mVerseAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Verse> verseList) {
        mContext = context;
        mVerseAdapter = new VerseAdapter(verseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mVerseAdapter);
    }

    /**
     * Verse item view holder
     */
    class VerseItemView extends RecyclerView.ViewHolder {
        private TextView mVerseId;
        private TextView mVerseContent;

        public VerseItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.verse_list_item, parent, false));
            mVerseId = itemView.findViewById(R.id.text_view_verse_id);
            mVerseContent = itemView.findViewById(R.id.text_view_verse_content);
        }

        public void bind(Verse verse) {
            mVerseId.setText(String.valueOf(verse.getVerse()));
            mVerseContent.setText(verse.getContent());
        }
    }

    /**
     * Verse adapter
     */
    class VerseAdapter extends RecyclerView.Adapter<VerseItemView> {
        private List<Verse> mVerseList;

        public VerseAdapter(List<Verse> verseList) {
            this.mVerseList = verseList;
        }


        @NonNull
        @Override
        public VerseItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VerseItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull VerseItemView holder, int position) {
            holder.bind(mVerseList.get(position));
        }

        @Override
        public int getItemCount() {
            return mVerseList.size();
        }
    }
}
