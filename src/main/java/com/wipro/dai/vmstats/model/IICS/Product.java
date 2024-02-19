package com.wipro.dai.vmstats.model.IICS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Product {
    private String name;
    private String baseApiUrl;
}
