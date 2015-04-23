package uk.co.gajzler.log;

import org.apache.log4j.Logger;
import java.text.MessageFormat;

public class SLogger {

    private final Logger log;

    private SLogger(Class<?> clazz) {
        this.log = Logger.getLogger(clazz);
    }

    private SLogger(String name) {
        this.log = Logger.getLogger(name);
    }

    public static SLogger getLogger(Class<?> clazz) {
        return new SLogger(clazz);
    }

    public void info(String message, Object... params) {
        if (this.log.isInfoEnabled()) {
            this.log.info(this.formatMessage(message, params));
        }

    }

    public void info(String message) {
        this.log.info(message);
    }

    public void debug(String message, Object... params) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.formatMessage(message, params));
        }

    }

    public void debug(String message) {
        this.log.debug(message);
    }

    public void trace(String message, Object... params) {
        if (this.log.isTraceEnabled()) {
            this.log.trace(this.formatMessage(message, params));
        }

    }

    public void trace(String message) {
        this.log.debug(message);
    }

    public void warn(String message) {
        this.log.warn(message);
    }

    public void warn(String message, Object... params) {
        this.log.warn(this.formatMessage(message, params));
    }

    public void warn(Throwable ex, String message) {
        this.log.warn(message, ex);
    }

    public void warn(Throwable ex, String message, Object... params) {
        this.log.warn(this.formatMessage(message, params), ex);
    }

    private String formatMessage(String message, Object... params) {
        return MessageFormat.format(message, params);
    }

    public void error(String message) {
        this.log.error(message);
    }

    public void error(String message, Object... params) {
        this.log.error(this.formatMessage(message, params));
    }

    public void error(Throwable ex, String message, Object... params) {
        this.log.error(this.formatMessage(message, params), ex);
    }

    public void errorThrow(Throwable ex, String message) {
        this.log.error(message, ex);
    }
}
