package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.csv.MeterUsageReader;
import com.wipro.dai.vmstats.exception.ReportFileReadException;
import com.wipro.dai.vmstats.model.IICS.MeterUsage;
import com.wipro.dai.vmstats.repository.iics.MeterUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class IICSDataServiceImpl implements IICSDataService{

    @Autowired
    MeterUsageRepository meterUsageRepository;
    @Override
    public void saveMeterUsageData(Path filePath) {
        try {
            System.out.println("in saveMeterUsageData, argument is:"+filePath);
            List<MeterUsage> meterUsageList= MeterUsageReader.readMeterUsageCSV(filePath);

            meterUsageRepository.saveAll(meterUsageList);



    } catch (ReportFileReadException e) {
            log.error("Unable to insert the records for the meter usage, report file was not read.");
        }
    }}
