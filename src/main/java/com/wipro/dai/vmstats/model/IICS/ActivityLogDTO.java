package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "iics_activity_logs")
public class ActivityLogDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long databaseId;

    private String id;
    private String type;
    private String objectId;
    private String objectName;
    private int runId;
    private String agentId;
    private String runtimeEnvironmentId;
    private Date startTime;
    private Date endTime;
    private Date startTimeUtc;
    private Date endTimeUtc;
    private String state;
    private int failedSourceRows;
    private int successSourceRows;
    private int failedTargetRows;
    private int successTargetRows;

    @Column(columnDefinition = "TEXT")
    private String errorMsg;
    private String startedBy;
    private String logFileName;
    private String consumedComputeUnits;
    private String errorCode;
    private String isServerLess;
    private String requestedComputeUnits;
    private String serviceType;
    private String detailedSessionLog;
    private String actualExecutionTime;
}
