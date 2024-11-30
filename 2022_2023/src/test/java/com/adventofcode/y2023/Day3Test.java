package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

/**
 * {@link https://adventofcode.com/2023/day/3}
 */
class Day3Test {
   Day3Part1 day3part1 = new Day3Part1();
   Day3Part2 day3part2 = new Day3Part2();

   @Test
   void testSymbolPattern() {
      String line = "...456.`.~.!.@.#.$.%.^.&.*.(.)._.-.=.+.[.].{.}.\\.|.;.:.'.\".,../.<.>.?...";
      Matcher matcher = Day3Part1.PATTERN_SYMBOL.matcher(line);
      char[] symbols = { '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '=', '+',
         '[', ']', '{', '}', '\\', '|', ';', ':', '\'', '"', ',', '/', '<', '>', '?' };
      int index = 0;
      
      while (matcher.find()) {
         assertEquals(symbols[index++], line.charAt(matcher.start()));
      }
   }

   @Test
   void testExample1() {
      long result = day3part1.part1("2023/day3/example1.input");

      assertEquals(4361L, result);
   }
   
   @Test
   void testPart1() {
      long result = day3part1.part1("2023/day3/data.input");
      
      System.out.println("Day3 - Part1: " + result);
      assertEquals(560670L, result);
   }

   @Test
   void testExample2() {
      long result = day3part2.part2("2023/day3/example1.input");

      assertEquals(467835L, result);
   }

   @Test
   void testPart2() {
      long result = day3part2.part2("2023/day3/data.input");
      
      System.out.println("Day3 - Part2: " + result);
      assertEquals(91622824L, result);
   }
}
