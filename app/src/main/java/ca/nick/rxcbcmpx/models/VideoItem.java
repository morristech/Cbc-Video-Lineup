package ca.nick.rxcbcmpx.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

import static ca.nick.rxcbcmpx.models.VideoItem.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class VideoItem {

    public static final String TABLE_NAME = "video_item";

    @PrimaryKey
    private final long guid;
    private final String polopolyId;
    private final String src;
    private final String title;
    private final String description;
    private final double duration;
    private final String publishedDate;
    private final String live;
    private final String thumbnailUrl;
    private final long insertionTimestamp;

    public VideoItem(long guid,
                     String polopolyId,
                     String src,
                     String title,
                     String description,
                     double duration,
                     String publishedDate,
                     String live,
                     String thumbnailUrl,
                     long insertionTimestamp) {
        this.guid = guid;
        this.polopolyId = polopolyId;
        this.src = src;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.publishedDate = publishedDate;
        this.live = live;
        this.thumbnailUrl = thumbnailUrl;
        this.insertionTimestamp = insertionTimestamp;
    }

    public static VideoItem fromRemoteData(ThePlatformItem thePlatformItem) {
        TpFeedItem tpFeedItem = thePlatformItem.getTpFeedItem();
        PolopolyItem polopolyItem = tpFeedItem.getPolopolyItem();
        LineupItem lineupItem = polopolyItem.getLineupItem();

        return new VideoItem(
                thePlatformItem.getGuid(),
                polopolyItem.getId(),
                thePlatformItem.getUrl(),
                lineupItem.getTitle(),
                lineupItem.getDescription(),
                tpFeedItem.getDuration(),
                polopolyItem.getPubdate(),
                tpFeedItem.getLive(),
                polopolyItem.getImageUrl(),
                System.currentTimeMillis());
    }

    public long getGuid() {
        return guid;
    }

    public String getSrc() {
        return src;
    }

    public String getPolopolyId() {
        return polopolyId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getDuration() {
        return duration;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLive() {
        return live;
    }

    public long getInsertionTimestamp() {
        return insertionTimestamp;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VideoItem videoItem = (VideoItem) object;
        return guid == videoItem.guid &&
                Objects.equals(src, videoItem.src);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, src);
    }
}
