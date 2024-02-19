package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;
import com.wipro.dai.vmstats.exception.IICSLogoutException;
import com.wipro.dai.vmstats.model.IICS.ExportMeteringJobBody;
import com.wipro.dai.vmstats.model.IICS.ExportMeteringJobResponse;
import com.wipro.dai.vmstats.model.IICS.LoginResponse;
import org.springframework.web.client.HttpClientErrorException;

public interface IICSApiActivities {



//    public LoginResponse login() throws IICSLoginException;
    public LoginResponse login() throws IICSLoginException, HttpClientErrorException;
    public boolean logout(String sessionId) throws HttpClientErrorException, IICSLogoutException;

    public ExportMeteringJobResponse exportMeteringDataAllLinkedOrgsAcrossRegion(String baseApiUrl,String sessionId, ExportMeteringJobBody exportMeteringJobBody) throws IICSLoginException, ExportMeteringJobException;
    public ExportMeteringJobResponse meteringJobStatus(String podURL, String sessionId, String jobId,int maxRetries,long delayMillis) throws IICSLoginException, ExportMeteringJobException, InterruptedException;
    public boolean DownloadMeterResponse(String podURL, String sessionId, String jobId,String meterFile,int maxRetries,long delayMillis);
}
