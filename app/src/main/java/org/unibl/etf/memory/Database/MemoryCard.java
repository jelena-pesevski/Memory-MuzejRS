package org.unibl.etf.memory.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.unibl.etf.memory.Constants;

import java.io.Serializable;


@Entity(tableName = Constants.Table_Name)
public class MemoryCard implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int memoryCard_id;

    @ColumnInfo(name = "memoryCard_name")
    private String name;

    private String path;

    private String description;

    public MemoryCard(int memoryCard_id, String name, String path, String description)
    {

        this.memoryCard_id = memoryCard_id;
        this.name=name;
        this.path=path;
        this.description=description;

    }

    @Ignore
    public MemoryCard()
    {

    }

    public int getMemoryCard_id() {
        return memoryCard_id;
    }

    public void setMemoryCard_id(int memoryCard_id) {
        this.memoryCard_id = memoryCard_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemoryCard)) return false;

        MemoryCard memoryCard = (MemoryCard) o;

        if (memoryCard_id != memoryCard.memoryCard_id) return false;
        return name != null ? name.equals(memoryCard.name) : memoryCard.name == null;
    }

}
