package com.wipro.dai.vmstats.csv;

import com.wipro.dai.vmstats.exception.ReportFileReadException;
import com.wipro.dai.vmstats.model.IICS.CDIAuditMeteringData;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CDIAuditDataReader {

    public  static List<CDIAuditMeteringData> readCDIAuditCSV(Path filePath) throws ReportFileReadException {
        List<CDIAuditMeteringData> cdiAuditMeteringDataList = new ArrayList<>();

        try (Reader reader = new FileReader(filePath.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

            for (CSVRecord csvRecord : csvParser) {
                try {
                    CDIAuditMeteringData cdiAuditMeteringData = new CDIAuditMeteringData();
                    cdiAuditMeteringData.setTaskId(csvRecord.get("Task ID"));
                    cdiAuditMeteringData.setTaskName(csvRecord.get("Task Name"));
                    cdiAuditMeteringData.setTaskObjectName(csvRecord.get("Task Object Name"));
                    cdiAuditMeteringData.setTaskType(csvRecord.get("Task Type"));
                    cdiAuditMeteringData.setTaskRunId(Integer.parseInt(csvRecord.get("Task Run ID")));
                    cdiAuditMeteringData.setProjectName(csvRecord.get("Project Name"));
                    cdiAuditMeteringData.setFolderName(csvRecord.get("Folder Name"));
                    cdiAuditMeteringData.setOrgId(csvRecord.get("Org ID"));
                    cdiAuditMeteringData.setEnvironmentId(csvRecord.get("Environment ID"));
                    cdiAuditMeteringData.setEnvironment(csvRecord.get("Environment"));
                    cdiAuditMeteringData.setCoresUsed(Double.parseDouble(csvRecord.get("Cores Used")));
                    cdiAuditMeteringData.setStartTime(LocalDateTime.parse(csvRecord.get("Start Time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
                    cdiAuditMeteringData.setEndTime(LocalDateTime.parse(csvRecord.get("End Time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
                    cdiAuditMeteringData.setStatus(csvRecord.get("Status"));
                    cdiAuditMeteringData.setMeteredValue(Double.parseDouble(csvRecord.get("Metered Value")));
                    cdiAuditMeteringData.setAuditTime(LocalDateTime.parse(csvRecord.get("Audit Time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
                    cdiAuditMeteringData.setObmTaskTimeSeconds(Integer.parseInt(csvRecord.get("OBM Task Time(s)")));

                    cdiAuditMeteringDataList.add(cdiAuditMeteringData);
                } catch (NumberFormatException | DateTimeParseException e) {
                    log.error("Error processing (NumberFormatException | DateTimeParseException) CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                    throw new ReportFileReadException("Error processing (NumberFormatException | DateTimeParseException) CSV record at line " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                }
            }
        } catch (IOException | RuntimeException e) {
            log.error("Error reading CSV file: " + e.getMessage());
            throw new ReportFileReadException("Error reading CSV file: " + e.getMessage());
        }
        return cdiAuditMeteringDataList;
    }
}
