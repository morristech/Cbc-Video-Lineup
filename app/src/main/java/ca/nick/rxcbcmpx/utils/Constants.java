package ca.nick.rxcbcmpx.utils;

public final class Constants {

    private Constants() { throw new IllegalStateException("Nope!"); }

    public static final String USER_AGENT_KEY = "User-Agent";
    public static final String USER_AGENT_VALUE = "CBC/AndroidApp";
    public static final String USER_AGENT = USER_AGENT_KEY + ": " + USER_AGENT_VALUE;

    public static final String SAMPLE_AD_URL = "it's a secret";
    public static final String LIVE_STATE = "live";
    public static final String SRT_CAPTIONS = "text/srt";
}
