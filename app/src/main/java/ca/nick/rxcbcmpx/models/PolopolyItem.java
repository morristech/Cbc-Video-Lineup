package ca.nick.rxcbcmpx.models;

public class PolopolyItem {

    private final String title;
    private final String show;
    private final String flag;
    private final String type;
    private final String url;
    private final String mediaid;

    public PolopolyItem(String title,
                        String show,
                        String flag,
                        String type,
                        String url,
                        String mediaid) {
        this.title = title;
        this.show = show;
        this.flag = flag;
        this.type = type;
        this.url = url;
        this.mediaid = mediaid;
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

    public String getMediaid() {
        return mediaid;
    }
}
