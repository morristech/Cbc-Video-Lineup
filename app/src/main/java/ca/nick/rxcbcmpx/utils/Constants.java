package ca.nick.rxcbcmpx.utils;

public final class Constants {

    private Constants() { throw new IllegalStateException("Nope!"); }

    public static final String USER_AGENT_KEY = "User-Agent";
    public static final String USER_AGENT_VALUE = "CBC/AndroidApp";
    public static final String USER_AGENT = USER_AGENT_KEY + ": " + USER_AGENT_VALUE;
}
