package com.wipro.dai.vmstats.csv;

import com.wipro.dai.vmstats.exception.ReportFileReadException;
import com.wipro.dai.vmstats.model.IICS.MeterUsage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IPUDataUsageReader {

    public  static List<MeterUsage> readIPUMeterUsageCSV(Path filePath) throws ReportFileReadException {
        List<MeterUsage> meterUsageList = new ArrayList<>();
        try (Reader reader = new FileReader(filePath.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

            for (CSVRecord csvRecord : csvParser) {
                try {
                    MeterUsage meterUsage = new MeterUsage();
                    meterUsage.setOrgId(csvRecord.get("OrgId"));
                    meterUsage.setMeterId(csvRecord.get("MeterId"));
                    meterUsage.setMeterName(csvRecord.get("MeterName"));
                    meterUsage.setDate(dateFormat.parse(csvRecord.get("Date")));
                    meterUsage.setBillingPeriodStartDate(dateFormat.parse(csvRecord.get("BillingPeriodStartDate")));
                    meterUsage.setBillingPeriodEndDate(dateFormat.parse(csvRecord.get("BillingPeriodEndDate")));
                    meterUsage.setMeterUsage(new BigDecimal(csvRecord.get("MeterUsage")));
                    meterUsage.setIPU(new BigDecimal(csvRecord.get("IPU")));
                    meterUsage.setScalar(csvRecord.get("Scalar"));
                    meterUsage.setMetricCategory(csvRecord.get("MetricCategory"));
                    meterUsage.setOrgName(csvRecord.get("OrgName"));
                    meterUsage.setOrgType(csvRecord.get("OrgType"));
                    meterUsage.setIPURate(new BigDecimal(csvRecord.get("IPURate")));

                    meterUsageList.add(meterUsage);
                } catch (ParseException e) {
                    log.error("Error parsing (ParseException) date in CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                    throw new ReportFileReadException("Error parsing (ParseException) date in CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    log.error("Error processing (IllegalArgumentException) CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                    throw new ReportFileReadException("Error processing (IllegalArgumentException) CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                }
            }
        } catch (IOException | RuntimeException e) {
            log.error("Error reading CSV file: " + e.getMessage());
            throw new ReportFileReadException("Error reading CSV file: " + e.getMessage());
        }
        return meterUsageList;
    }
}
