package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name = "iics_cdi_metering_audit_data")
public class CDIAuditMeteringData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskId;
    private String taskName;
    private String taskObjectName;
    private String taskType;
    private int taskRunId;
    private String projectName;
    private String folderName;
    private String orgId;
    private String environmentId;
    private String environment;
    private double coresUsed;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private double meteredValue;
    private LocalDateTime auditTime;
    private int obmTaskTimeSeconds;

}
