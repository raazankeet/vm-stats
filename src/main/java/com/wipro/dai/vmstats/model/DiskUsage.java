package com.wipro.dai.vmstats.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "zerotouch_disk_usage")
    public class DiskUsage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer diskUsageID;

        @ManyToOne
        @JoinColumn(name = "VMID")
        private VirtualMachine virtualMachine;

        private LocalDateTime entryDate;

        private BigDecimal totalSpace;
        private BigDecimal freeSpace;
        private BigDecimal usedDiskPCT;

    public void setFreeSpaceWithScale(double usedSpace, int scale) {
        this.freeSpace = BigDecimal.valueOf(usedSpace).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalSpaceWithScale(double totalSpace, int scale) {
        this.totalSpace = BigDecimal.valueOf(totalSpace).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setUsedDiskPCTWithScale(double usedDiskPCT, int scale) {
        this.usedDiskPCT = BigDecimal.valueOf(usedDiskPCT).setScale(scale, RoundingMode.HALF_UP);
    }
}
