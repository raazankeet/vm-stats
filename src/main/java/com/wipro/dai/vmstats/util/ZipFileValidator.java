package com.wipro.dai.vmstats.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipFileValidator {
    public static boolean validateZipFile(String zipFilePath) {
        log.info("Ensuring {} is not corrupt.",zipFilePath);
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    // Skip directories
                    continue;
                }
                // Attempt to read the content of each entry to ensure it's not corrupted
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zipInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                byteArrayOutputStream.close(); // Close the ByteArrayOutputStream
            }
            log.info(zipFilePath+" is not corrupt.");
            return true; // No exceptions occurred, zip file is valid
        } catch (IOException e) {
            // An IOException occurred while reading the zip file, indicating corruption
            log.error("An IOException occurred while reading the zip file {}, indicating corruption",zipFilePath);
            return false;
        }
    }
}
