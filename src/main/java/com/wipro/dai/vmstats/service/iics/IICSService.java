package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;

public interface IICSService {

    void  updateMeterUsage() throws IICSLoginException, ExportMeteringJobException, InterruptedException;
}
