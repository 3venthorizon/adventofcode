package com.adventofcode.y2023;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link https://adventofcode.com/2023/day/3}
 */
public class Day3Part1 {
   static class PartAccumulator {
      String previousLine = null;
      String currentLine = null;
      long partSum = 0L;
   }

   static final Pattern PATTERN_NUMBER = Pattern.compile("\\d+");
   static final Pattern PATTERN_SYMBOL = Pattern.compile("[^\\d.]");

   public long part1(String fileName) {
      PartAccumulator accumulator = FileLineStreamer.read(fileName)
            .collect(PartAccumulator::new, this::accumulate, this::combine);
      return accumulator.partSum;
   }

   void accumulate(PartAccumulator accumulator, String line) {
      accumulator.previousLine = accumulator.currentLine;
      accumulator.currentLine = line;

      accumulator.partSum += sumPartMatches(accumulator.currentLine, accumulator.currentLine);
      accumulator.partSum += sumPartMatches(accumulator.currentLine, accumulator.previousLine);
      accumulator.partSum += sumPartMatches(accumulator.previousLine, accumulator.currentLine);
   }

   long sumPartMatches(String numberLine, String symbolLine) {
      long sum = 0L;
      if (numberLine == null || symbolLine == null) return sum;
      Matcher numberMatcher = PATTERN_NUMBER.matcher(numberLine);
      
      while (numberMatcher.find()) {
         int start = Math.max(0, numberMatcher.start() - 1);
         int end = Math.min(symbolLine.length(), numberMatcher.end() + 1);

         if (PATTERN_SYMBOL.asPredicate().test(symbolLine.substring(start, end))) {
            sum += Long.parseLong(numberMatcher.group());
         }
      }

      return sum;
   }

   PartAccumulator combine(PartAccumulator left, PartAccumulator right) {
      PartAccumulator combined = new PartAccumulator();
      combined.partSum = left.partSum + right.partSum;

      return combined;
   }
}
