package ca.nick.rxcbcmpx.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "smil")
public class ThePlatformItem {

    @Element
    private Head head;
    @Element
    private Body body;

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
        return body.seq.video.guid;
    }

    public String getUrl() {
        return body.seq.video != null
                ? body.seq.video.src
                : body.seq.par.video.src;
    }

    @Override
    public String toString() {
        return "ThePlatformItem{" +
                "head=" + head +
                ", body=" + body +
                '}';
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

        @Override
        public String toString() {
            return "Head{" +
                    "meta=" + meta +
                    '}';
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

            @Override
            public String toString() {
                return "Meta{" +
                        "name='" + name + '\'' +
                        ", content='" + content + '\'' +
                        '}';
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

        @Override
        public String toString() {
            return "Body{" +
                    "seq=" + seq +
                    '}';
        }

        public static class Seq {
            @Element
            private Video video;
            @Element(required = false)
            private Par par;

            public Seq() {
                this(null, null);
            }

            public Seq(Video video, Par par) {
                this.video = video;
                this.par = par;
            }

            public Video getVideo() {
                return video;
            }

            @Override
            public String toString() {
                return "Seq{" +
                        "video=" + video +
                        ", par=" + par +
                        '}';
            }

            public static class Par {

                @Attribute
                private Video video;

                public Par() {
                    this(null);
                }

                public Par(Video video) {
                    this.video = video;
                }

                public Video getVideo() {
                    return video;
                }
            }

            public static class Video {
                @Attribute
                private String src;
                @Attribute(required = false)
                private String title;
                @Attribute(name = "abstract", required = false)
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
                @ElementList(inline = true, required = false)
                private List<Param> params;

                public Video() {
                    this(null, null, null, null, -1, null, null, null, null, null, -1, -1, null, null, null, null);
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
                             List<Param> params) {
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

                public List<Param> getParams() {
                    return params;
                }

                @Override
                public String toString() {
                    return "Video{" +
                            "src='" + src + '\'' +
                            ", title='" + title + '\'' +
                            ", abstract_='" + abstract_ + '\'' +
                            ", dur='" + dur + '\'' +
                            ", guid='" + guid + '\'' +
                            ", categories='" + categories + '\'' +
                            ", author='" + author + '\'' +
                            ", copyright='" + copyright + '\'' +
                            ", provider='" + provider + '\'' +
                            ", type='" + type + '\'' +
                            ", height='" + height + '\'' +
                            ", width='" + width + '\'' +
                            ", keywords='" + keywords + '\'' +
                            ", ratings='" + ratings + '\'' +
                            ", expression='" + expression + '\'' +
                            ", params=" + params +
                            '}';
                }

                @Root
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

                    @Override
                    public String toString() {
                        return "Param{" +
                                "name='" + name + '\'' +
                                ", value='" + value + '\'' +
                                '}';
                    }
                }
            }
        }
    }
}
