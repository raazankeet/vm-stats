package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.model.VMStatsData;

import java.io.IOException;
import java.net.URISyntaxException;

public interface VMStatsService {
    VMStatsData getVMStats() throws InterruptedException, IOException, URISyntaxException;
}
