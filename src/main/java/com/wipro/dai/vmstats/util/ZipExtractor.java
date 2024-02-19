package com.wipro.dai.vmstats.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

@Slf4j
public class ZipExtractor {
    public static boolean extractZipFile(String zipFilePath, String extractDirectory) {
        // Validate the presence of the input zip file
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            log.error("Input zip file {} not found" , zipFilePath);
            return false;
        }

        // Create the extract directory if it doesn't exist
        Path extractPath = Paths.get(extractDirectory);
        try {
            Files.createDirectories(extractPath);
        } catch (IOException e) {
            System.err.println("Error creating extract directory: " + e.getMessage());
            return false;
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // Extract each entry in the zip file
                Path entryPath = extractPath.resolve(entry.getName());
                try {
                    if (entry.isDirectory()) {
                        // Create directories
                        Files.createDirectories(entryPath);
                    } else {
                        // Extract files
                        try (OutputStream outputStream = Files.newOutputStream(entryPath)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zipInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("Error extracting zip entry: " + entry.getName() + ", " + e.getMessage());
                    return false;
                } finally {
                    zipInputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            log.error("Error reading zip file: " + e.getMessage());
            return false;
        }

        return true;
    }
}
