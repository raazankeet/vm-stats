package com.wipro.dai.vmstats.util;

import com.wipro.dai.vmstats.exception.ApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApiCall {

    public static   <T> ResponseEntity callApi(String method, String url, Object requestBody, HttpHeaders headers, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod httpMethod = HttpMethod.resolve(method.toUpperCase());
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.exchange(url, httpMethod, requestEntity, responseType);
        } catch (HttpClientErrorException ex) {
            // Log the HTTP error and return the error response
            HttpStatus statusCode = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            log.error("HTTP error occurred: " + statusCode + ", Response body: " + responseBody);
            throw new HttpClientErrorException(statusCode,responseBody);
        } catch (Exception e) {
            // Log other exceptions and return internal server error response
            throw new ApiCallException("An error occurred while making the API call: " + e.getMessage());
        }
}}
