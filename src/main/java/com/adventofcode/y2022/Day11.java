package com.adventofcode.y2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Day11 {

   static class Monkey {
      final List<Long> items = new ArrayList<>();
      final UnaryOperator<Long> operation;
      final Function<Long, Integer> test;
      long inspections = 0L;
      
      Monkey(UnaryOperator<Long> operation, Function<Long, Integer> test) {
         this.operation = operation;
         this.test = test;
      }

      public Long getInspections() { return inspections; }
   }
   
   List<Monkey> monkeys;
   
   void init() {
      monkeys = new ArrayList<>();
      monkeys.add(new Monkey(old -> old * 19L, number -> (number % 3L == 0L) ? 2 : 3));
      monkeys.get(0).items.addAll(List.of(76L, 88L, 96L, 97L, 58L, 61L, 67L));
      
      monkeys.add(new Monkey(old -> old + 8L, number -> (number % 11L == 0) ? 5 : 6));
      monkeys.get(1).items.addAll(List.of(93L, 71L, 79L, 83L, 69L, 70L, 94L, 98L));
      
      monkeys.add(new Monkey(old -> old * 13L, number -> (number % 19L == 0) ? 3 : 1));
      monkeys.get(2).items.addAll(List.of(50L, 74L, 67L, 92L, 61L, 76L));
      
      monkeys.add(new Monkey(old -> old + 6L, number -> (number % 5L == 0) ? 1 : 6));
      monkeys.get(3).items.addAll(List.of(76L, 92L));
      
      monkeys.add(new Monkey(old -> old + 5L, number -> (number % 2L == 0) ? 2 : 0));
      monkeys.get(4).items.addAll(List.of(74L, 94L, 55L, 87L, 62L));
      
      monkeys.add(new Monkey(old -> old * old, number -> (number % 7L == 0) ? 4 : 7));
      monkeys.get(5).items.addAll(List.of(59L, 62L, 53L, 62L));
      
      monkeys.add(new Monkey(old -> old + 2L, number -> (number % 17L == 0) ? 5 : 7));
      monkeys.get(6).items.add(62L);
      
      monkeys.add(new Monkey(old -> old + 3L, number -> (number % 13L == 0) ? 4 : 0));
      monkeys.get(7).items.addAll(List.of(85L, 54L, 53L));
   }
   
   public long part1() {
      init();
      UnaryOperator<Long> relief = value -> value / 3L;
      
      for (int round = 0; round < 20; round++) {
         monkeys.forEach(monkey -> inspect(monkey, relief));
      }
      
      return monkeys.stream()
            .map(Monkey::getInspections)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(1L, Math::multiplyExact);
   }
   
   public long part2() {
      init();
      UnaryOperator<Long> norelief = value -> value % 9699690;
      
      for (int round = 0; round < 10_000; round++) {
         monkeys.forEach(monkey -> inspect(monkey, norelief));
         
         if ((round + 1) % 1000 == 0) {
            String thousand = monkeys.stream()
                  .map(Monkey::getInspections)
                  .map(Object::toString)
                  .collect(Collectors.joining(", ", "Round: " + (round + 1) + " [", "]"));
            System.out.println(thousand);
         }
      }
      
      return monkeys.stream()
            .map(Monkey::getInspections)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(1L, Math::multiplyExact);
   }
   
   void inspect(Monkey monkey, UnaryOperator<Long> relief) {
      monkey.inspections += monkey.items.size();
      monkey.items.stream()
            .map(monkey.operation) //inspects
            .map(relief) //item is not broken
            .forEach(item -> monkeys.get(monkey.test.apply(item)).items.add(item));
      monkey.items.clear();
   }
}
