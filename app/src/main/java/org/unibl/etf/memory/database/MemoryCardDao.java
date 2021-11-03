package org.unibl.etf.memory.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.memory.utils.Constants;

import java.util.List;

@Dao
public interface MemoryCardDao {

    @Query("SELECT * FROM " + Constants.Table_Name)
    List<MemoryCard> getMemoryCard();

    @Insert
    long insertMemoryCard(MemoryCard memoryCard);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void updateMemoryCard(MemoryCard memoryCard);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void deleteMemoryCard(MemoryCard memoryCard);

    // Note... is varargs, here note is an array
    /*
     * delete list of objects from database
     * @param note, array of oject to be deleted
     */
    @Delete
    void deleteMemoryCards(MemoryCard memoryCard);






}
