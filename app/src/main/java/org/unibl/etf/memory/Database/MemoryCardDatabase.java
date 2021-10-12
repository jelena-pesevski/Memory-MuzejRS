package org.unibl.etf.memory.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.unibl.etf.memory.MainActivity;

@Database(entities = { MemoryCard.class }, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class MemoryCardDatabase extends RoomDatabase{


    private static MemoryCardDatabase memoryCardDB;

    public abstract MemoryCardDao getMemoryCardDao();

    public static MemoryCardDatabase getInstance(Context context)
    {
        if(null == memoryCardDB)
        {
            memoryCardDB =buildDatabaseInstance(context);

        }
        return memoryCardDB;

    }
    private static MemoryCardDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                MemoryCardDatabase.class,
                MainActivity.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        memoryCardDB = null;
    }

}
