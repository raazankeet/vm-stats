package com.wipro.dai.vmstats.service.iics;

import java.nio.file.Path;

public interface IICSDataService {
    public void saveIPUMeterUsageData(Path filePath);
    public void saveAuditData(Path filePath);
}
