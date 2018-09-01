package ca.nick.rxcbcmpx.models;

import android.net.Uri;

import com.squareup.moshi.Json;

import java.util.List;

import ca.nick.rxcbcmpx.utils.Constants;

public class TpFeedItem {

    private final List<Entry> entries;
    private AggregateItem aggregateItem;

    public TpFeedItem(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public String getSmilUrl() {
        return findMediaContent().url;
    }

    public String getSmilUrlId() {
        return Uri.parse(getSmilUrl())
                .getLastPathSegment();
    }

    public double getDuration() {
        return findMediaContent().duration;
    }

    public String getLive() {
        return entries.get(0)
                .liveOnDemand;
    }

    public boolean isLive() {
        return Constants.LIVE_STATE.equalsIgnoreCase(getLive());
    }

    private Entry.MediaContent findMediaContent() {
        return entries.get(0)
                .mediaContents.get(0);
    }

    public TpFeedItem setAggregateItem(AggregateItem aggregateItem) {
        this.aggregateItem = aggregateItem;
        return this;
    }

    public AggregateItem getAggregateItem() {
        return aggregateItem;
    }

    @Override
    public String toString() {
        return "TpFeedItem{" +
                "entries=" + entries +
                ", aggregateItem=" + aggregateItem +
                '}';
    }

    public static class Entry {
        private final String id;
        private final String guid;
        private final String title;
        @Json(name = "plmedia$defaultThumbnailUrl")
        private final String thumbnailUrl;
        @Json(name = "media$content")
        private final List<MediaContent> mediaContents;
        @Json(name = "cbc$liveOndemand")
        private final String liveOnDemand;

        public Entry(String id, String guid, String title, String thumbnailUrl, List<MediaContent> mediaContents, String liveOnDemand) {
            this.id = id;
            this.guid = guid;
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
            this.mediaContents = mediaContents;
            this.liveOnDemand = liveOnDemand;
        }

        public String getId() {
            return id;
        }

        public String getGuid() {
            return guid;
        }

        public String getTitle() {
            return title;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public List<MediaContent> getMediaContents() {
            return mediaContents;
        }

        public String getLiveOnDemand() {
            return liveOnDemand;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "id='" + id + '\'' +
                    ", guid='" + guid + '\'' +
                    ", title='" + title + '\'' +
                    ", thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", mediaContents=" + mediaContents +
                    ", liveOnDemand='" + liveOnDemand + '\'' +
                    '}';
        }

        public static class MediaContent {
            @Json(name = "plfile$duration")
            private final double duration;
            @Json(name = "plfile$url")
            private final String url;

            public MediaContent(double duration, String url) {
                this.duration = duration;
                this.url = url;
            }

            public double getDuration() {
                return duration;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "MediaContent{" +
                        "duration=" + duration +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }
}
