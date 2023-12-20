package com.adventofcode.y2023;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {

   record Arrangement(int[] numbers, int digit) {}

   public long part1(String fileName) {

      return 0L;
   }

   long countArrangements(String line) {
      String[] records = line.split(" ");
      String partial = records[0];
      String[] sizes = records[1].split(",");
      int[] groupSizes = Stream.of(sizes).mapToInt(Integer::parseInt).toArray();
      int minLength = IntStream.of(groupSizes).sum() + groupSizes.length - 1;
      int radix = partial.length() - minLength;
      if (radix <= 0) return 1L;

      int digits = groupSizes.length + 1;
      int[] numbers = new int[digits];

      numbers[0] = radix;
      numbers[groupSizes.length] = 0;
      IntStream.range(1, groupSizes.length).forEach(index -> numbers[index] = 1);

      Arrangement arrangement = new Arrangement(numbers, digits);

      return 0L;
   }

   Pattern compose(int[] groupSizes) {

      return null;
   }

   boolean isValid(String partial, Arrangement arrangement, Pattern pattern) {
      return false;
   }

   List<Arrangement> enumerate(Arrangement arrangement) {
      return List.of();
   }

   public long part2() {
      return 0L;
   }
}
