package com.wipro.dai.vmstats.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zerotouch_memory_usage")
public class MemoryUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memoryUsageID;

    @ManyToOne
    @JoinColumn(name = "VMID")
    private VirtualMachine virtualMachine;
    private BigDecimal totalMemory;
    private BigDecimal availableMemory;
    private BigDecimal usedMemoryPCT;

    private LocalDateTime entryDate;

    public void setTotalMemoryWithScale(double totalMemory, int scale) {
        this.totalMemory = BigDecimal.valueOf(totalMemory).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setUsedMemoryPCTWithScale(double usedMemoryPCT, int scale) {
        this.usedMemoryPCT = BigDecimal.valueOf(usedMemoryPCT).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setAvailableMemoryWithScale(double availableMemory, int scale) {
        this.availableMemory = BigDecimal.valueOf(availableMemory).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }



}