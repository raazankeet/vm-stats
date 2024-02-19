package com.wipro.dai.vmstats.model.IICS;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestBody {
    private String username;
    private String password;
}
