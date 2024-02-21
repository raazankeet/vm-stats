package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class ActivityLogEntry {

    private String id;
    private String type;
    private String objectId;
    private String objectName;
    private int runId;
    private Date startTime;
    private Date endTime;
    private Date startTimeUtc;
    private Date endTimeUtc;
    private String state;
    private int UIState;
    private int failedSourceRows;
    private int successSourceRows;
    private int failedTargetRows;
    private int successTargetRows;
    private String startedBy;
    private String runContextType;
    private List<Entry> entries;
    private int totalSuccessRows;
    private int totalFailedRows;
    private boolean stopOnError;
    private boolean hasStopOnErrorRecord;
    private boolean isStopped;
//    private List<ObjectExtradatas> objectExtradatas;
}

