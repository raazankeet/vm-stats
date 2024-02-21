package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.exception.ApiCallException;
import com.wipro.dai.vmstats.exception.ExportMeteringJobException;
import com.wipro.dai.vmstats.exception.IICSLoginException;
import com.wipro.dai.vmstats.exception.IICSLogoutException;
import com.wipro.dai.vmstats.model.IICS.*;
import com.wipro.dai.vmstats.util.ApiCall;
import com.wipro.dai.vmstats.util.ApiCallWithList;
import com.wipro.dai.vmstats.util.FileDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
public class IICSApiActivitiesImpl implements IICSApiActivities {

    @Value("${iics.credentials.username}")
    private String username;

    @Value("${iics.credentials.password}")
    private String password;
    @Value("${iics.urls.allorgURI}")
    private String allorgURI;
    @Value("${iics.urls.loginURI}")
    private String loginURI;
    @Value("${iics.urls.logoutURI}")
    private String logoutURI;

    @Value("${iics.urls.baseAPIURL}")
    private String baseAPIUrl;

    @Value("${iics.urls.exportMeteringDataURI}")
    private String exportMeteringDataURI;
    @Value("${iics.schedules.maxRetries}")
    private int maxRetries;
    @Value("${iics.schedules.delaySecondsBetweenRetries}")
    private long delaySecondsBetweenRetries;


    @Override
    public LoginResponse login() throws IICSLoginException {
        String loginUrl = baseAPIUrl + loginURI;
        log.info("Performing IICS login");
        log.debug("Performing IICS login: url={}, username={}", loginUrl, username);

        String method = "post";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginRequestBody loginRequestBody =
                new LoginRequestBody(username, password);

        try {
            // Make the HTTP POST request
            ResponseEntity response = ApiCall.callApi(
                    method, loginUrl, loginRequestBody, headers, LoginResponse.class);

            // Retrieve response body
            LoginResponse loginResponse = (LoginResponse) response.getBody();
            loginResponse.setHttpStatusCode(
                    HttpStatus.valueOf(response.getStatusCodeValue()));
            loginResponse.setLoginSuccess(true);

            // Return the login response
            return loginResponse;
        } catch (HttpClientErrorException ex) {
            // Handle HTTP error response
            log.error("HTTP error occurred during login: " + ex.getStatusCode() + ", "
                    + ex.getStatusText());
            LoginResponse response = new LoginResponse();
            ErrorDetails errorDetails = new ErrorDetails();

            response.setHttpStatusCode(ex.getStatusCode());
            response.setError(errorDetails);
            response.setLoginSuccess(false);
            errorDetails.setMessage(ex.getStatusText());

            return response;

        } catch (Exception ex) {
            // Handle other exceptions
            log.error("An error occurred during login: " + ex.getMessage());
            throw new IICSLoginException(
                    "An error occurred during login" + ex.getMessage());
        }
    }

    @Override
    public boolean logout(String sessionId) throws IICSLogoutException {
        String logoutURL = baseAPIUrl + logoutURI;

        log.info("Performing IICS logout");
        log.debug(
                "Performing IICS logout: url={}, username={}", logoutURL, username);

        String method = "post";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //        headers.setAccept((List<MediaType>) MediaType.APPLICATION_JSON);
        headers.add("INFA-SESSION-ID", sessionId);

        try {
            ResponseEntity response = ApiCall.callApi(
                    method, logoutURL, null, headers, LogoutResponse.class);
            log.info("Logout respose:{}", response);
            log.info("IICS logout completed.");
            return true;
        } catch (ApiCallException ex) {
            throw new IICSLogoutException("IICS Login exception:" + ex.getMessage());
        }
    }

