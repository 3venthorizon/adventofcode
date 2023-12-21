package com.adventofcode.y2023;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {
   record Arrangement(int[] groupSizes, int[] numbers, int digit) {
      public String toString() {
         StringBuilder builder = new StringBuilder();
         
         for (int index = 0; index < groupSizes.length; index++) {
            char[] dots = new char[numbers[groupSizes.length - index]];
            char[] hash = new char[groupSizes[index]];
            Arrays.fill(dots, '.');
            Arrays.fill(hash, '#');

            builder.append(dots).append(hash);
         }

         char[] dots = new char[numbers[0]];
         Arrays.fill(dots, '.');
         builder.append(dots);

         return builder.toString();
      }
   }

   public long part1(String fileName) {
      return FileLineStreamer.read(fileName)
            .mapToLong(this::countArrangements)
            .sum();
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
      Pattern pattern = compose(groupSizes);
      Arrangement arrangement = new Arrangement(groupSizes, numbers, digits);
      List<Arrangement> list = enumerate(partial, pattern, arrangement);
      if (validate(partial, arrangement.toString(), pattern)) list.add(0, arrangement);

      return list.size();
   }

   Pattern compose(int[] groupSizes) {
      String regex = IntStream.of(groupSizes)
            .mapToObj(size -> "#{" + size + "}")
            .collect(Collectors.joining("\\.+"));
      return Pattern.compile(regex);
   }

   boolean validate(String partial, String arrangement, Pattern pattern) {
      StringBuilder builder = new StringBuilder(arrangement);

      for (int index = 0, count = partial.length(); index < count; index++) {
         char override = partial.charAt(index);
         if (override == '?') continue;
         if (override != arrangement.charAt(index)) return false;
      }

      String result = builder.toString();
      Matcher matcher = pattern.matcher(result);
      if (!matcher.find()) return false;

      return Stream.of(result.split(pattern.pattern())).noneMatch(padding -> padding.contains("#"));
   }

   List<Arrangement> enumerate(String partial, Pattern pattern, Arrangement arrangement) {
      List<Arrangement> list = new ArrayList<>();
      int remainder = arrangement.numbers[0];
      if (remainder == 0) return list;
      
      for (int index = 1; index < arrangement.digit; index++) {
         int[] generated = Arrays.copyOf(arrangement.numbers, arrangement.numbers.length);
         generated[0] -= 1;
         generated[index] += 1;

         Arrangement sub = new Arrangement(arrangement.groupSizes, generated, index + 1);
         if (validate(partial, sub.toString(), pattern)) list.add(sub);
         list.addAll(enumerate(partial, pattern, sub));
      }

      return list;
   }

   public BigInteger part2(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::unfold)
            .reduce(BigInteger::add)
            .orElse(BigInteger.ZERO);
   }

   BigInteger unfold(String line) {
      long arrangements = countArrangements(line);

      String[] records = line.split(" ");
      String partial = records[0];
      String groups = records[1];
      String unfoldLine = partial + '?' + partial + " " + groups + ',' + groups;
      long unfolded = countArrangements(unfoldLine);
      BigInteger multiplier = BigInteger.valueOf(unfolded / arrangements);

      return BigInteger.valueOf(arrangements).multiply(multiplier.pow(4));
   }
}
