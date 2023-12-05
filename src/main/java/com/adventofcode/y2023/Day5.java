package com.adventofcode.y2023;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {
   static class TransformAccumulator {
      final Set<Long> values = new HashSet<>();
      final Set<Long> latched = new HashSet<>();
   }

   static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+\\s\\d+\\s\\d+");
   static final Pattern PATTERN_LATCH = Pattern.compile("[^\\d\\s]");

   static final int INDEX_SOURCE = 1;
   static final int INDEX_DESTINATION = 0;
   static final int INDEX_LENGTH = 2;

   public long part1(String fileName) {
      TransformAccumulator accumulator = FileLineStreamer.read(fileName)
            .collect(TransformAccumulator::new, this::accumulate, this::combine);
      accumulator = combine(accumulator, accumulator);
      return accumulator.values.stream()
            .mapToLong(Long::longValue)
            .summaryStatistics()
            .getMin();
   }

   public long part2(String fileName) {
      return 0L;
   }

   void accumulate(TransformAccumulator accumulator, String line) {
      if (line.isBlank()) return;
      if (line.startsWith("seeds: ")) {
         List.of(line.substring(7).split(" ")).stream()
               .map(Long::parseLong)
               .forEach(accumulator.latched::add);
         return;
      }

      if (PATTERN_LATCH.matcher(line).find()) {
         accumulator.values.addAll(accumulator.latched);
         accumulator.latched.clear();
         return;
      }

      //skip line when all the values are latched
      if (accumulator.values.isEmpty()) return;

      Matcher matcher = PATTERN_NUMBERS.matcher(line);
      if (!matcher.find()) return;
      //guarantees to match 3 numbers separated by a space
      List<Long> ranges = List.of(line.split(" ")).stream()
            .map(Long::parseLong)
            .toList();
      long startRange = ranges.get(INDEX_SOURCE);
      long destRange = ranges.get(INDEX_DESTINATION);
      long length = ranges.get(INDEX_LENGTH);

      Iterator<Long> sourceIterator = accumulator.values.iterator();

      while (sourceIterator.hasNext()) {
         long sourceIndex = sourceIterator.next();
         long offset = sourceIndex - startRange;

         if (offset < 0) continue;
         if (offset >= length) continue;

         sourceIterator.remove();
         accumulator.latched.add(Math.addExact(destRange, offset));
      }
   }

   TransformAccumulator combine(TransformAccumulator left, TransformAccumulator right) {
      TransformAccumulator combined = new TransformAccumulator();
      combined.values.addAll(left.values);
      combined.values.addAll(right.values);
      combined.values.addAll(left.latched);
      combined.values.addAll(right.latched);

      return combined;
   }
}
