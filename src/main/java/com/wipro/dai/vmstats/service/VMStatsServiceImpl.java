package com.wipro.dai.vmstats.service;

import com.wipro.dai.vmstats.model.VMStatsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

@Slf4j
@Service
public class VMStatsServiceImpl implements VMStatsService {

    @Override
    public VMStatsData getVMStats() throws InterruptedException, IOException, URISyntaxException {
        // Command to execute the script based on the operating system

        List<String> commandArgs = null;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            // For Windows, use PowerShell
            InputStream inputStream = getClass().getResourceAsStream("/windows.ps1");
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder scriptContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        scriptContent.append(line).append(System.lineSeparator());
                    }
                    String script = scriptContent.toString();
                    commandArgs = Arrays.asList(
                            "powershell.exe",
                            "-ExecutionPolicy",
                            "Bypass",
                            "-Command",
                            "& {" + script + "}"
                    );

                } catch (IOException e) {

                    log.error("IO Exception was found:" + e);
                }
            } else {
                // Handle the case when the resource is not found
                System.err.println("Windows PowerShell script not found.");
            }

        } else {
            // For other operating systems, assume it's Unix-like and use bash
            URL resourceUrl = getClass().getResource("/linux.sh");
            System.out.println("lin res url:"+resourceUrl);
            if (resourceUrl != null) {
                Path tempScript = Files.createTempFile("linux", ".sh");
                try (InputStream inputStream = resourceUrl.openStream();
                     OutputStream outputStream = Files.newOutputStream(tempScript)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                // Make the script executable
                tempScript.toFile().setExecutable(true);

                // Construct the command to execute the shell script
                commandArgs = Collections.singletonList(tempScript.toString());
            }
        }


        // Create a VMStatsData object to store parsed data from script output
        VMStatsData vmStats = new VMStatsData();

        // Create ProcessBuilder instance
        ProcessBuilder processBuilder = new ProcessBuilder(commandArgs);

        // Merge error stream with input stream
        processBuilder.redirectErrorStream(true);

        // Start the script process
        Process process = processBuilder.start();

        // Read the output of the script
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;


            while ((line = reader.readLine()) != null) {

                String[] keyValue = line.split(": ");

                // Ensure the line is in the expected format (key: value)
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    // Update VMStatsData fields based on the parsed data
                    switch (key) {
                        case "machineName":
                            vmStats.setMachineName(value);
                            break;
                        case "cpuUtilization":
                            vmStats.setCpuUtilization(Float.parseFloat(value));
                            break;
                        case "networkIn":
                            vmStats.setNetworkIn(Float.parseFloat(value));
                            break;
                        case "totalDiskSize":
                            vmStats.setTotalDiskSize(Long.parseLong(value));
                            break;
                        case "freeDiskSpace":
                            vmStats.setFreeDiskSpace(Long.parseLong(value));
                            break;
                        case "totalPhysicalMemory":
                            vmStats.setTotalPhysicalMemory(Long.parseLong(value));
                            break;
                        case "availablePhysicalMemory":
                            vmStats.setAvailablePhysicalMemory(Long.parseLong(value));
                            break;
                        case "networkOut":
                            vmStats.setNetworkOut(Float.parseFloat(value));
                            break;
                        default:
                            // Ignore unrecognized keys
                            break;
                    }
                }
            }
        }

        // Wait for script execution to complete
        int exitCode = process.waitFor();

        // Check the exit code
        if (exitCode != 0) {
            throw new IOException("Script exited with non-zero exit code: " + exitCode);
        }

        // Log the parsed data
        log.debug("VM Stats: {}", vmStats);

        // Return the parsed data
        return vmStats;
    }
}
