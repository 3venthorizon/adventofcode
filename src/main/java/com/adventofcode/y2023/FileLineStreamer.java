package com.adventofcode.y2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class FileLineStreamer {
   private FileLineStreamer() {
   }

   public static Stream<String> read(String fileName) {
      ClassLoader classLoader = FileLineStreamer.class.getClassLoader();
      URL resource = classLoader.getResource(fileName);

      try {
         return Files.newBufferedReader(Paths.get(resource.toURI())).lines();
      } catch (IOException | URISyntaxException exception) {
         throw new RuntimeException(exception);
      }
   }
}
