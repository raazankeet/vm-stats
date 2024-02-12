package com.wipro.dai.vmstats.repository;

import com.wipro.dai.vmstats.model.CPUUsage;
import com.wipro.dai.vmstats.model.DiskUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskUsageRepository extends JpaRepository<DiskUsage, Integer> {
}

