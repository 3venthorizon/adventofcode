package com.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.stream.reader.LineReader;

public class Day8 {
   
   static final byte TREE_TALLEST = '9';

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day8.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   public long part1() throws IOException, URISyntaxException {
      byte[] forrest = growForrest();
      int length = (int) Math.sqrt(forrest.length);
      long count = 0;
      
      for (int index = 0; index < forrest.length; index++) {
         int row = index / length;
         int col = index % length;
         if (isVisible(forrest, index, row, col, length)) count++;
      }
      
      return count;
   }
   
   byte[] growForrest() throws IOException, URISyntaxException {
      StringBuilder sb = new StringBuilder(99 * 99);
      int count = 0;
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            count++;
            sb.append(line);
            line = lineReader.readLine();
         }
      }
      
      byte[] trees = sb.toString().getBytes();
      
      if (trees.length / count != count) throw new RuntimeException("Not a square forrest");
      return trees;
   }
   
   boolean isVisible(byte[] forrest, int position, int row, int col, int length) {
      int minRowCount = Math.min(row, length - row);
      int minColCount = Math.min(col, length - col);
      int middleCount = Math.min(minRowCount, minColCount);
      int command = 1 << 1;
      command += minRowCount < minColCount ? 1 : 0;
      command <<= 1;
      
      return true;
   }
   
   boolean isVisibleRow(byte[] forrest, int position, int row, int col, int length, int direction) {
      for (int index = row + direction; index >= 0 && index < length; index += direction) {
         if (forrest[position] <= forrest[index * length + col]) return false;
      }
      
      return true;
   }
   
   boolean isVisibleColumn(byte[] forrest, int position, int row, int col, int length, int direction) {
      for (int index = col + direction; index >= 0 && index < length; index += direction) {
         if (forrest[position] <= forrest[row * length + index]) return false;
      }
      
      return true;
   }
   
}
