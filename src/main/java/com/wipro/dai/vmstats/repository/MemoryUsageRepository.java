package com.wipro.dai.vmstats.repository;

import com.wipro.dai.vmstats.model.CPUUsage;
import com.wipro.dai.vmstats.model.MemoryUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryUsageRepository extends JpaRepository<MemoryUsage, Integer> {

}