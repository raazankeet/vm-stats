package com.wipro.dai.vmstats.model.IICS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserInfo {
    private String sessionId;
    private String id;
    private String name;
    private String parentOrgId;
    private String orgId;
    private String orgName;
    private String status;
}
