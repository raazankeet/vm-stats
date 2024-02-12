package com.wipro.dai.vmstats.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zerotouch_cpu_usage")
public class CPUUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CPUUsageID;

    @ManyToOne
    @JoinColumn(name = "VMID")
    private VirtualMachine virtualMachine;

    private LocalDateTime entryDate;
    private BigDecimal usagePercentage;

    public void setUsagePercentageWithScale(double   usagePercentage, int scale) {
        this.usagePercentage = BigDecimal.valueOf(usagePercentage).setScale(scale, RoundingMode.HALF_UP);
    }

}

