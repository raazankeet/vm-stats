package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ConfigFileException;
import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;

public interface IICSService {

    void updateIPUUsage() throws IICSLoginException, ExportMeteringJobException, InterruptedException, ConfigFileException;
    void updateActivityLog() throws IICSLoginException, InterruptedException;
    void updateMeteringAuditData() throws IICSLoginException, InterruptedException, ConfigFileException, ExportMeteringJobException;
}
