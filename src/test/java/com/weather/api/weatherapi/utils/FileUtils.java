package com.weather.api.weatherapi.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static String readFileAsString(String filePath) throws IOException {
        Path path = Path.of(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
