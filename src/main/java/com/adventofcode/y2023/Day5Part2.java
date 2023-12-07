package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5Part2 {
   record Range(long source, long destination, long length) {}

   static class RangeAccumulator {
      List<Range> sourceRanges = new ArrayList<>();
      List<Range> destinationRanges = new ArrayList<>();
   }

   static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+\\s\\d+\\s\\d+");
   static final Pattern PATTERN_SEEDS = Pattern.compile("\\d+\\s\\d+");
   static final Pattern PATTERN_LATCH = Pattern.compile("[^\\d\\s]");

   static final int INDEX_SOURCE = 1;
   static final int INDEX_DESTINATION = 0;
   static final int INDEX_LENGTH = 2;

   long part2(String fileName) {
      RangeAccumulator accumulator = FileLineStreamer.read(fileName)
            .collect(RangeAccumulator::new, this::accumulate, this::combine);
      
      return 0L;
   }

   void accumulate(RangeAccumulator accumulator, String line) {
      if (line.isBlank()) return;
      if (seedsRanges(accumulator, line)) return;
      if (switchRange(accumulator, line)) return;

      Matcher matcher = PATTERN_NUMBERS.matcher(line);
      if (!matcher.find()) return;

      //guarantees to match 3 numbers separated by a space
      List<Long> values = List.of(line.split(" ")).stream()
            .map(Long::parseLong)
            .toList();
      Range range = new Range(values.get(INDEX_SOURCE), values.get(INDEX_DESTINATION), values.get(INDEX_LENGTH));
      accumulator.destinationRanges.add(range);
   }

   boolean seedsRanges(RangeAccumulator accumulator, String line) {
      if (!line.startsWith("seeds: ")) return false;

      Matcher matcher = PATTERN_SEEDS.matcher(line);
      
      while (matcher.find()) {
         List<Long> values = List.of(matcher.group().split(" ")).stream()
               .map(Long::parseLong)
               .toList();
         Range range = new Range(values.get(0), values.get(0), values.get(1));
         accumulator.destinationRanges.add(range);
      }

      return true;
   }

   boolean switchRange(RangeAccumulator accumulator, String line) {
      if (!PATTERN_LATCH.matcher(line).find()) return false;

      accumulator.sourceRanges = accumulator.destinationRanges;
      accumulator.destinationRanges = new ArrayList<>();

      return true;
   }

   RangeAccumulator combine(RangeAccumulator left, RangeAccumulator right) {
      RangeAccumulator combined = new RangeAccumulator();

      return combined;
   }
}
