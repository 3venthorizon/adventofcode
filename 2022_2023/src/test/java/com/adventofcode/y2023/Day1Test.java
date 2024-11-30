package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * {@link https://adventofcode.com/2023/day/1}
 */
class Day1Test {
   Day1 day1 = new Day1();

   @Test
   void testExample1() {
      long result = day1.part1("2023/day1/example1.input");

      assertEquals(142L, result);
   }
   
   @Test
   void testPart1() {
      long result = day1.part1("2023/day1/data.input");
      
      System.out.println("Day1 - Part1: " + result);
      assertEquals(56397L, result);
   }

   @Test
   void testExample2() {
      long result = day1.part2("2023/day1/example2.input");

      assertEquals(281L, result);
   }

   @Test
   void testPart2() {
      long result = day1.part2("2023/day1/data.input");

      System.out.println("Day1 - Part2: " + result);
      assertEquals(55701L, result);
   }

   @Test
   void testExtractions() {
      Long result;
      result = Optional.of("oneight")
            .map(line -> day1.extractDigits(Day1.PATTERN_NUMBER.matcher(line)))
            .map(day1::replaceDigits)
            .map(Long::parseLong)
            .orElse(0L);
      assertEquals(18L, result);

      result = Optional.of("twone")
            .map(line -> day1.extractDigits(Day1.PATTERN_NUMBER.matcher(line)))
            .map(day1::replaceDigits)
            .map(Long::parseLong)
            .orElse(0L);
      assertEquals(21L, result);

      result = Optional.of("threeight")
            .map(line -> day1.extractDigits(Day1.PATTERN_NUMBER.matcher(line)))
            .map(day1::replaceDigits)
            .map(Long::parseLong)
            .orElse(0L);
      assertEquals(38L, result);

      result = Optional.of("fiveight")
            .map(line -> day1.extractDigits(Day1.PATTERN_NUMBER.matcher(line)))
            .map(day1::replaceDigits)
            .map(Long::parseLong)
            .orElse(0L);
      assertEquals(58L, result);

      result = Optional.of("nineight")
            .map(line -> day1.extractDigits(Day1.PATTERN_NUMBER.matcher(line)))
            .map(day1::replaceDigits)
            .map(Long::parseLong)
            .orElse(0L);
      assertEquals(98L, result);
   }
}
