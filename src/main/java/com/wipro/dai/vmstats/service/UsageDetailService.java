package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.exception.VirtualMachineNotFoundException;
import com.wipro.dai.vmstats.model.VMStatsData;
import org.springframework.transaction.annotation.Transactional;

public interface UsageDetailService {


    void updateVMStats( VMStatsData vmStatsData) throws VirtualMachineNotFoundException;

    void updateCPUUsage(String machineName, Float cpuUtilization) throws VirtualMachineNotFoundException;


    void updateMemoryUsage(String machineName, long totalMemory, long usedMemory) throws VirtualMachineNotFoundException;

    void updateDiskUsage(String machineName, long totalSpace,long usedSpace) throws VirtualMachineNotFoundException;
    void updateNetworkUsage(String machineName, float outgoingTraffic, float incomingTraffic) throws VirtualMachineNotFoundException;
}
