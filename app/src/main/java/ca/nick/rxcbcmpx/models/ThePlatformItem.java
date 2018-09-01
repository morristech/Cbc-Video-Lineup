package ca.nick.rxcbcmpx.models;

import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import ca.nick.rxcbcmpx.utils.Constants;

@Xml(name = "smil")
public class ThePlatformItem {

    @Element
    private Head head;
    @Element
    private Body body;
    private TpFeedItem tpFeedItem;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public long getGuid() {
        return findVideo().guid;
    }

    public String getUrl() {
        return findVideo().src;
    }

    @Nullable
    public String getSrtCaptions() {
        Body.Seq.Par par = body.seq.pars != null
                ? body.seq.pars.get(0)
                : null;

        if (par != null && par.textStreams != null) {
            for (Body.Seq.Par.TextStream textStream : par.textStreams) {
                if (textStream.src != null
                        && textStream.type != null
                        && Constants.SRT_CAPTIONS.equals(textStream.type)) {
                    return textStream.src;
                }
            }
        }

        return null;
    }

    private Body.Seq.Video findVideo() {
        return body.seq.video != null
                ? body.seq.video
                : body.seq.pars.get(0).video;
    }

    public ThePlatformItem setTpFeedItem(TpFeedItem tpFeedItem) {
        this.tpFeedItem = tpFeedItem;
        return this;
    }

    public TpFeedItem getTpFeedItem() {
        return tpFeedItem;
    }

    @Xml
    public static class Head {

        @Element
        private Meta meta;

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        @Xml
        public static class Meta {

            @Attribute
            private String name;
            @Attribute
            private String content;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }

    @Xml
    public static class Body {

        @Element
        private Seq seq;

        public Seq getSeq() {
            return seq;
        }

        public void setSeq(Seq seq) {
            this.seq = seq;
        }

        @Xml
        public static class Seq {

            @Element
            private Video video;
            @Element
            private List<Par> pars;

            public Video getVideo() {
                return video;
            }

            public void setVideo(Video video) {
                this.video = video;
            }

            public List<Par> getPars() {
                return pars;
            }

            public void setPars(List<Par> pars) {
                this.pars = pars;
            }

            @Xml
            public static class Par {

                @Element
                private Video video;
                @Element
                private List<TextStream> textStreams;

                public Video getVideo() {
                    return video;
                }

                public void setVideo(Video video) {
                    this.video = video;
                }

                public List<TextStream> getTextStreams() {
                    return textStreams;
                }

                public void setTextStreams(List<TextStream> textStreams) {
                    this.textStreams = textStreams;
                }

                @Xml(name = "textstream")
                public static class TextStream {

                    @Attribute
                    private String src;
                    @Attribute
                    private String type;
                    @Attribute
                    private boolean closedCaptions;
                    @Attribute
                    private String lang;

                    public String getSrc() {
                        return src;
                    }

                    public void setSrc(String src) {
                        this.src = src;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public boolean isClosedCaptions() {
                        return closedCaptions;
                    }

                    public void setClosedCaptions(boolean closedCaptions) {
                        this.closedCaptions = closedCaptions;
                    }

                    public String getLang() {
                        return lang;
                    }

                    public void setLang(String lang) {
                        this.lang = lang;
                    }
                }
            }

            @Xml
            public static class Video {

                @Attribute
                private String src;
                @Attribute
                private String title;
                @Attribute(name = "abstract")
                private String abstract_;
                @Attribute
                private String dur;
                @Attribute
                private long guid;
                @Attribute
                private String categories;
                @Attribute
                private String author;
                @Attribute
                private String copyright;
                @Attribute
                private String provider;
                @Attribute
                private String type;
                @Attribute
                private int height;
                @Attribute
                private int width;
                @Attribute
                private String keywords;
                @Attribute
                private String ratings;
                @Attribute
                private String expression;
                @Attribute
                private String clipBegin;
                @Attribute
                private String clipEnd;
                @Element
                private List<Param> params;

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getAbstract_() {
                    return abstract_;
                }

                public void setAbstract_(String abstract_) {
                    this.abstract_ = abstract_;
                }

                public String getDur() {
                    return dur;
                }

                public void setDur(String dur) {
                    this.dur = dur;
                }

                public long getGuid() {
                    return guid;
                }

                public void setGuid(long guid) {
                    this.guid = guid;
                }

                public String getCategories() {
                    return categories;
                }

                public void setCategories(String categories) {
                    this.categories = categories;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getCopyright() {
                    return copyright;
                }

                public void setCopyright(String copyright) {
                    this.copyright = copyright;
                }

                public String getProvider() {
                    return provider;
                }

                public void setProvider(String provider) {
                    this.provider = provider;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getKeywords() {
                    return keywords;
                }

                public void setKeywords(String keywords) {
                    this.keywords = keywords;
                }

                public String getRatings() {
                    return ratings;
                }

                public void setRatings(String ratings) {
                    this.ratings = ratings;
                }

                public String getExpression() {
                    return expression;
                }

                public void setExpression(String expression) {
                    this.expression = expression;
                }

                public String getClipBegin() {
                    return clipBegin;
                }

                public void setClipBegin(String clipBegin) {
                    this.clipBegin = clipBegin;
                }

                public String getClipEnd() {
                    return clipEnd;
                }

                public void setClipEnd(String clipEnd) {
                    this.clipEnd = clipEnd;
                }

                public List<Param> getParams() {
                    return params;
                }

                public void setParams(List<Param> params) {
                    this.params = params;
                }

                @Xml
                public static class Param {

                    @Attribute
                    private String name;
                    @Attribute
                    private String value;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }
                }
            }
        }
    }
}
