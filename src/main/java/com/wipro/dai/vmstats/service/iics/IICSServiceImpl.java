package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ApiResponseException;
import com.wipro.dai.vmstats.exception.ConfigFileException;
import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;
import com.wipro.dai.vmstats.model.IICS.*;
import com.wipro.dai.vmstats.repository.iics.ActivityLogEntryRepository;
import com.wipro.dai.vmstats.util.ApiCall;
import com.wipro.dai.vmstats.util.FileProcessor;
import com.wipro.dai.vmstats.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    @Value("${iics.schedules.reportsFromLastNDays}")
    private String reportsFromLastNDays;

    @Value("${iics.schedules.activityLogRowLimit}")
    private String activityLogRowLimit;

    @Value("${iics.schedules.jobLevelDataMeterid}")
    private String jobLevelDataMeterid;



    @Autowired
    private IICSApiActivities iicsApiActivities;

    @Autowired
    private IICSDataService iicsDataService;
    @Autowired
    ActivityLogEntryRepository activityLogEntryRepository;
    @Override
    public void updateIPUUsage() throws IICSLoginException, ExportMeteringJobException, InterruptedException, ConfigFileException {
        String meterFile = "meter_response_" + new SimpleDateFormat("dd_MM_yy_HH_mm").format(new Date()) + ".zip";
        String meterDirectory = "meter_reports/"+new SimpleDateFormat("dd_MM_yy").format(new Date())+"/";

        LoginResponse loginResponse = iicsApiActivities.login();

        if(loginResponse.isLoginSuccess()){
            System.out.println("Login successful");

            Product firstProduct =loginResponse.getProducts().stream().findFirst().orElse(null);
            String sessionId=loginResponse.getUserInfo().getSessionId();

            if (firstProduct != null && sessionId!=null) {

                String baseApiUrl = firstProduct.getBaseApiUrl();

                System.out.println("sessionId:" + sessionId);
                System.out.println("baseApiUrl:" + baseApiUrl);

                MeteringJobBody meteringJobBody = new MeteringJobBody();

                meteringJobBody.setCombinedMeterUsage("FALSE");

                LocalDateTime currentDateTime = LocalDateTime.now();
                long reportsFromLastNDayss;
                try {
                    reportsFromLastNDayss = Long.parseLong(reportsFromLastNDays);
                } catch (NumberFormatException e) {

                    log.error("Bad property [reportsFromLastNDayss:{}] in the configuration file.", reportsFromLastNDays);
                    throw new ConfigFileException("Bad property reportsFromLastNDayss:{} in the configuration file.", reportsFromLastNDays);

                }

                LocalDateTime startDate = currentDateTime.minusDays(reportsFromLastNDayss);

                LocalDateTime endDate = LocalDateTime.now(ZoneOffset.UTC);

                meteringJobBody.setStartDate(startDate);
                meteringJobBody.setEndDate(endDate);
                log.debug("Metering job body for IPU:"+meteringJobBody);
                System.out.println("Metering job body for IPU:"+meteringJobBody);

                ExportMeteringJobResponse exportMeteringJobResponse = iicsApiActivities.exportMeteringDataAllLinkedOrgsAcrossRegion(baseApiUrl, sessionId, meteringJobBody);

                if (exportMeteringJobResponse.isRequestSuccessful()) {
                    log.info("Export job ran successfully.");
                    ExportMeteringJobResponse jobResponse = iicsApiActivities.meteringJobStatusCheck(baseApiUrl, sessionId, exportMeteringJobResponse.getJobId());
                    if (jobResponse.isRequestSuccessful()) {
                        if (iicsApiActivities.DownloadMeterResponse(baseApiUrl, sessionId, jobResponse.getJobId(), meterFile, 2, 1000)) {
                            log.debug("Meter response file was downloaded.");
                            boolean meterReportExtracted = ZipExtractor.extractZipFile(meterFile, meterDirectory);

                            if (meterReportExtracted) {
                                List<Path> filePaths = FileProcessor.listFilesInDirectory(meterDirectory);
                                log.debug("List of files to process:"+filePaths);
                                if (filePaths != null) {

                                    filePaths.forEach(file -> iicsDataService.saveIPUMeterUsageData(Paths.get(meterDirectory + file.getFileName())));


                                }else{
                                    log.warn("No files were found to be processed :"+filePaths);
                                }


                            } else {
                                log.error("Issue extracting the meter report.");
                            }

                        } else {
                            log.error("Metering file download issue");
                        }

                    }else {
                        log.warn("Meter job did not finish during the time window specified.");
                    }


                }


            } else {
                log.error("Unable to get baseURL after login.");
            }

        }else {
            System.out.println("Login failed.");
        }

    }

    @Override
    public void updateActivityLog() throws IICSLoginException {

        LoginResponse loginResponse = iicsApiActivities.login();

        if(loginResponse.isLoginSuccess()){
            System.out.println("Login successful");

            Product firstProduct =loginResponse.getProducts().stream().findFirst().orElse(null);
            String sessionId=loginResponse.getUserInfo().getSessionId();

            if (firstProduct != null && sessionId!=null) {

                String baseApiUrl = firstProduct.getBaseApiUrl();
                String apiUrl = baseApiUrl+"/api/v2/activity/activityLog?rowLimit="+activityLogRowLimit;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("icSessionId", sessionId);
                String method="get";
                Class<ActivityLogEntry[]> responseType = ActivityLogEntry[].class;
                ActivityLogEntry[] response = ApiCall.callApiwithList(method,apiUrl,null,headers,responseType);

                if (response != null) {
                    for (ActivityLogEntry dto : response) {
                        ActivityLogDTO tableRow = mapDtoToEntity(dto);
                        activityLogEntryRepository.save(tableRow);

                    }
                }else{
                    log.error("No response was found after api call to activity log.");
                    throw new ApiResponseException("No response was found after api call to activity log.");
                }


            } else {
                log.error("Unable to get baseURL after login.");
            }

        }else {
            log.error("Login failed.");
        }
    }

    private ActivityLogDTO mapDtoToEntity(ActivityLogEntry dto) {
        ActivityLogDTO tableRow = new ActivityLogDTO();

        tableRow.setId(dto.getId());
        tableRow.setAgentId(dto.getEntries().get(0).getAgentId());
        tableRow.setConsumedComputeUnits(dto.getEntries().get(0).getLogEntryItemAttrs().getCONSUMED_COMPUTE_UNITS());
        tableRow.setActualExecutionTime(dto.getEntries().get(0).getLogEntryItemAttrs().getActualExecutionTime());
        tableRow.setEndTime(dto.getEndTime());
        tableRow.setEndTimeUtc(dto.getEndTimeUtc());
        tableRow.setErrorCode(dto.getEntries().get(0).getLogEntryItemAttrs().getERROR_CODE());
        tableRow.setErrorMsg(dto.getEntries().get(0).getErrorMsg());
        tableRow.setFailedSourceRows(dto.getFailedSourceRows());
        tableRow.setFailedTargetRows(dto.getFailedTargetRows());
        tableRow.setIsServerLess(dto.getEntries().get(0).getLogEntryItemAttrs().getIS_SERVER_LESS());
        tableRow.setLogFileName(dto.getEntries().get(0).getLogEntryItemAttrs().getSessionLogFileName());
        tableRow.setObjectId(dto.getObjectId());
        tableRow.setObjectName(dto.getObjectName());
        tableRow.setRunId(dto.getRunId());
        tableRow.setRuntimeEnvironmentId(dto.getEntries().get(0).getRuntimeEnvironmentId());
        tableRow.setServiceType(dto.getEntries().get(0).getLogEntryItemAttrs().getServiceType());
        tableRow.setStartTime(dto.getStartTime());
        tableRow.setStartTimeUtc(dto.getStartTimeUtc());
        tableRow.setStartedBy(dto.getStartedBy());
        tableRow.setState(dto.getState());
        tableRow.setSuccessSourceRows(dto.getSuccessSourceRows());
        tableRow.setSuccessTargetRows(dto.getSuccessTargetRows());
        tableRow.setType(dto.getType());

        return tableRow;
    }

    @Override
    public void updateMeteringAuditData() throws IICSLoginException, InterruptedException, ConfigFileException, ExportMeteringJobException {

        String auditFile = "audit_response_" + new SimpleDateFormat("dd_MM_yy_HH_mm").format(new Date()) + ".zip";
        String auditDirectory = "audit_reports/"+new SimpleDateFormat("dd_MM_yy").format(new Date())+"/";

        LoginResponse loginResponse = iicsApiActivities.login();

        if(loginResponse.isLoginSuccess()){
            System.out.println("Login successful");

            Product firstProduct =loginResponse.getProducts().stream().findFirst().orElse(null);
            String sessionId=loginResponse.getUserInfo().getSessionId();

            if (firstProduct != null && sessionId!=null) {

                String baseApiUrl = firstProduct.getBaseApiUrl();

                System.out.println("sessionId:" + sessionId);
                System.out.println("baseApiUrl:" + baseApiUrl);

                MeteringJobBody meteringJobBody = new MeteringJobBody();

                meteringJobBody.setAllMeters("FALSE");

                LocalDateTime currentDateTime = LocalDateTime.now();

                long reportsFromLastNDayss;
                try {
                    reportsFromLastNDayss = Long.parseLong(reportsFromLastNDays);
                } catch (NumberFormatException e) {

                    log.error("Bad property [reportsFromLastNDayss:{}] in the configuration file.", reportsFromLastNDays);
                    throw new ConfigFileException("Bad property reportsFromLastNDayss:{} in the configuration file.", reportsFromLastNDays);

                }

                LocalDateTime startDate = currentDateTime.minusDays(reportsFromLastNDayss);

                LocalDateTime endDate = LocalDateTime.now(ZoneOffset.UTC);

                meteringJobBody.setStartDate(startDate);
                meteringJobBody.setEndDate(endDate);
                meteringJobBody.setMeterId(jobLevelDataMeterid);

                log.debug("Metering job body for Audit Data:"+meteringJobBody);
                System.out.println("Metering job body for Audit Data:"+meteringJobBody);

                ExportMeteringJobResponse exportMeteringJobResponse = iicsApiActivities.exportServiceJobLevelMeteringData(baseApiUrl, sessionId, meteringJobBody);

                if (exportMeteringJobResponse.isRequestSuccessful()) {
                    log.info("Export job ran successfully.");
                    ExportMeteringJobResponse jobResponse = iicsApiActivities.meteringJobStatusCheck(baseApiUrl, sessionId, exportMeteringJobResponse.getJobId());
                    if (jobResponse.isRequestSuccessful()) {
                        if (iicsApiActivities.DownloadMeterResponse(baseApiUrl, sessionId, jobResponse.getJobId(), auditFile, 2, 1000)) {
                            log.debug("Meter response file was downloaded.");
                            boolean meterReportExtracted = ZipExtractor.extractZipFile(auditFile, auditDirectory);

                            if (meterReportExtracted) {
                                List<Path> filePaths = FileProcessor.listFilesInDirectory(auditDirectory);
                                log.debug("List of files to process:"+filePaths);
                                if (filePaths != null) {

                                    filePaths.forEach(file -> iicsDataService.saveAuditData(Paths.get(auditDirectory + file.getFileName())));


                                }else{
                                    log.warn("No files were found to be processed :"+filePaths);
                                }


                            } else {
                                log.error("Issue extracting the meter report.");
                            }

                        } else {
                            log.error("Metering file download issue");
                        }

                    }else {
                        log.warn("Meter job did not finish during the time window specified.");
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
