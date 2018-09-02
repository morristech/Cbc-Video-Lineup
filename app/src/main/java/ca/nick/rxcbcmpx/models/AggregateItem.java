package ca.nick.rxcbcmpx.models;

import android.net.Uri;

public class AggregateItem {

    private final String id;
    private final String title;
    private final String description;
    private final String source;
    private final String sourceId;
    private final long publishedAt;
    private final String readablePublishedAt;
    private final long updatedAt;
    private final String readableUpdatedAt;
    private final TypeAttributes typeAttributes;

    private static final String MPX_SOURCE = "mpx";
    private static final String POLOPOLY_SOURCE = "Polopoly";

    public AggregateItem(String id,
                         String title,
                         String description,
                         String source,
                         String sourceId,
                         long publishedAt,
                         String readablePublishedAt,
                         long updatedAt,
                         String readableUpdatedAt,
                         TypeAttributes typeAttributes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.source = source;
        this.sourceId = sourceId;
        this.publishedAt = publishedAt;
        this.readablePublishedAt = readablePublishedAt;
        this.updatedAt = updatedAt;
        this.readableUpdatedAt = readableUpdatedAt;
        this.typeAttributes = typeAttributes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public String getReadablePublishedAt() {
        return readablePublishedAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getReadableUpdatedAt() {
        return readableUpdatedAt;
    }

    public TypeAttributes getTypeAttributes() {
        return typeAttributes;
    }

    public String getImageLarge() {
        return getTypeAttributes().imageLarge;
    }

    public boolean isPolopolySource() {
        return POLOPOLY_SOURCE.equals(source);
    }

    public boolean isMpxSource() {
        return MPX_SOURCE.equals(source);
    }

    public String getMpxSourceGuid() {
        if (!isMpxSource()) {
            throw new RuntimeException("Trying to get MPX Source GUID when source was not MPX");
        }

        Uri uri = Uri.parse(sourceId);
        return uri.getLastPathSegment();
    }

    @Override
    public String toString() {
        return "AggregateItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", source='" + source + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", publishedAt=" + publishedAt +
                ", readablePublishedAt='" + readablePublishedAt + '\'' +
                ", updatedAt=" + updatedAt +
                ", readableUpdatedAt='" + readableUpdatedAt + '\'' +
                ", typeAttributes=" + typeAttributes +
                '}';
    }

    public static class TypeAttributes {

        private final String url;
        private final String imageLarge;

        public TypeAttributes(String url, String imageLarge) {
            this.url = url;
            this.imageLarge = imageLarge;
        }

        @Override
        public String toString() {
            return "TypeAttributes{" +
                    "url='" + url + '\'' +
                    ", imageLarge='" + imageLarge + '\'' +
                    '}';
        }
    }
}
