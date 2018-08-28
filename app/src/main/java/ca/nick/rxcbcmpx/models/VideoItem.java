package ca.nick.rxcbcmpx.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.Objects;

import static ca.nick.rxcbcmpx.models.VideoItem.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class VideoItem {

    public static final String TABLE_NAME = "video_item";

    @PrimaryKey
    private final long guid;
    private final String sourceId;
    private final String src;
    private final String smilUrl;
    private final String title;
    private final String description;
    private final double duration;
    private final String publishedDate;
    private final String live;
    private final String thumbnailUrl;
    @Nullable
    private final String captionsUrl;
    private final long insertionTimestamp;

    public VideoItem(long guid,
                     String sourceId,
                     String src,
                     String smilUrl,
                     String title,
                     String description,
                     double duration,
                     String publishedDate,
                     String live,
                     String thumbnailUrl,
                     @Nullable String captionsUrl,
                     long insertionTimestamp) {
        this.guid = guid;
        this.sourceId = sourceId;
        this.src = src;
        this.smilUrl = smilUrl;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.publishedDate = publishedDate;
        this.live = live;
        this.thumbnailUrl = thumbnailUrl;
        this.captionsUrl = captionsUrl;
        this.insertionTimestamp = insertionTimestamp;
    }

    public static VideoItem fromRemoteData(ThePlatformItem thePlatformItem) {
        TpFeedItem tpFeedItem = thePlatformItem.getTpFeedItem();
        LineupItem lineupItem = tpFeedItem.getLineupItem();

        return new VideoItem(
                thePlatformItem.getGuid(),
                lineupItem.getSourceId(),
                thePlatformItem.getUrl(),
                tpFeedItem.getSmilUrl(),
                lineupItem.getTitle(),
                lineupItem.getDescription(),
                tpFeedItem.getDuration(),
                lineupItem.getReadablePublishedAt(),
                tpFeedItem.getLive(),
                lineupItem.getImageLarge(),
                thePlatformItem.getCaptions(),
                System.currentTimeMillis());
    }

    public long getGuid() {
        return guid;
    }

    public String getSrc() {
        return src;
    }

    public Uri getSrcUri() {
        return Uri.parse(src);
    }

    public String getSourceId() {
        return sourceId;
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

    public String getSmilUrl() {
        return smilUrl;
    }

    @Nullable
    public String getCaptionsUrl() {
        return captionsUrl;
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
                Objects.equals(sourceId, videoItem.sourceId) &&
                Objects.equals(smilUrl, videoItem.smilUrl) &&
                Objects.equals(title, videoItem.title) &&
                Objects.equals(description, videoItem.description) &&
                Objects.equals(publishedDate, videoItem.publishedDate) &&
                Objects.equals(live, videoItem.live);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, sourceId, smilUrl, title, description, publishedDate, live);
    }
}
