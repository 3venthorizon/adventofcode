package com.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.stream.reader.LineReader;

public class Day7 {
   
   
   
   static class Directory {
      String name;
      Directory parent;
      List<Directory> subdirs = new ArrayList<>();
      List<Long> fileSizes = new ArrayList<>();
   }

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day5.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   void part1() throws IOException, URISyntaxException {
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
      }
   }
   
   void build(String line) {
      if (line.startsWith(""))
   }
   
   long calculateSize(Directory directory) {
      long size = directory.subdirs.stream()
            .mapToLong(this::calculateSize)
            .reduce(0L, Math::addExact);
      return directory.fileSizes.stream().mapToLong(Long::longValue).reduce(size, Math::addExact);
   }
}
