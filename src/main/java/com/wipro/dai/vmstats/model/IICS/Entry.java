package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

import java.util.List;

@Data
public class Entry {
    private String id;
    private String type;
    private String objectName;
    private int runId;
    private String agentId;
    private String runtimeEnvironmentId;
    private String startTime;
    private String endTime;
    private String startTimeUtc;
    private String endTimeUtc;
    private int state;
    private int UIState;
    private int failedSourceRows;
    private int successSourceRows;
    private int failedTargetRows;
    private int successTargetRows;
    private String errorMsg;
    private String startedBy;
    private String runContextType;
    private LogEntryItemAttrs logEntryItemAttrs;
    private int totalSuccessRows;
    private int totalFailedRows;
    private boolean stopOnError;
    private boolean hasStopOnErrorRecord;
    private String contextExternalId;
    private boolean isStopped;
    private List<TransformationEntries> transformationEntries;

    // Getters and setters
}
