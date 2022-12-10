package com.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.opencsv.stream.reader.LineReader;

public class Day7 {
   
   static final String CD_UP = "$ cd ..";
   static final String CD = "$ cd ";
   static final String LS = "$ ls";
   static final String DIR = "dir";
   static final String EMPTY = "";
   static final String SPACE = " ";
   static final String SEPERATOR = "/";
   
   static class Directory {
      final String name;
      final Directory parent;
      final List<Directory> subdirs = new ArrayList<>();
      final Map<String, Long> fileSizeMap = new HashMap<>();

      Directory(Directory parent, String name) {
         this.name = name;
         this.parent = parent;
         if (parent != null) parent.subdirs.add(this);
      }
      
      @Override
      public int hashCode() {
         return Objects.hash(name, parent);
      }
      
      @Override
      public boolean equals(Object object) {
         if (this == object) return true;
         if (object == null) return false;
         if (getClass() != object.getClass()) return false;
         Directory other = (Directory) object;
         return Objects.equals(this.name, other.name) && Objects.equals(this.parent, other.parent);
      }

      @Override
      public String toString() {
         return parent == null ? name : (parent.toString() + SEPERATOR + name);
      }
   }

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day7.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   public long part1() throws IOException, URISyntaxException {
      Set<Directory> directories = build();
      
      return directories.stream()
            .map(this::calculateSize)
            .filter(size -> size < 100000)
            .reduce(0L, Math::addExact);
   }
   
   public long part2() throws IOException, URISyntaxException {
      Set<Directory> directories = build();
      Directory root = directories.iterator().next();
      //get root
      while (root.parent != null) {
         root = root.parent;
      }
      
      long available = 70_000_000L - calculateSize(root);
      long required = 30_000_000L - available;
      
      return directories.stream()
            .mapToLong(this::calculateSize)
            .filter(size -> size > required)
            .min()
            .orElse(0L);
   }
   
   Set<Directory> build() throws IOException, URISyntaxException {
      Directory current = null;
      Set<Directory> directories = new HashSet<>();
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            current = build(current, line);
            directories.add(current);
            line = lineReader.readLine();
         }
      }
      
      return directories;
   }
   
   Directory build(Directory current, String line) {
      if (line.startsWith(CD_UP)) return current.parent;
      if (line.startsWith(CD)) return changeDirectory(current, line);
      if (line.startsWith(LS)) return current;
      if (line.startsWith(DIR)) return dir(current, line.replace(DIR, EMPTY).trim()).parent;
      
      String[] sizeFile = line.split(SPACE);
      if (sizeFile.length < 2) return current;
      
      current.fileSizeMap.put(sizeFile[1].trim(), Long.parseLong(sizeFile[0].trim()));
      
      return current;
   }
   
   Directory changeDirectory(Directory current, String line) {
      String name = line.replace(CD, EMPTY).trim();
      if (current == null) return new Directory(current, name);
      return dir(current, name);
   }
   
   Directory dir(Directory current, String name) {
      return current.subdirs.stream()
            .filter(subdir -> Objects.equals(name, subdir.name))
            .findFirst()
            .orElseGet(() -> new Directory(current, name));
   }
   
   long calculateSize(Directory directory) {
      long size = directory.subdirs.stream()
            .mapToLong(this::calculateSize)
            .reduce(0L, Math::addExact);
      return directory.fileSizeMap.values().stream().mapToLong(Long::longValue).reduce(size, Math::addExact);
   }
}
