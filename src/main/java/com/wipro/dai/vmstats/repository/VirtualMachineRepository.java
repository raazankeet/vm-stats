package com.wipro.dai.vmstats.repository;

import com.wipro.dai.vmstats.model.MemoryUsage;
import com.wipro.dai.vmstats.model.VirtualMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface    VirtualMachineRepository extends JpaRepository<VirtualMachine, Integer> {
    // Add custom query methods if needed
    VirtualMachine findByVMName(String VMName);
    Optional<VirtualMachine> findFirstByVMName(String VMName);
}
