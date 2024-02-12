package com.wipro.dai.vmstats.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "zerotouch_network_usage")
public class NetworkUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer networkUsageID;

    @ManyToOne
    @JoinColumn(name = "VMID")
    private VirtualMachine virtualMachine;

    private LocalDateTime entryDate;
    private BigDecimal  outgoingTraffic;
    private BigDecimal incomingTraffic;

    public void setOutgoingTrafficWithScale(double totalMemory, int scale) {
        this.outgoingTraffic = BigDecimal.valueOf(totalMemory).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setIncomingTrafficWithScale(double availableMemory, int scale) {
        this.incomingTraffic = BigDecimal.valueOf(availableMemory).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
}
