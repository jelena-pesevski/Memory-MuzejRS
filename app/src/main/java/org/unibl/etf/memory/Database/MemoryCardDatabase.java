package org.unibl.etf.memory.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.unibl.etf.memory.Constants;

@Database(entities = { MemoryCard.class }, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class MemoryCardDatabase extends RoomDatabase{


    private static MemoryCardDatabase memoryCardDB;

    public abstract MemoryCardDao getMemoryCardDao();

    public static MemoryCardDatabase getInstance(Context context)
    {
        if(null == memoryCardDB)
        {
            synchronized (MemoryCardDatabase.class)
            {
                if(null == memoryCardDB)
                {

                    memoryCardDB=Room.databaseBuilder(context.getApplicationContext(),MemoryCardDatabase.class,Constants.DB_NAME).createFromAsset("database/DataBase.db").build();

                }
            }

        }
        return memoryCardDB;

    }
    private static MemoryCardDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                MemoryCardDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        memoryCardDB = null;
    }


}
