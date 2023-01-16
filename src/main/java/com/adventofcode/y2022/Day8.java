package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.stream.reader.LineReader;

public class Day8 {
   
   static final byte TREE_TALLEST = '9';

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day8.input");
      return Files.newBufferedReader(Paths.get(resource.toURI()));
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
   
   boolean isVisible(byte[] forrest, int position, int row, int col, int length) {
      int minRowCount = Math.min(row, length - row);
      int minColCount = Math.min(col, length - col);
      
      if (minRowCount < minColCount) {
         if (isVisibleRow(forrest, position, row, col, length, row == minRowCount ? -1 : 1)) return true;
         if (isVisibleCol(forrest, position, row, col, length, col == minColCount ? -1 : 1)) return true;
         if (isVisibleCol(forrest, position, row, col, length, col == minColCount ? 1 : -1)) return true;
         if (isVisibleRow(forrest, position, row, col, length, row == minRowCount ? 1 : -1)) return true;
      } else {
         if (isVisibleCol(forrest, position, row, col, length, col == minColCount ? -1 : 1)) return true;
         if (isVisibleRow(forrest, position, row, col, length, row == minRowCount ? -1 : 1)) return true;
         if (isVisibleRow(forrest, position, row, col, length, row == minRowCount ? 1 : -1)) return true;
         if (isVisibleCol(forrest, position, row, col, length, col == minColCount ? 1 : -1)) return true;
      }
      
      return false;
   }
   
   boolean isVisibleRow(byte[] forrest, int position, int row, int col, int length, int direction) {
      for (int index = row + direction; index >= 0 && index < length; index += direction) {
         if (forrest[position] <= forrest[index * length + col]) return false;
      }
      
      return true;
   }
   
   boolean isVisibleCol(byte[] forrest, int position, int row, int col, int length, int direction) {
      for (int index = col + direction; index >= 0 && index < length; index += direction) {
         if (forrest[position] <= forrest[row * length + index]) return false;
      }
      
      return true;
   }

   public int part2() throws IOException, URISyntaxException {
      byte[] forrest = growForrest();
      int length = (int) Math.sqrt(forrest.length);
      int max = -1;
      
      for (int index = 0; index < forrest.length; index++) {
         int row = index / length;
         int col = index % length;
         
         max = Math.max(max, scenicScore(forrest, index, row, col, length));
      }
      
      return max;
   }
   
   int scenicScore(byte[] forrest, int position, int row, int col, int length) {
      int score = 1;
      int west = scoreRow(forrest, position, row, col, length, -1);
      int east = scoreRow(forrest, position, row, col, length, 1);
      int north = scoreCol(forrest, position, row, col, length, -1);
      int south = scoreCol(forrest, position, row, col, length, 1);
      
      if (west > 0) score *= west;
      if (east > 0) score *= east;
      if (north > 0) score *= north;
      if (south > 0) score *= south;
      
      return score;
   }
   
   int scoreRow(byte[] forrest, int position, int row, int col, int length, int direction) {
      int score = 0;
      
      for (int index = row + direction; index >= 0 && index < length; index += direction) {
         score++;
         if (forrest[position] <= forrest[index * length + col]) return score;
      }
      
      return score;
   }
   
   int scoreCol(byte[] forrest, int position, int row, int col, int length, int direction) {
      int score = 0;
      
      for (int index = col + direction; index >= 0 && index < length; index += direction) {
         score++;
         if (forrest[position] <= forrest[row * length + index]) return score;
      }
      
      return score;
   }
}
