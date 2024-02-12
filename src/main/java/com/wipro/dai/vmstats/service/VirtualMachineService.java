package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.exception.MachineRegisterException;

public interface VirtualMachineService {
    void registerMachine() throws MachineRegisterException;
}
