package com.wipro.dai.vmstats.exception;

public class ConfigFileException extends Exception{
    public ConfigFileException(String message, String reportsFromLastNDays) {
        super(message);
    }
}
