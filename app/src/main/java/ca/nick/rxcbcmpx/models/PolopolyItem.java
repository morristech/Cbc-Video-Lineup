package ca.nick.rxcbcmpx.models;

public class PolopolyItem {

    private final String id;
    private final String title;
    private final String show;
    private final String flag;
    private final String type;
    private final String url;
    private final String mediaid;
    private final String pubdate;
    private final Image image;
    private LineupItem lineupItem;

    public PolopolyItem(String id,
                        String title,
                        String show,
                        String flag,
                        String type,
                        String url,
                        String mediaid,
                        String pubdate,
                        Image image) {
        this.id = id;
        this.title = title;
        this.show = show;
        this.flag = flag;
        this.type = type;
        this.url = url;
        this.mediaid = mediaid;
        this.pubdate = pubdate;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShow() {
        return show;
    }

    public String getFlag() {
        return flag;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getImageUrl() {
        return image.url;
    }

    public String getMediaid() {
        return mediaid;
    }

    public PolopolyItem setLineupItem(LineupItem lineupItem) {
        this.lineupItem = lineupItem;
        return this;
    }

    public LineupItem getLineupItem() {
        return lineupItem;
    }

    @Override
    public String toString() {
        return "PolopolyItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", show='" + show + '\'' +
                ", flag='" + flag + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", mediaid='" + mediaid + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", image=" + image +
                ", lineupItem=" + lineupItem +
                '}';
    }

    public static class Image {
        private final String url;

        public Image(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
