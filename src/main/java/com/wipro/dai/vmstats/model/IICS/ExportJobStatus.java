package com.wipro.dai.vmstats.model.IICS;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ExportJobStatus {
    private String jobId;
    private String status;
    private String errorMessage;
    private String orgId;
    private String userId;
    private String selectedOrgId;
    private String meterId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime endDate;

    private String callbackUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
