package com.malak.roommvvmdemo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.malak.roommvvmdemo.R;
import com.malak.roommvvmdemo.data.local.MemoCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemoCardAdapter extends RecyclerView.Adapter<MemoCardAdapter.MemoHolder> {

    private final List<MemoCard> memoCards = new ArrayList<>();
    private OnMemoActionListener actionListener;

    public interface OnMemoActionListener {
        void onMemoClicked(MemoCard memoCard);
        void onMemoLongPressed(MemoCard memoCard);
    }

    public void setOnMemoActionListener(OnMemoActionListener listener) {
        this.actionListener = listener;
    }

    public void submitMemos(List<MemoCard> freshList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return memoCards.size();
            }

            @Override
            public int getNewListSize() {
                return freshList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return memoCards.get(oldItemPosition).getMemoId()
                        == freshList.get(newItemPosition).getMemoId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                MemoCard oldMemo = memoCards.get(oldItemPosition);
                MemoCard newMemo = freshList.get(newItemPosition);

                return oldMemo.getHeadline().equals(newMemo.getHeadline())
                        && oldMemo.getContent().equals(newMemo.getContent())
                        && oldMemo.getCreatedAt() == newMemo.getCreatedAt();
            }
        });

        memoCards.clear();
        memoCards.addAll(freshList);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_card_item, parent, false);
        return new MemoHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHolder holder, int position) {
        MemoCard currentMemo = memoCards.get(position);

        holder.titleView.setText(currentMemo.getHeadline());
        holder.bodyView.setText(currentMemo.getContent());
        holder.dateView.setText(formatDate(currentMemo.getCreatedAt()));

        String title = currentMemo.getHeadline().trim();
        String initial = title.isEmpty()
                ? "M"
                : title.substring(0, 1).toUpperCase(Locale.getDefault());

        holder.initialView.setText(initial);
    }

    @Override
    public int getItemCount() {
        return memoCards.size();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault());
        return formatter.format(new Date(timestamp));
    }

    class MemoHolder extends RecyclerView.ViewHolder {

        private final TextView initialView;
        private final TextView titleView;
        private final TextView bodyView;
        private final TextView dateView;

        public MemoHolder(@NonNull View itemView) {
            super(itemView);

            initialView = itemView.findViewById(R.id.textInitialBadge);
            titleView = itemView.findViewById(R.id.textMemoTitle);
            bodyView = itemView.findViewById(R.id.textMemoBody);
            dateView = itemView.findViewById(R.id.textMemoDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (actionListener != null && position != RecyclerView.NO_POSITION) {
                    actionListener.onMemoClicked(memoCards.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (actionListener != null && position != RecyclerView.NO_POSITION) {
                    actionListener.onMemoLongPressed(memoCards.get(position));
                    return true;
                }
                return false;
            });
        }
    }
}