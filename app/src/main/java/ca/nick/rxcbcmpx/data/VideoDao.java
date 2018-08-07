package ca.nick.rxcbcmpx.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ca.nick.rxcbcmpx.models.VideoItem;

@Dao
public abstract class VideoDao {

    @Query("DELETE FROM " + VideoItem.TABLE_NAME)
    public abstract void nuke();

    @Query("SELECT * FROM " + VideoItem.TABLE_NAME)
    public abstract LiveData<List<VideoItem>> videoItems();
}
