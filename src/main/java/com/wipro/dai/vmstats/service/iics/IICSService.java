package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ConfigFileException;
import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;
import com.wipro.dai.vmstats.model.IICS.ActivityLogDTO;

public interface IICSService {

    void  updateMeterUsage() throws IICSLoginException, ExportMeteringJobException, InterruptedException, ConfigFileException;
    void updateActivityLog() throws IICSLoginException, InterruptedException;
}
