package com.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/// https://adventofcode.com/2024/day/7
public class Day7 {
   final int OPERATOR_ADD = 0;
   final int OPERATOR_MUL = 1;

   record Equation(long answer, List<Long> numbers) {}

   public long part1(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extract)
            .filter(this::evaluate)
            .map(Equation::answer)
            .mapToLong(Long::longValue)
            .sum();
   }

   public static long part2(String filename) {
      return 0L;
   }

   Equation extract(String line) {
      String[] parts = line.split(":");
      long answer = Long.parseLong(parts[0]);
      List<Long> numbers = Arrays.stream(parts[1].trim().split(" "))
            .map(Long::parseLong)
            .toList();
      return new Equation(answer, numbers);
   }

   boolean evaluate(Equation equation) {
      int operatorCount = equation.numbers().size() - 1;
      int enumeratedCount = IntStream.range(0, operatorCount).reduce(1, (acc, _) -> acc << 1);

      for (int x = 0; x < enumeratedCount; x++) {
         List<LongBinaryOperator> operators = operators(x, operatorCount);
         long calculated = calculate(equation.numbers(), operators);
         if (calculated == equation.answer()) return true;
      }

      return false;
   }

   List<LongBinaryOperator> operators(int enumeration, int size) {
      List<LongBinaryOperator> operators = new ArrayList<>();

      for (int x = 0; x < size; x++) {
         int operatorCode = (enumeration >> x) & 1;
         LongBinaryOperator operator = switch (operatorCode) {
            case OPERATOR_ADD -> Math::addExact;
            default -> Math::multiplyExact;
         };

         operators.add(operator);
      }

      return operators;
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
