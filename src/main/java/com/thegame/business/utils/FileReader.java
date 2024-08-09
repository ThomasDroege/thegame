package com.thegame.business.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {
    public static String readResourceFile(String fileName) throws IOException, URISyntaxException {
        URI resource = FileReader.class.getClassLoader().getResource(fileName).toURI();
        Path path = Paths.get(resource);
        return Files.readString(path);
    }
}