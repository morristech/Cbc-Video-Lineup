package ca.nick.rxcbcmpx.models;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "smil", strict = false)
public class ThePlatformItem {

    @Element
    private Head head;
    @Element
    private Body body;
    private TpFeedItem tpFeedItem;

    public ThePlatformItem() {
        this(null, null);
    }

    public ThePlatformItem(Head head, Body body) {
        this.head = head;
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public Body getBody() {
        return body;
    }

    public long getGuid() {
        return findVideo().guid;
    }

    public String getUrl() {
        return findVideo().src;
    }

    @Nullable
    public String getCaptions() {
        Body.Seq.Par par = body.seq.pars != null
                ? body.seq.pars.get(0)
                : null;

        if (par != null && par.textStreams != null) {
            for (Body.Seq.Par.TextStream textStream : par.textStreams) {
                if (textStream.src != null) {
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

    public static class Head {

        @Element(required = false)
        private Meta meta;

        public Head() {
            this(null);
        }

        public Head(Meta meta) {
            this.meta = meta;
        }

        public Meta getMeta() {
            return meta;
        }

        public static class Meta {
            @Attribute
            private String name;
            @Attribute
            private String content;

            public Meta() {
                this(null, null);
            }

            public Meta(String name, String content) {
                this.name = name;
                this.content = content;
            }

            public String getName() {
                return name;
            }

            public String getContent() {
                return content;
            }
        }
    }

    public static class Body {
        @Element
        private Seq seq;

        public Body() {
            this(null);
        }

        public Body(Seq seq) {
            this.seq = seq;
        }

        public Seq getSeq() {
            return seq;
        }

        public static class Seq {
            @Element(required = false)
            private Video video;
            @ElementList(inline = true, required = false)
            private List<Par> pars;

            public Seq() {
                this(null, null);
            }

            public Seq(Video video, List<Par> pars) {
                this.video = video;
                this.pars = pars;
            }

            public Video getVideo() {
                return video;
            }

            public List<Par> getPars() {
                return pars;
            }

            @Root(strict = false)
            public static class Par {

                @Element
                private Video video;
                @ElementList(inline = true, required = false)
                private List<TextStream> textStreams;

                public Par() {
                    this(null, null);
                }

                public Par(Video video, List<TextStream> textStreams) {
                    this.video = video;
                    this.textStreams = textStreams;
                }

                public Video getVideo() {
                    return video;
                }

                public List<TextStream> getTextStreams() {
                    return textStreams;
                }

                @Root(strict = false)
                public static class TextStream {
                    @Attribute
                    private String src;
                    @Attribute(required = false)
                    private String type;
                    @Attribute(required = false)
                    private boolean closedCaptions;
                    @Attribute(required = false)
                    private String lang;

                    public TextStream() {
                        this(null, null, false, null);
                    }

                    public TextStream(String src, String type, boolean closedCaptions, String lang) {
                        this.src = src;
                        this.type = type;
                        this.closedCaptions = closedCaptions;
                        this.lang = lang;
                    }
                }
            }

            public static class Video {
                @Attribute
                private String src;
                @Attribute
                private String title;
                @Attribute(name = "abstract")
                private String abstract_;
                @Attribute(required = false)
                private String dur;
                @Attribute(required = false)
                private long guid;
                @Attribute(required = false)
                private String categories;
                @Attribute(required = false)
                private String author;
                @Attribute(required = false)
                private String copyright;
                @Attribute(required = false)
                private String provider;
                @Attribute(required = false)
                private String type;
                @Attribute(required = false)
                private int height;
                @Attribute(required = false)
                private int width;
                @Attribute(required = false)
                private String keywords;
                @Attribute(required = false)
                private String ratings;
                @Attribute(required = false)
                private String expression;
                @Attribute(required = false)
                private String clipBegin;
                @Attribute(required = false)
                private String clipEnd;
                @ElementList(inline = true, required = false)
                private List<Param> params;

                public Video() {
                    this(null, null, null, null, -1, null, null, null, null, null, -1, -1, null, null, null, null, null, null);
                }

                public Video(String src,
                             String title,
                             String abstract_,
                             String dur,
                             long guid,
                             String categories,
                             String author,
                             String copyright,
                             String provider,
                             String type,
                             int height,
                             int width,
                             String keywords,
                             String ratings,
                             String expression,
                             String clipBegin, String clipEnd, List<Param> params) {
                    this.src = src;
                    this.title = title;
                    this.abstract_ = abstract_;
                    this.dur = dur;
                    this.guid = guid;
                    this.categories = categories;
                    this.author = author;
                    this.copyright = copyright;
                    this.provider = provider;
                    this.type = type;
                    this.height = height;
                    this.width = width;
                    this.keywords = keywords;
                    this.ratings = ratings;
                    this.expression = expression;
                    this.clipBegin = clipBegin;
                    this.clipEnd = clipEnd;
                    this.params = params;
                }

                public String getSrc() {
                    return src;
                }

                public String getTitle() {
                    return title;
                }

                public String getAbstract_() {
                    return abstract_;
                }

                public String getDur() {
                    return dur;
                }

                public long getGuid() {
                    return guid;
                }

                public String getCategories() {
                    return categories;
                }

                public String getAuthor() {
                    return author;
                }

                public String getCopyright() {
                    return copyright;
                }

                public String getProvider() {
                    return provider;
                }

                public String getType() {
                    return type;
                }

                public int getHeight() {
                    return height;
                }

                public int getWidth() {
                    return width;
                }

                public String getKeywords() {
                    return keywords;
                }

                public String getRatings() {
                    return ratings;
                }

                public String getExpression() {
                    return expression;
                }

                public String getClipBegin() {
                    return clipBegin;
                }

                public String getClipEnd() {
                    return clipEnd;
                }

                public List<Param> getParams() {
                    return params;
                }

                @Root(strict = false)
                public static class Param {
                    @Attribute
                    private String name;
                    @Attribute
                    private String value;

                    public Param() {
                        this(null, null);
                    }

                    public Param(String name, String value) {
                        this.name = name;
                        this.value = value;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getValue() {
                        return value;
                    }
                }
            }
        }
    }
}
