package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.exception.MachineRegisterException;
import com.wipro.dai.vmstats.model.VirtualMachine;
import com.wipro.dai.vmstats.repository.VirtualMachineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class VirtualMachineServiceImpl implements VirtualMachineService {

    @Autowired
    private VirtualMachineRepository virtualMachineRepository;

    @Transactional
    public void createVirtualMachine(VirtualMachine vm) {

        virtualMachineRepository.save(vm);
    }

    @Override
    public void registerMachine() throws MachineRegisterException {
        String VMname = null;
        try {
            InetAddress id = InetAddress.getLocalHost();
            VMname = id.getHostName();
            String OS = System.getProperty("os.name");
            LocalDateTime provisioningDate = LocalDateTime.now();
            Optional<VirtualMachine> optionalVirtualMachine = virtualMachineRepository.findFirstByVMName(VMname);

            if (!optionalVirtualMachine.isPresent()) {
                log.info("Registering " + VMname);
                VirtualMachine vm = new VirtualMachine();
                vm.setVMName(VMname);
                vm.setOS(OS);
                vm.setProvisioningDate(provisioningDate);

                virtualMachineRepository.save(vm);
            } else {
                log.info(VMname + " is already registered");
            }

        } catch (Exception e) {
            throw new MachineRegisterException("Unable to register Machine:" + VMname);
        }

    }
}
