package net.dolpen.mcmod.lib.logger;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LogWrapper {
    private final Logger logger;

    private Level minimumLevel;

    public LogWrapper(Logger logger) {
        this.logger = logger;
        minimumLevel = Level.INFO;
    }

    public void log(Level level, String message) {
        if (!level.isMoreSpecificThan(minimumLevel)) return; // skipped!
        logger.log(level, message);
    }

    public void setLevel(Level level) {
        minimumLevel = level;
    }

    public void fatal(String message) {
        log(Level.FATAL, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void trace(String message) {
        log(Level.TRACE, message);
    }

}
