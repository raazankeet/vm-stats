package com.wipro.dai.vmstats.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Slf4j
public class FileDownloader {

    public static boolean downloadFile(String url, String method, Object requestBody,
                                       HttpHeaders headers, Class<byte[]> responseType, String destinationFilePath) {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.resolve(method.toUpperCase());

        log.info("Downloading from url {}, saving to {}",url,destinationFilePath);

        try {
            // Make the HTTP request with the specified method, headers, and request body
            HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    responseType
            );

            // Check if response is successful (HTTP status code 200)
            if (response.getStatusCode() == HttpStatus.OK) {
                byte[] responseBody = response.getBody();
                // Save the file to the specified destination
                if (responseBody != null) {
                    int responseBodySize = responseBody.length;
                    log.info("Response body size: {}" , responseBodySize );
                    saveFile(response.getBody(), destinationFilePath);
                    log.info("File {} was downloaded successfully.",destinationFilePath);
                } else {
                    log.warn("Response body is empty");
                    return false;
                }



                return true;
            } else {
                System.out.println("Failed to download file. HTTP status code: " + response.getStatusCodeValue());
                return false;
            }
        } catch (Exception e) {
            log.error("An error occurred while downloading the file: " + e.getMessage());
            return false;
        }
    }

    private static void saveFile(byte[] content, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content);
    }
}
