package com.wipro.dai.vmstats.repository.iics;

import com.wipro.dai.vmstats.model.IICS.MeterUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterUsageRepository extends JpaRepository<MeterUsage, Integer> {
}