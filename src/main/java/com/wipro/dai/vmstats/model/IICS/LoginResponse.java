package com.wipro.dai.vmstats.model.IICS;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponse {
    @Getter
    private List<Product> products;
    private UserInfo userInfo;
    private ErrorDetails error;
    private HttpStatus httpStatusCode;
    private boolean loginSuccess;

}
