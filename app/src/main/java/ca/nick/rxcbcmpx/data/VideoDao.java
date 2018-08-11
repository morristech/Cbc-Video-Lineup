package ca.nick.rxcbcmpx.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ca.nick.rxcbcmpx.models.VideoItem;

@Dao
public abstract class VideoDao {

    @Query("SELECT * FROM " + VideoItem.TABLE_NAME + " ORDER BY insertionTimestamp")
    public abstract LiveData<List<VideoItem>> videoItems();

    @Query("DELETE FROM " + VideoItem.TABLE_NAME)
    public abstract void nuke();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVideoItem(VideoItem videoItem);

    @Delete
    public abstract void deleteVideoItem(VideoItem videoItem);
}
