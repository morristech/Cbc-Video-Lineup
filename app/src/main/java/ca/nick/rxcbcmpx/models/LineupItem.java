package ca.nick.rxcbcmpx.models;

public class LineupItem {

    private final String title;
    private final String description;
    private final String source;
    private final String sourceId;
    private final long publishedAt;
    private final String readablePublishedAt;
    private final long updatedAt;
    private final String readableUpdatedAt;

    public LineupItem(String title,
                      String description,
                      String source,
                      String sourceId,
                      long publishedAt,
                      String readablePublishedAt,
                      long updatedAt,
                      String readableUpdatedAt) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.sourceId = sourceId;
        this.publishedAt = publishedAt;
        this.readablePublishedAt = readablePublishedAt;
        this.updatedAt = updatedAt;
        this.readableUpdatedAt = readableUpdatedAt;
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

    public boolean isValidPolopolySource() {
        return "polopoly".equalsIgnoreCase(source)
                // FIXME
//                && !sourceId.startsWith("1.2")
                ;
    }

    public boolean isMpxSource() {
        return "mpx".equalsIgnoreCase(source);
    }

    @Override
    public String toString() {
        return "LineupItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", source='" + source + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", publishedAt=" + publishedAt +
                ", readablePublishedAt='" + readablePublishedAt + '\'' +
                ", updatedAt=" + updatedAt +
                ", readableUpdatedAt='" + readableUpdatedAt + '\'' +
                '}';
    }
}
