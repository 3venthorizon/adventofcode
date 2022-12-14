package com.adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Day11 {

   static class Monkey {
      final List<BigInteger> items = new ArrayList<>();
      final UnaryOperator<BigInteger> operation;
      final Function<BigInteger, Integer> test;
      BigInteger inspections = BigInteger.ZERO;
      
      Monkey(UnaryOperator<BigInteger> operation, Function<BigInteger, Integer> test) {
         this.operation = operation;
         this.test = test;
      }

      public BigInteger getInspections() { return inspections; }
   }
   
   List<Monkey> monkeys;
   
   void init() {
      List<Monkey> monkeys = new ArrayList<>();
      monkeys.add(new Monkey(old -> old.multiply(BigInteger.valueOf(19L)), 
            number -> (number.mod(BigInteger.valueOf(3L)).compareTo(BigInteger.ZERO) == 0) ? 2 : 3));
      monkeys.get(0).items.add(BigInteger.valueOf(76L));
      monkeys.get(0).items.add(BigInteger.valueOf(88L));
      monkeys.get(0).items.add(BigInteger.valueOf(96L));
      monkeys.get(0).items.add(BigInteger.valueOf(97L));
      monkeys.get(0).items.add(BigInteger.valueOf(58L));
      monkeys.get(0).items.add(BigInteger.valueOf(61L));
      monkeys.get(0).items.add(BigInteger.valueOf(67L));
      
      monkeys.add(new Monkey(old -> old.add(BigInteger.valueOf(8L)), 
            number -> (number.mod(BigInteger.valueOf(11L)).compareTo(BigInteger.ZERO) == 0) ? 5 : 6));
      monkeys.get(1).items.add(BigInteger.valueOf(93L));
      monkeys.get(1).items.add(BigInteger.valueOf(71L));
      monkeys.get(1).items.add(BigInteger.valueOf(79L));
      monkeys.get(1).items.add(BigInteger.valueOf(83L));
      monkeys.get(1).items.add(BigInteger.valueOf(69L));
      monkeys.get(1).items.add(BigInteger.valueOf(70L));
      monkeys.get(1).items.add(BigInteger.valueOf(94L));
      monkeys.get(1).items.add(BigInteger.valueOf(98L));
      
      monkeys.add(new Monkey(old -> old.multiply(BigInteger.valueOf(13L)), 
            number -> (number.mod(BigInteger.valueOf(19L)).compareTo(BigInteger.ZERO) == 0) ? 3 : 1));
      monkeys.get(2).items.add(BigInteger.valueOf(50L));
      monkeys.get(2).items.add(BigInteger.valueOf(74L));
      monkeys.get(2).items.add(BigInteger.valueOf(67L));
      monkeys.get(2).items.add(BigInteger.valueOf(92L));
      monkeys.get(2).items.add(BigInteger.valueOf(61L));
      monkeys.get(2).items.add(BigInteger.valueOf(76L));
      
      monkeys.add(new Monkey(old -> old.add(BigInteger.valueOf(6L)), 
            number -> (number.mod(BigInteger.valueOf(5L)).compareTo(BigInteger.ZERO) == 0) ? 1 : 6));
      monkeys.get(3).items.add(BigInteger.valueOf(76L));
      monkeys.get(3).items.add(BigInteger.valueOf(92L));
      
      monkeys.add(new Monkey(old -> old.add(BigInteger.valueOf(5L)), 
            number -> (number.mod(BigInteger.valueOf(2L)).compareTo(BigInteger.ZERO) == 0) ? 2 : 0));
      monkeys.get(4).items.add(BigInteger.valueOf(74L));
      monkeys.get(4).items.add(BigInteger.valueOf(94L));
      monkeys.get(4).items.add(BigInteger.valueOf(55L));
      monkeys.get(4).items.add(BigInteger.valueOf(87L));
      monkeys.get(4).items.add(BigInteger.valueOf(62L));
      
      monkeys.add(new Monkey(old -> old.multiply(old), 
            number -> (number.mod(BigInteger.valueOf(7L)).compareTo(BigInteger.ZERO) == 0) ? 4 : 7));
      monkeys.get(5).items.add(BigInteger.valueOf(59L));
      monkeys.get(5).items.add(BigInteger.valueOf(62L));
      monkeys.get(5).items.add(BigInteger.valueOf(53L));
      monkeys.get(5).items.add(BigInteger.valueOf(62L));
      
      monkeys.add(new Monkey(old -> old.add(BigInteger.valueOf(2L)), 
            number -> (number.mod(BigInteger.valueOf(17L)).compareTo(BigInteger.ZERO) == 0) ? 5 : 7));
      monkeys.get(6).items.add(BigInteger.valueOf(62L));
      
      monkeys.add(new Monkey(old -> old.add(BigInteger.valueOf(3L)), 
            number -> (number.mod(BigInteger.valueOf(13L)).compareTo(BigInteger.ZERO) == 0) ? 4 : 0));
      monkeys.get(7).items.add(BigInteger.valueOf(85L));
      monkeys.get(7).items.add(BigInteger.valueOf(54L));
      monkeys.get(7).items.add(BigInteger.valueOf(53L));
      
      this.monkeys = Collections.unmodifiableList(monkeys);
   }
   
   public BigInteger part1() {
      init();
      UnaryOperator<BigInteger> relief = value -> value.divide(BigInteger.valueOf(3L));
      
      for (int round = 0; round < 20; round++) {
         monkeys.forEach(monkey -> inspect(monkey, relief));
      }
      
      return monkeys.stream()
            .map(Monkey::getInspections)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(BigInteger.ONE, BigInteger::multiply);
   }
   
   public BigInteger part2() {
      init();
      UnaryOperator<BigInteger> norelief = value -> value;
      
      for (int round = 0; round < 10_000; round++) {
         monkeys.forEach(monkey -> inspect(monkey, norelief));
         
         if ((round + 1) % 500 == 0) {
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
            .reduce(BigInteger.ONE, BigInteger::multiply);
   }
   
   void inspect(Monkey monkey, UnaryOperator<BigInteger> relief) {
      monkey.inspections = monkey.inspections.add(BigInteger.valueOf(monkey.items.size()));
      monkey.items.stream()
            .map(monkey.operation) //inspects
            .map(relief) //item is not broken
            .forEach(item -> monkeys.get(monkey.test.apply(item).intValue()).items.add(item));
      monkey.items.clear();
   }
}