    @Override
    public ExportMeteringJobResponse exportMeteringDataAllLinkedOrgsAcrossRegion(
            String podURL, String sessionId,
            ExportMeteringJobBody exportMeteringJobBody)
            throws IICSLoginException, ExportMeteringJobException {
        String exportURL = podURL
                + allorgURI;


                log.debug("Performing ExportMeteringDataAllLinkedOrgsAcrossRegion: " +
                                "exportURL={}, exportMeteringJobBody={}",
                        exportURL, exportMeteringJobBody);

        String method = "post";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //        headers.setAccept((List<MediaType>) MediaType.APPLICATION_JSON);
        headers.add("INFA-SESSION-ID", sessionId);

        try {
            // Make the HTTP POST request
            ResponseEntity response = ApiCall.callApi(method, exportURL,
                    exportMeteringJobBody, headers, ExportMeteringJobResponse.class);

            // Retrieve response body
            ExportMeteringJobResponse exportMeteringJobResponse =
                    (ExportMeteringJobResponse) response.getBody();

            exportMeteringJobResponse.setRequestSuccessful(true);

            return exportMeteringJobResponse;
        } catch (HttpClientErrorException ex) {
            // Handle HTTP error response
            log.error("HTTP error occurred during exportMeteringData: "
                    + ex.getStatusCode() + ", " + ex.getStatusText());
            ExportMeteringJobResponse response = new ExportMeteringJobResponse();

            response.setErrorMessage(ex.getStatusText());
            response.setRequestSuccessful(false);

            return response;

        } catch (Exception ex) {
            // Handle other exceptions
            log.error("An error occurred during ExportMeteringJobException: "
                    + ex.getMessage());
            throw new ExportMeteringJobException(
                    "An error occurred during ExportMeteringJobException"
                            + ex.getMessage());
        }
    }

    @Override
    public ExportMeteringJobResponse meteringJobStatusCheck(String podURL, String sessionId, String jobId) {

        String meteringJobStatusURL = podURL + exportMeteringDataURI +"/" + jobId;

        log.debug("Checking metering job status: meteringJobStatusURL={}", meteringJobStatusURL);

        ExportMeteringJobResponse exportMeteringJobResponse = null;
        int retries = 0;
        while (retries < maxRetries) {
            try {
                String method = "get";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("INFA-SESSION-ID", sessionId);

                // Make the HTTP GET request
                ResponseEntity  response = ApiCall.callApi(method, meteringJobStatusURL, null, headers, ExportMeteringJobResponse.class);
                exportMeteringJobResponse = (ExportMeteringJobResponse) response.getBody();

                if (exportMeteringJobResponse != null && "SUCCESS".equalsIgnoreCase(exportMeteringJobResponse.getStatus())) {
                    exportMeteringJobResponse.setRequestSuccessful(true);
                    return exportMeteringJobResponse;
                }else{
                    if (exportMeteringJobResponse != null) {
                        log.info("exportMeteringJobResponse:"+exportMeteringJobResponse.getStatus());
                    }
                }
            } catch (HttpClientErrorException ex) {
                // Handle HTTP error response
                log.warn("HTTP error occurred during checking metering job status: {}", ex.getRawStatusCode());
            } catch (Exception ex) {
                // Handle other exceptions
                log.error("An error occurred during checking metering job status: {}", ex.getMessage());
            }

            // Sleep before next retry
            try {
                Thread.sleep(delaySecondsBetweenRetries*1000);
            } catch (InterruptedException e) {
                log.warn("Sleep was interrupted");
            }

            retries++;
            log.info("Attempt {} of {}", retries, maxRetries);
        }

        // Maximum retries reached without success
        log.error("Maximum retries reached without success");
        ExportMeteringJobResponse response = new ExportMeteringJobResponse();
        response.setErrorMessage("Maximum retries reached without success");
        response.setRequestSuccessful(false);
        return response;
    }

    @Override
    public boolean DownloadMeterResponse(String podURL, String sessionId, String jobId,String meterFile, int maxRetries, long delayMillis) {
        String downloadUrl=podURL+"/public/core/v3/license/metering/ExportMeteringData/" + jobId+"/download";

        String method = "get";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("INFA-SESSION-ID", sessionId);

       if(FileDownloader.downloadFile(downloadUrl,method,null,headers,byte[].class,meterFile)) {
           System.out.println("file was downloaded.");
           return true;
       }
        return false;
    }




}

