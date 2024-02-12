package com.wipro.dai.vmstats.model;

import lombok.Data;

@Data
public class VMStatsData {
    // Getters and setters
    private String machineName;
    private float cpuUtilization;
    private float networkIn;
    private float networkOut;
    private long totalDiskSize;
    private long freeDiskSpace;
    private long availablePhysicalMemory;
    private long totalPhysicalMemory;


}
