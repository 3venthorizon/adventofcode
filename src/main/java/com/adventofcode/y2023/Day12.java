package com.adventofcode.y2023;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {
   record Groups(String partial, int[] groupSizes) {}

   public long part1(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::extract)
            .mapToLong(this::countArrangements)
            .sum();
   }

   Groups extract(String line) {
      String[] records = line.split(" ");
      String[] sizes = records[1].split(",");
      int[] groupSizes = Stream.of(sizes).mapToInt(Integer::parseInt).toArray();
      return new Groups(records[0], groupSizes);
   }

   long countArrangements(Groups groups) {
      int minLength = IntStream.of(groups.groupSizes()).sum() + groups.groupSizes.length - 1;
      int radix = groups.partial.length() - minLength;
      if (radix == 0) return 1L;

      int digits = groups.groupSizes.length + 1;
      int[] numberSystem = new int[digits];
      int[] radixes = new int[digits];


      return 0L;
   }

   public long part2() {
      return 0L;
   }
}
