package com.wipro.dai.vmstats.repository;

import com.wipro.dai.vmstats.model.NetworkUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkUsageRepository extends JpaRepository<NetworkUsage, Integer> {
    // Add custom query methods if needed
}

