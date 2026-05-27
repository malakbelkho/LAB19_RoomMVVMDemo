package com.malak.roommvvmdemo.data.local;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memo_cards")
public class MemoCard {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    private int memoId;

    @ColumnInfo(name = "headline")
    private String headline;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public MemoCard(String headline, String content, long createdAt) {
        this.headline = headline;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public String getHeadline() {
        return headline;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}