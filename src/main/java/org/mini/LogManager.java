package org.mini;

public class LogManager {
    private static PublicLogger publicLogger = null;

    public static void setPublicLogger(PublicLogger publicLogger) {
        LogManager.publicLogger = publicLogger;
    }

    public static PublicLogger getPublicLogger() {
        return publicLogger;
    }
}
