package ca.nick.rxcbcmpx.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static ca.nick.rxcbcmpx.models.VideoItem.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class VideoItem {

    public static final String TABLE_NAME = "video_item";

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String src;

    public VideoItem(long id, String src) {
        this.id = id;
        this.src = src;
    }

    public long getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "id=" + id +
                ", src='" + src + '\'' +
                '}';
    }
}
