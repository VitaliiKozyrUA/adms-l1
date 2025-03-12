package com.kozyr.adms.l1.utils;

public class Logger {
    private static String log = "";

    public static synchronized void log(String message) {
        log += message + "\n";
    }

    public static synchronized String getLog() {
        return log;
    }
}
