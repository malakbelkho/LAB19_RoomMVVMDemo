package com.malak.roommvvmdemo.data.local;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemoAccess {

    @Insert
    void addMemo(MemoCard memoCard);

    @Delete
    void removeMemo(MemoCard memoCard);

    @Query("DELETE FROM memo_cards")
    void clearBoard();

    @Query("SELECT * FROM memo_cards ORDER BY created_at DESC, memo_id DESC")
    LiveData<List<MemoCard>> observeMemoBoard();
}