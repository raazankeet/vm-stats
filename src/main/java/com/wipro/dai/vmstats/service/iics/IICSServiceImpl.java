package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;
import com.wipro.dai.vmstats.model.IICS.ExportMeteringJobBody;
import com.wipro.dai.vmstats.model.IICS.ExportMeteringJobResponse;
import com.wipro.dai.vmstats.model.IICS.LoginResponse;
import com.wipro.dai.vmstats.model.IICS.Product;
import com.wipro.dai.vmstats.util.FileProcessor;
import com.wipro.dai.vmstats.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class IICSServiceImpl implements IICSService{

    @Autowired
    private IICSApiActivities iicsApiActivities;

    @Autowired
    private IICSDataService iicsDataService;
    @Override
    public void updateMeterUsage() throws IICSLoginException, ExportMeteringJobException, InterruptedException {
        String meterFile = "meter_response_" + new SimpleDateFormat("dd_MM_yy_HH_mm").format(new Date()) + ".zip";
        String meterDirectory = "meter_reports/"+new SimpleDateFormat("dd_MM_yy").format(new Date())+"/";

        LoginResponse loginResponse = iicsApiActivities.login();

        if(loginResponse.isLoginSuccess()){
            System.out.println("Login successful");

            Product firstProduct =loginResponse.getProducts().stream().findFirst().orElse(null);
            String sessionId=loginResponse.getUserInfo().getSessionId();

            if (firstProduct != null && sessionId!=null) {

                String baseApiUrl = firstProduct.getBaseApiUrl();

                System.out.println("sessionId:"+sessionId);
                System.out.println("baseApiUrl:"+baseApiUrl);

//					iicsActivities.logout(sessionId);
                ExportMeteringJobBody exportMeteringJobBody=new ExportMeteringJobBody();

                exportMeteringJobBody.setCombinedMeterUsage("FALSE");

                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime startDate = currentDateTime.minusDays(170);

//					String endDateString = "2022-08-12T00:00:00Z";
//					OffsetDateTime endoffsetDateTime = OffsetDateTime.parse(endDateString);
//					LocalDateTime endDate = endoffsetDateTime.toLocalDateTime();

                LocalDateTime endDate= LocalDateTime.now(ZoneOffset.UTC);

                exportMeteringJobBody.setStartDate(startDate);
                exportMeteringJobBody.setEndDate(endDate);

                ExportMeteringJobResponse exportMeteringJobResponse = iicsApiActivities.exportMeteringDataAllLinkedOrgsAcrossRegion(baseApiUrl,sessionId,exportMeteringJobBody);

                System.out.println("Export Response:"+exportMeteringJobResponse);;


                try {
                    // Sleep for 3 seconds
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // Handle the InterruptedException if necessary
                    e.printStackTrace();
                }
                System.out.println("Finished waiting.");



                if (exportMeteringJobResponse.isRequestSuccessful()){
                    System.out.println("export job ran successful");
                    ExportMeteringJobResponse jobResponse= iicsApiActivities.meteringJobStatus(baseApiUrl,sessionId,exportMeteringJobResponse.getJobId(),10,30000);
                    System.out.println("get status request:"+jobResponse.getStatus());

                    if(iicsApiActivities.DownloadMeterResponse(baseApiUrl,sessionId, jobResponse.getJobId(),meterFile,2,1000)){
                        System.out.println("file downloaded. lets extract to..."+meterDirectory);
                        boolean meterReportExtracted = ZipExtractor.extractZipFile(meterFile,meterDirectory);

                        if(meterReportExtracted){
                            System.out.println("Meter report is extracted for the cycle.");
                            List<Path> filePaths= FileProcessor.listFilesInDirectory(meterDirectory);

                            System.out.println("filePaths:"+filePaths);
                            if (filePaths!=null){

                                filePaths.forEach(file ->iicsDataService.saveMeterUsageData(Paths.get(meterDirectory + file.getFileName())));


                            }


                        }else{
                            System.out.println("Issue extracting the meter report.");
                        }

                    }else{
                        System.out.println("File download issue");
                    }

                }


            } else {
                log.error("Unable to get baseURL after login.");
            }

        }else {
            System.out.println("Login failed.");
        }

    }
}
