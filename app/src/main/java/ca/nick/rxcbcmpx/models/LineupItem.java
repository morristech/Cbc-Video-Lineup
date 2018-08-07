package ca.nick.rxcbcmpx.models;

public class LineupItem {

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

    public LineupItem(String id, String title,
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

    public boolean isPolopolySource() {
        return "polopoly".equalsIgnoreCase(source);
    }

    public boolean isMpxSource() {
        return "mpx".equalsIgnoreCase(source);
    }

    public static class TypeAttributes {

        private final String url;

        public TypeAttributes(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
