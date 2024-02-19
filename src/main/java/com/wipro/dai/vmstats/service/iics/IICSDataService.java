package com.wipro.dai.vmstats.service.iics;

import java.nio.file.Path;

public interface IICSDataService {
    public void saveMeterUsageData(Path filePath);
}
