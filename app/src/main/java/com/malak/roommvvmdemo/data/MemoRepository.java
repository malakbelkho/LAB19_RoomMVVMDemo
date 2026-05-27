package com.malak.roommvvmdemo.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.malak.roommvvmdemo.data.local.MemoAccess;
import com.malak.roommvvmdemo.data.local.MemoCard;
import com.malak.roommvvmdemo.data.local.PulseRoomDb;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemoRepository {

    private final MemoAccess memoAccess;
    private final LiveData<List<MemoCard>> memoStream;
    private final ExecutorService diskExecutor;

    public MemoRepository(Application application) {
        PulseRoomDb database = PulseRoomDb.getDatabase(application);
        memoAccess = database.memoAccess();
        memoStream = memoAccess.observeMemoBoard();
        diskExecutor = Executors.newSingleThreadExecutor();
    }

    public void addMemo(MemoCard memoCard) {
        diskExecutor.execute(() -> memoAccess.addMemo(memoCard));
    }

    public void removeMemo(MemoCard memoCard) {
        diskExecutor.execute(() -> memoAccess.removeMemo(memoCard));
    }

    public void clearBoard() {
        diskExecutor.execute(memoAccess::clearBoard);
    }

    public LiveData<List<MemoCard>> getMemoStream() {
        return memoStream;
    }
}