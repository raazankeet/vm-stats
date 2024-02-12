package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.exception.VirtualMachineNotFoundException;
import com.wipro.dai.vmstats.model.*;
import com.wipro.dai.vmstats.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UsageDetailServiceImpl implements UsageDetailService {

    @Autowired
    private CPUUsageRepository cpuUsageRepository;

    @Autowired
    private MemoryUsageRepository memoryUsageRepository;

    @Autowired
    private DiskUsageRepository diskUsageRepository;

    @Autowired
    private NetworkUsageRepository networkUsageRepository;

    @Autowired
    private VirtualMachineRepository virtualMachineRepository;

    @Override
    @Transactional
    public void updateVMStats(VMStatsData vmStatsData) throws VirtualMachineNotFoundException {
        String machineName=vmStatsData.getMachineName();
        log.info("Updating VM stats for machine: {}, VMStatsData: {}", machineName, vmStatsData);
        updateCPUUsage(machineName, vmStatsData.getCpuUtilization());
        updateMemoryUsage(machineName, vmStatsData.getTotalPhysicalMemory(), vmStatsData.getAvailablePhysicalMemory());
        updateDiskUsage(machineName, vmStatsData.getTotalDiskSize(), vmStatsData.getFreeDiskSpace());
        updateNetworkUsage(machineName, vmStatsData.getNetworkIn(), vmStatsData.getNetworkOut());
        log.info("VM stats update completed for machine: {}", machineName);
    }

    @Override
    @Transactional
    public void updateCPUUsage(String machineName, Float cpuUtilization) throws VirtualMachineNotFoundException {
        log.debug("Updating CPU usage for machine: {}", machineName);
        VirtualMachine virtualMachine = getVirtualMachineByName(machineName);
        CPUUsage cpuUsage = new CPUUsage();
        cpuUsage.setVirtualMachine(virtualMachine);
        cpuUsage.setUsagePercentageWithScale(cpuUtilization,2);
        cpuUsage.setEntryDate(LocalDateTime.now());
        cpuUsageRepository.save(cpuUsage);
        log.debug("CPU usage updated for machine: {}", machineName);
    }

    @Override
    @Transactional
    public void updateMemoryUsage(String machineName, long totalMemory, long availableMemory) throws VirtualMachineNotFoundException {
        log.debug("Updating memory usage for machine: {}", machineName);
        VirtualMachine virtualMachine = getVirtualMachineByName(machineName);
        MemoryUsage memoryUsage = new MemoryUsage();
        memoryUsage.setVirtualMachine(virtualMachine);
        memoryUsage.setTotalMemoryWithScale(totalMemory,2);
        memoryUsage.setAvailableMemoryWithScale(availableMemory,2);
        memoryUsage.setUsedMemoryPCTWithScale((100 - ((double) availableMemory / totalMemory * 100)),2);
        memoryUsage.setEntryDate(LocalDateTime.now());
        memoryUsageRepository.save(memoryUsage);
        log.debug("Memory usage updated for machine: {}", machineName);
    }

    @Override
    @Transactional
    public void updateDiskUsage(String machineName, long totalSpace, long freeSpace) throws VirtualMachineNotFoundException {
        log.debug("Updating disk usage for machine: {}", machineName);
        VirtualMachine virtualMachine = getVirtualMachineByName(machineName);
        DiskUsage diskUsage = new DiskUsage();
        diskUsage.setVirtualMachine(virtualMachine);
        diskUsage.setTotalSpaceWithScale(totalSpace,0);
        diskUsage.setFreeSpaceWithScale(freeSpace,0);
        diskUsage.setUsedDiskPCTWithScale((100 - ((double) freeSpace / totalSpace * 100)),2);
        diskUsage.setEntryDate(LocalDateTime.now());
        diskUsageRepository.save(diskUsage);
        log.debug("Disk usage updated for machine: {}", machineName);
    }

    @Override
    @Transactional
    public void updateNetworkUsage(String machineName, float incomingTraffic, float outgoingTraffic) throws VirtualMachineNotFoundException {
        log.debug("Updating network usage for machine: {}", machineName);
        VirtualMachine virtualMachine = getVirtualMachineByName(machineName);
        NetworkUsage networkUsage = new NetworkUsage();

        networkUsage.setVirtualMachine(virtualMachine);
        networkUsage.setIncomingTrafficWithScale(incomingTraffic,2);
        networkUsage.setOutgoingTrafficWithScale(outgoingTraffic,2);
        networkUsage.setEntryDate(LocalDateTime.now());

        networkUsageRepository.save(networkUsage);
        log.debug("Network usage updated for machine: {}", machineName);
    }

    private VirtualMachine getVirtualMachineByName(String machineName) throws VirtualMachineNotFoundException {
        Optional<VirtualMachine> optionalVirtualMachine = virtualMachineRepository.findFirstByVMName(machineName);
        if (optionalVirtualMachine.isPresent()) {
            return optionalVirtualMachine.get();
        } else {
            log.error("Virtual machine with name {} not found", machineName);
            throw new VirtualMachineNotFoundException("Virtual machine with name " + machineName + " not found");
        }
    }
}
