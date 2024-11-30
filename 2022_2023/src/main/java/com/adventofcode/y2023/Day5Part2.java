package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5Part2 {
   record Range(long startIndex, long endIndex) {}

   static class RangeAccumulator {
      final List<Range> ranges = new ArrayList<>();
      final List<Range> destinations = new ArrayList<>();
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
      accumulator = combine(accumulator, new RangeAccumulator());
      Collections.sort(accumulator.ranges, Comparator.comparing(Range::startIndex));
      
      return accumulator.ranges.get(0).startIndex;
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
      final long length = values.get(INDEX_LENGTH);
      final long startIndex = values.get(INDEX_SOURCE);
      final long endIndex = startIndex + length;
      final long destinationIndex = values.get(INDEX_DESTINATION);

      ListIterator<Range> sourceIterator = accumulator.ranges.listIterator();

      while (sourceIterator.hasNext()) {
         Range source = sourceIterator.next();

         if (source.endIndex <= startIndex) continue;
         if (source.startIndex >= endIndex) continue;

         sourceIterator.remove();
         long outerStart = Math.min(startIndex, source.startIndex);
         long joinStart = Math.max(startIndex, source.startIndex);
         long joinEnd = Math.min(endIndex, source.endIndex);
         long outerEnd = Math.max(endIndex, source.endIndex);
         long offset = joinStart - startIndex;
         long joinLength = joinEnd - joinStart;
         long destinationStart = destinationIndex + offset;
         long destinationEnd = destinationStart + joinLength;

         accumulator.destinations.add(new Range(destinationStart, destinationEnd));
         
         if (outerStart == source.startIndex && outerStart != joinStart) {
            sourceIterator.add(new Range(outerStart, joinStart));
         }
         if (outerEnd == source.endIndex && outerEnd != joinEnd) {
            sourceIterator.add(new Range(joinEnd, outerEnd));
         }
      }
   }

   boolean seedsRanges(RangeAccumulator accumulator, String line) {
      if (!line.startsWith("seeds: ")) return false;

      Matcher matcher = PATTERN_SEEDS.matcher(line);
      
      while (matcher.find()) {
         List<Long> values = List.of(matcher.group().split(" ")).stream()
               .map(Long::parseLong)
               .toList();
         long startIndex = values.get(0);
         long endIndex = startIndex + values.get(1);
         accumulator.ranges.add(new Range(startIndex, endIndex));
      }

      return true;
   }

   boolean switchRange(RangeAccumulator accumulator, String line) {
      if (!PATTERN_LATCH.matcher(line).find()) return false;

      accumulator.ranges.addAll(accumulator.destinations);
      accumulator.destinations.clear();

      return true;
   }

   RangeAccumulator combine(RangeAccumulator left, RangeAccumulator right) {
      RangeAccumulator combined = new RangeAccumulator();
      combined.ranges.addAll(left.ranges);
      combined.ranges.addAll(left.destinations);
      combined.ranges.addAll(right.ranges);
      combined.ranges.addAll(right.destinations);

      return combined;
   }
}
