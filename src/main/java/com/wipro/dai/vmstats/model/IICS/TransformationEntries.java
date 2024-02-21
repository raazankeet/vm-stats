package com.wipro.dai.vmstats.model.IICS;

import lombok.Data;

@Data
public class TransformationEntries {
    private String id;
    private String txName;
    private String txType;
    private int successRows;
    private int failedRows;
    private String txInstanceName;

    // Getters and setters
}
