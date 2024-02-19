package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

@Data
public class ErrorDetails {
    private String code;
    private String message;
    private String requestId;
    private String details;
}
