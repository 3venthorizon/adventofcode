package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link https://adventofcode.com/2023/day/3}
 */
public class Day3Part2 {
   static class PartAccumulator {
      String previousLine = null;
      String currentLine = null;
      String nextLine = null;
      long partSum = 0L;
   }

   static final Pattern PATTERN_NUMBER = Pattern.compile("\\d+");
   static final Pattern PATTERN_GEAR = Pattern.compile("[*]");

   public long part2(String fileName) {
      PartAccumulator accumulator = FileLineStreamer.read(fileName)
            .collect(PartAccumulator::new, this::accumulate, this::combine);
      return accumulator.partSum;
   }

   void accumulate(PartAccumulator accumulator, String line) {
      accumulator.previousLine = accumulator.currentLine;
      accumulator.currentLine = accumulator.nextLine;
      accumulator.nextLine = line;

      accumulateGearRatios(accumulator);
   }

   void accumulateGearRatios(PartAccumulator accumulator) {
      if (accumulator.previousLine == null || accumulator.currentLine == null || accumulator.nextLine == null) return;
      Matcher gearMatcher = PATTERN_GEAR.matcher(accumulator.currentLine);

      while (gearMatcher.find()) {
         List<Long> ratios = new ArrayList<>();

         matchGears(ratios, accumulator.previousLine, gearMatcher.start());
         matchGears(ratios, accumulator.currentLine, gearMatcher.start());
         matchGears(ratios, accumulator.nextLine, gearMatcher.start());

         if (ratios.size() != 2) continue;
         accumulator.partSum += Math.multiplyExact(ratios.get(0), ratios.get(1));
      }
   }

   void matchGears(List<Long> ratios, String line, int gearPosition) {
      Matcher numberMatcher = PATTERN_NUMBER.matcher(line);
      
      while (numberMatcher.find()) {
         if ((gearPosition + 1) < numberMatcher.start()) continue;
         if (gearPosition > numberMatcher.end()) continue;

         ratios.add(Long.parseLong(numberMatcher.group()));
      }
   }

   PartAccumulator combine(PartAccumulator left, PartAccumulator right) {
      PartAccumulator combined = new PartAccumulator();
      combined.partSum = left.partSum + right.partSum;

      return combined;
   }
}
