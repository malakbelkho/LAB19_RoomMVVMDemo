package com.malak.roommvvmdemo.data.local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MemoCard.class}, version = 1, exportSchema = false)
public abstract class PulseRoomDb extends RoomDatabase {

    private static volatile PulseRoomDb INSTANCE;

    public abstract MemoAccess memoAccess();

    public static PulseRoomDb getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (PulseRoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PulseRoomDb.class,
                                    "memo_pulse_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
