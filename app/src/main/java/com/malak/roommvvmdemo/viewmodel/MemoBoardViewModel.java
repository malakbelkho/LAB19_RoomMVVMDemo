package com.malak.roommvvmdemo.viewmodel;



import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.malak.roommvvmdemo.data.MemoRepository;
import com.malak.roommvvmdemo.data.local.MemoCard;

import java.util.List;

public class MemoBoardViewModel extends AndroidViewModel {

    private final MemoRepository repository;
    private final LiveData<List<MemoCard>> memoList;

    public MemoBoardViewModel(@NonNull Application application) {
        super(application);
        repository = new MemoRepository(application);
        memoList = repository.getMemoStream();
    }

    public void addMemo(MemoCard memoCard) {
        repository.addMemo(memoCard);
    }

    public void removeMemo(MemoCard memoCard) {
        repository.removeMemo(memoCard);
    }

    public void clearBoard() {
        repository.clearBoard();
    }

    public LiveData<List<MemoCard>> getMemoList() {
        return memoList;
    }
}