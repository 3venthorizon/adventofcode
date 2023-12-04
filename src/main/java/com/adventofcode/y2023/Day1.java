package com.adventofcode.y2023;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link https://adventofcode.com/2023/day/1}
 */
public class Day1 {
   static final Pattern PATTERN_DIGIT = Pattern.compile("\\d");
   static final Pattern PATTERN_NUMBER = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");

   public long part1(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(PATTERN_DIGIT::matcher)
            .map(this::extractDigits)
            .mapToLong(Long::parseLong)
            .reduce(Long::sum)
            .orElse(0L);
   }

   public long part2(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(PATTERN_NUMBER::matcher)
            .map(this::extractDigits)
            .map(this::replaceDigits)
            .mapToLong(Long::parseLong)
            .reduce(Long::sum)
            .orElse(0L);
   }

   String replaceDigits(String line) {
      return line.replace("one", "1")
            .replace("two", "2")
            .replace("three", "3")
            .replace("four", "4")
            .replace("five", "5")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8")
            .replace("nine", "9");
   }

   String extractDigits(Matcher matcher) {
      if (!matcher.find()) return "0";
      int index = matcher.start();
      String firstDigit = matcher.group();
      String lastDigit = firstDigit;

      while (matcher.find(index + 1)) {
         index = matcher.start();
         lastDigit = matcher.group();
      }

      return firstDigit + lastDigit;
   }
}
