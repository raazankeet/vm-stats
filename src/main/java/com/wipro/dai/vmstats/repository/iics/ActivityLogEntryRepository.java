package com.wipro.dai.vmstats.repository.iics;

import com.wipro.dai.vmstats.model.IICS.ActivityLogDTO;
import com.wipro.dai.vmstats.model.IICS.ActivityLogEntry;
import com.wipro.dai.vmstats.model.IICS.ActivityTableRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogEntryRepository extends JpaRepository<ActivityLogDTO, String> {
}
