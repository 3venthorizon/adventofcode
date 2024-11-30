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
   record Record(String partial, int[] groupSizes) {}

   record Arrangement(Record line, int[] numbers, int digit) {
      public String toString() {
         StringBuilder builder = new StringBuilder();

         for (int index = 0; index < line.groupSizes.length; index++) {
            char[] dots = new char[numbers[line.groupSizes.length - index]];
            char[] hash = new char[line.groupSizes[index]];
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
            .map(this::extract)
            .map(this::enumerateArrangements)
            .flatMap(List::stream)
            .count();
   }

   Record extract(String line) {
      String[] records = line.split(" ");
      String partial = records[0];
      String groups = records[1];
      String[] sizes = groups.split(",");
      int[] groupSizes = Stream.of(sizes).mapToInt(Integer::parseInt).toArray();

      return new Record(partial, groupSizes);
   }

   Arrangement root(Record line) {
      int minLength = IntStream.of(line.groupSizes).sum() + line.groupSizes.length - 1;
      int radix = line.partial.length() - minLength;
      int digits = line.groupSizes.length + 1;
      int[] numbers = new int[digits];
      numbers[0] = radix;
      numbers[line.groupSizes.length] = 0;
      IntStream.range(1, line.groupSizes.length).forEach(index -> numbers[index] = 1);

      return new Arrangement(line, numbers, digits);
   }

   List<Arrangement> enumerateArrangements(Record line) {
      Arrangement arrangement = root(line);
      int minLength = IntStream.of(line.groupSizes).sum() + line.groupSizes.length - 1;
      int radix = line.partial.length() - minLength;

      if (radix <= 0) return List.of(arrangement);

      Pattern pattern = compose(line.groupSizes);
      List<Arrangement> list = enumerate(line.partial, pattern, arrangement);
      if (validate(line.partial, arrangement.toString(), pattern)) list.add(0, arrangement);

      return list;
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

         Arrangement sub = new Arrangement(arrangement.line, generated, index + 1);
         if (validate(partial, sub.toString(), pattern)) list.add(sub);
         list.addAll(enumerate(partial, pattern, sub));
      }

      return list;
   }

   public BigInteger part2(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::extract)
            .map(this::unfold)
            .reduce(BigInteger::add)
            .orElse(BigInteger.ZERO);
   }

   BigInteger unfold(Record line) {
      Record extended = new Record('?' + line.partial, line.groupSizes);
      List<Arrangement> arrangements = enumerateArrangements(line);
      List<Arrangement> extendedList = enumerateArrangements(extended);

      int[] doubled = IntStream.concat(IntStream.of(line.groupSizes), IntStream.of(line.groupSizes)).toArray();
      Record unfold = new Record(line.partial + '?' + line.partial, doubled);
      List<Arrangement> huge = enumerateArrangements(unfold);



      return BigInteger.valueOf(1);
   }
}
