package com.wipro.dai.vmstats.model.IICS;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
public class ActivityTableRow {

    @Id
    @Column(name="id")
    private String id;

    private String type;
    private String objectId;
    private String objectName;
    private int runID;
    private String agentID;
    private String runtimeEnvironmentId;
    private Date startTime;
    private Date endTime;
    private Date startTimeUTC;
    private Date endTimeUTC;
    private String state;
    private String failedSourceRow;
    private String successSourceRow;
    private String failedTargetRow;
    private String successTargetRow;
    private String errorMsg;

    private String startedBy;
    private String logFileName;
    private String itemID;
    private String consumedComputeUnits;
    private String errorCode;
    private String isServerLess;
    private String requestedComputeUnits;
    private String serviceType;
    private String detailedSessionLog;
    private String stateDescription;
    private String actualExecutionTime;


}
