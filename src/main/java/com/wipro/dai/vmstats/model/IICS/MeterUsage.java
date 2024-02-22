package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Entity
@Table(name = "iics_ipu_meter_usage")
public class MeterUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgId;
    private String meterId;
    private String meterName;
    private Date date;
    private Date billingPeriodStartDate;
    private Date billingPeriodEndDate;
    private BigDecimal meterUsage;
    private BigDecimal IPU;
    private String scalar;
    private String metricCategory;
    private String orgName;
    private String orgType;
    private BigDecimal IPURate;
}
