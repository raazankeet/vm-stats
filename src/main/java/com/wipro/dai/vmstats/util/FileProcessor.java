package com.wipro.dai.vmstats.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Slf4j
public class FileProcessor {
    public static List<Path> listFilesInDirectory(String directoryPath) {
        List<Path> fileList = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get(directoryPath))) {
            fileList = paths.collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Unable to list all the files in the given directory {}",directoryPath);
            return null;
        }
        return fileList;
    }
}
