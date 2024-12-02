package com.adventofcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@link https://adventofcode.com/2024/day/2}
 */
public class Day2 {   
   public long part1(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extractReport)
            .filter(this::isSafe)
            .count();
   }

   public long part2(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extractReport)
            .filter(this::isSafe2)
            .count();
   }

   boolean isSafe(List<Long> numberList) {
      Iterator<Long> iterator = numberList.iterator();
      if (!iterator.hasNext()) return false;

      Long number = iterator.next();
      int slope = 0;

      while (iterator.hasNext()) {
         Long next = iterator.next();
         long difference = next.longValue() - number.longValue();
         int sign = Long.compare(number.longValue(), next.longValue());
         if (difference == 0L) return false;
         if (Math.abs(difference) > 3) return false;
         if (slope == 0) slope = sign;
         if (sign != slope) return false;
         number = next;
      }

      return true;
   }

   boolean isSafe2(List<Long> numberList) {
      for (int index = 0, total = numberList.size(); index < total; index++) {
         List<Long> subList = new ArrayList<>(numberList);
         subList.remove(index);
         if (isSafe(subList)) return true;
      }

      return false;
   }

   List<Long> extractReport(String line) {
      String[] numbers = line.split(" +");
      return Stream.of(numbers).map(Long::parseLong).toList();
   }
}
