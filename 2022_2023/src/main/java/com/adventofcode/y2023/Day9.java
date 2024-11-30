package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 {

   static final Pattern PATTERN_NUMBER = Pattern.compile("(-?\\d+)");

   public long part1(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::extractNumbers)
            .mapToInt(numbers -> extrapolate(numbers, this::forward))
            .sum();
   }

   public long part2(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::extractNumbers)
            .mapToInt(numbers -> extrapolate(numbers, this::backward))
            .sum();
   }

   List<Integer> extractNumbers(String line) {
      Matcher matcher = PATTERN_NUMBER.matcher(line);
      List<Integer> numbers = new ArrayList<>();

      while (matcher.find()) {
         numbers.add(Integer.parseInt(matcher.group()));
      }

      return numbers;
   }

   int extrapolate(List<Integer> numbers, BiFunction<List<Integer>, List<Integer>, Integer> direction) {
      int previous = numbers.get(0).intValue();
      List<Integer> reducedList = new ArrayList<>();
      int reduced = 0;

      for (Integer number : numbers.subList(1, numbers.size())) {
         reduced = number.intValue() - previous;
         previous = number.intValue();
         reducedList.add(reduced);
      }

      return direction.apply(numbers, reducedList).intValue();
   }

   int forward(List<Integer> numbers, List<Integer> reducedList) {
      int extrapolated = numbers.get(numbers.size() - 1).intValue();
      boolean terminal = reducedList.stream().allMatch(number -> number == 0);
      return terminal ? extrapolated : extrapolated + extrapolate(reducedList, this::forward);
   }

   int backward(List<Integer> numbers, List<Integer> reducedList) {
      int extrapolated = numbers.get(0).intValue();
      boolean terminal = reducedList.stream().allMatch(number -> number == 0);
      return terminal ? extrapolated : extrapolated - extrapolate(reducedList, this::backward);
   }
}
