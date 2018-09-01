package ca.nick.rxcbcmpx.utils;

public final class Constants {

    private Constants() { throw new IllegalStateException("Nope!"); }

    public static final String USER_AGENT_KEY = "User-Agent";
    public static final String USER_AGENT_VALUE = "CBC/AndroidApp";
    public static final String USER_AGENT = USER_AGENT_KEY + ": " + USER_AGENT_VALUE;

    public static final String SAMPLE_AD_URL = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";
    public static final String LIVE_STATE = "live";
    public static final String SRT_CAPTIONS = "text/srt";
}
