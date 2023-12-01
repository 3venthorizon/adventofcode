package com.adventofcode.y2023;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link https://adventofcode.com/2023/day/1}
 */
public class Day1 {
   static final Pattern PATTERN_NUMBER = Pattern.compile("\\d");

   public long solvePart1(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::extractDigits)
            .mapToLong(Long::parseLong)
            .reduce(Long::sum)
            .orElse(0L);
   }

   String extractDigits(String line) {
      Matcher matcher = PATTERN_NUMBER.matcher(line);
      if (!matcher.find()) return "0";
      int firstIndex = matcher.start();
      
      matcher = PATTERN_NUMBER.matcher(new StringBuilder(line).reverse().toString());
      matcher.find();
      int lastIndex = line.length() - matcher.start() - 1;

      return line.charAt(firstIndex) + "" + line.charAt(lastIndex);
   }
}
