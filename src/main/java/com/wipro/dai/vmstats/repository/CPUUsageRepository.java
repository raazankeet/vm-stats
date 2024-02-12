package com.wipro.dai.vmstats.repository;

import com.wipro.dai.vmstats.model.CPUUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CPUUsageRepository extends JpaRepository<CPUUsage, Integer> {
}