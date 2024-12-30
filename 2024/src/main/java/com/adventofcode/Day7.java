package com.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.LongBinaryOperator;

/// https://adventofcode.com/2024/day/7
public class Day7 {
   enum Operator {
      ADD(Math::addExact), 
      MULTIPLY(Math::multiplyExact), 
      CONCAT((left, right) -> Long.parseLong(Long.toString(left) + Long.toString(right)));

      public final LongBinaryOperator operator;
      
      Operator(LongBinaryOperator operator) {
         this.operator = operator;
      }
   }

   record Equation(long answer, List<Long> numbers) {}

   public long part1(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extract)
            .filter(equation -> evaluate(equation, List.of(Operator.ADD, Operator.MULTIPLY)))
            .map(Equation::answer)
            .mapToLong(Long::longValue)
            .sum();
   }

   public long part2(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extract)
            .filter(equation -> evaluate(equation, List.of(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)))
            .map(Equation::answer)
            .mapToLong(Long::longValue)
            .sum();
   }

   Equation extract(String line) {
      String[] parts = line.split(":");
      long answer = Long.parseLong(parts[0]);
      List<Long> numbers = Arrays.stream(parts[1].trim().split(" "))
            .map(Long::parseLong)
            .toList();
      return new Equation(answer, numbers);
   }

   boolean evaluate(Equation equation, List<Operator> operators) {
      int operatorCount = equation.numbers().size() - 1;
      int enumeratedCount = (int) Math.pow(operators.size(), operatorCount);

      for (int x = 0; x < enumeratedCount; x++) {
         List<LongBinaryOperator> operatorsList = operators(operators, x, operatorCount);
         long calculated = calculate(equation.numbers(), operatorsList);
         if (calculated == equation.answer()) return true;
      }

      return false;
   }

   List<LongBinaryOperator> operators(List<Operator> operators, int enumeration, int size) {
      List<LongBinaryOperator> operatorList = new ArrayList<>();
      int radix = operators.size();

      for (int x = 0; x < size; x++) {
         int select = (enumeration / ((int) (Math.pow(radix, x)))) % radix;
         operatorList.add(operators.get(select).operator);
      }

      return operatorList;
   }

   long calculate(List<Long> numbers, List<LongBinaryOperator> operators) {
      Iterator<Long> numberIterator = numbers.iterator();
      Iterator<LongBinaryOperator> operatorIterator = operators.iterator();

      if (!numberIterator.hasNext()) return 0L;
      long result = numberIterator.next();

      while (numberIterator.hasNext() && operatorIterator.hasNext()) {
         long number = numberIterator.next();
         LongBinaryOperator operator = operatorIterator.next();
         result = operator.applyAsLong(result, number);
      }

      return result;
   }
}
