package fi.nordicwatt.utils;

import java.io.File;

/**
 * A Helper class for logging. All methods are static.
 * 
 */
public final class Logger {

    // the default logfolder that is put on root
    public static final String LOG_FOLDER = "logs";

    // the default logfile that is put in logfolder
    public static final String LOG_FILE = "log.log";

    /**
     * Checks if given logfile exists in the default logfolder and creates it if
     * it doesn't.
     * @param logFile
     */
    private static void createLogFileIfNotExists(String logFile) {
        // Check if logfolder exists, create if not
        File logFolder = new File(LOG_FOLDER);
        // Check if logfile exists, create if not
        File log = new File(LOG_FOLDER + "/" + logFile);

        try {
            if (!logFolder.exists()) {
                logFolder.mkdir();
            }
            if (!log.exists()) {
                log.createNewFile();
            }
        } catch (Exception e) {
            System.out.println("Error creating logfolder or logfile: " + e);
        }
    }

    /**
     * Logs a message to a logfile. If logfile doesn't exist, creates it.
     * @param message
     * @param logFile
     */
    public static void log(String message, String logFile) {
        createLogFileIfNotExists(logFile);
        try {
            String timestamp = java.time.LocalDateTime.now().toString();
            java.io.FileWriter fw = new java.io.FileWriter(LOG_FOLDER + "/" + logFile, true);
            fw.write(timestamp + " " + message + "\n");
            fw.close();
        } catch (Exception e) {
            System.out.println("Error logging message: " + e);
        }
    }

    /**
     * Logs a message to the default logfile. If logfile doesn't exist, creates it.
     * @param message
     */
    public static void log(String message) {
        log(message, LOG_FILE);
    }
}