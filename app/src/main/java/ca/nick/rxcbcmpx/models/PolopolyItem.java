package ca.nick.rxcbcmpx.models;

public class PolopolyItem {

    private final String title;
    private final String show;
    private final String flag;

    public PolopolyItem(String title,
                        String show,
                        String flag) {
        this.title = title;
        this.show = show;
        this.flag = flag;
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

    @Override
    public String toString() {
        return "PolopolyItem{" +
                "title='" + title + '\'' +
                ", show='" + show + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
