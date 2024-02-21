package com.wipro.dai.vmstats.model.IICS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogEntryItemAttrs {
    @JsonProperty("CONSUMED_COMPUTE_UNITS")
    private String CONSUMED_COMPUTE_UNITS;
    @JsonProperty("ERROR_CODE")
    private String ERROR_CODE;
    @JsonProperty("IS_SERVER_LESS")
    private String IS_SERVER_LESS;
    @JsonProperty("OBJ_LOCATION")
    private String objLocation;
    @JsonProperty("REQUESTED_COMPUTE_UNITS")
    private String requestedComputeUnits;
    @JsonProperty("SERVICE_TYPE")
    private String serviceType;
    @JsonProperty("Session Log File Name")
    private String sessionLogFileName;
    private String actualExecutionTime;
    private String actualTaskExecutionTime;
    private String releaseVersion;

}
