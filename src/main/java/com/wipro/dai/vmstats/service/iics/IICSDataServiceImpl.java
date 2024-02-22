package com.wipro.dai.vmstats.service.iics;

import com.wipro.dai.vmstats.csv.CDIAuditDataReader;
import com.wipro.dai.vmstats.csv.IPUDataUsageReader;
import com.wipro.dai.vmstats.exception.ReportFileReadException;
import com.wipro.dai.vmstats.model.IICS.CDIAuditMeteringData;
import com.wipro.dai.vmstats.model.IICS.MeterUsage;
import com.wipro.dai.vmstats.repository.iics.CDIAuditMeterRepository;
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

    @Autowired
    CDIAuditMeterRepository cdiAuditMeterRepository;
    @Override
    public void saveIPUMeterUsageData(Path filePath) {
        try {
            System.out.println("in saveMeterUsageData, argument is:"+filePath);
            List<MeterUsage> meterUsageList= IPUDataUsageReader.readIPUMeterUsageCSV(filePath);

            meterUsageRepository.saveAll(meterUsageList);



    } catch (ReportFileReadException e) {
            log.error("Unable to insert the records for the meter usage, report file was not read.");
        }
    }

    @Override
    public void saveAuditData(Path filePath) {

        try {
            System.out.println("in saveAuditData, argument is:"+filePath);
            List<CDIAuditMeteringData> cdiAuditMeteringDataList= CDIAuditDataReader.readCDIAuditCSV(filePath);

            cdiAuditMeterRepository.saveAll(cdiAuditMeteringDataList);



        } catch (ReportFileReadException e) {
            log.error("Unable to insert the records for the meter usage, report file was not read.");
        }
    }
}
