package com.adventofcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * {@link https://adventofcode.com/2024/day/1}
 */
public class Day1 {
   record Numbers(Long left, Long right) { }

   public long part1(String filename) {
      List<Long> leftList = new ArrayList<>();  
      List<Long> rightList = new ArrayList<>();
      Consumer<Numbers> listsAdd = numbers -> leftList.add(numbers.left());
      listsAdd = listsAdd.andThen(numbers -> rightList.add(numbers.right()));

      FileLineStreamer.read(filename)
            .map(this::extract)
            .forEach(listsAdd);
      leftList.sort(Comparator.naturalOrder());   
      rightList.sort(Comparator.naturalOrder());
      long distanceTotal = 0L;

      for (int index = 0, total = leftList.size(); index < total; index++) {
         long distance = Math.abs(leftList.get(index).longValue() - rightList.get(index).longValue());
         distanceTotal += distance;
      }

      return distanceTotal;
   }

   public long part2(String filename) {
      List<Long> leftList = new ArrayList<>();  
      List<Long> rightList = new ArrayList<>();
      Consumer<Numbers> listsAdd = numbers -> leftList.add(numbers.left());
      listsAdd = listsAdd.andThen(numbers -> rightList.add(numbers.right()));

      FileLineStreamer.read(filename)
            .map(this::extract)
            .forEach(listsAdd);
      long productTotal = 0L;

      for (int index = 0, total = leftList.size(); index < total; index++) {
         long left = leftList.get(index);
         long count = rightList.stream().filter(right -> right == left).count();
         productTotal += left * count;
      }

      return productTotal;
   }

   Numbers extract(String line) {
      String[] numbers = line.split(" +");
      return new Numbers(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
   }
}
