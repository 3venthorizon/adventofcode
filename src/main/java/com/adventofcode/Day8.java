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
      long count = (length - 1L) * 4L; //perimeter visible
      
      for (int rowIndex = 1, rowLimit = length - 1; rowIndex < rowLimit; rowIndex++) {
         byte ltrMin = forrest[rowIndex * length];
         byte rtlMin = forrest[rowIndex * length + length - 1];
         
         for (int colIndex = 1, colLimit = length - 1; colIndex < colLimit; colIndex++) {
            byte ltr = forrest[rowIndex * length + colIndex];
            byte rtl = forrest[rowIndex * length + length - colIndex - 1];
            
            if (ltr > ltrMin) {
               ltrMin = ltr;
               count++;
            }
            
            if (rtl > rtlMin) {
               rtlMin = rtl;
               count++;
            }
            
            if (ltr == TREE_TALLEST && rtl == TREE_TALLEST) break;
         }
      }
      
      for (int colIndex = 1, colLimit = length - 1; colIndex < colLimit; colIndex++) {
         byte ttbMin = forrest[colIndex];
         byte bttMin = forrest[length * (length - 1) + colIndex];

         for (int rowIndex = 1, rowLimit = length - 1; rowIndex < rowLimit; rowIndex++) {
            byte ttb = forrest[rowIndex * length + colIndex];
            byte btt = forrest[rowIndex * (length - 1) - colIndex];
            
            if (ttb > ttbMin) {
               ttbMin = ttb;
               count++;
            }
            
            if (btt > bttMin) {
               bttMin = btt;
               count++;
            }
            
            if (ttb == TREE_TALLEST && btt == TREE_TALLEST) break;
         }
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
}
