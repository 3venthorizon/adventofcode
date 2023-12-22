package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class Day12Test {
   Day12 day12 = new Day12();

   @Test
   void testCompose() {
      int[] groupSizes = {2, 1, 5, 3, 4};
      Pattern pattern = day12.compose(groupSizes);

      assertEquals("#{2}\\.+#{1}\\.+#{5}\\.+#{3}\\.+#{4}", pattern.pattern());
   }

   @Test
   void testValidate() {
      Pattern pattern = day12.compose(new int[] {3, 2, 1});
      String partial = "?###????????";

      assertTrue(day12.validate(partial, ".###.##.#...", pattern));
      assertTrue(day12.validate(partial, ".###.##..#..", pattern));
      assertTrue(day12.validate(partial, ".###.##...#.", pattern));
      assertTrue(day12.validate(partial, ".###.##....#", pattern));
      assertTrue(day12.validate(partial, ".###..##.#..", pattern));
      assertTrue(day12.validate(partial, ".###..##..#.", pattern));
      assertTrue(day12.validate(partial, ".###..##...#", pattern));
      assertTrue(day12.validate(partial, ".###...##.#.", pattern));
      assertTrue(day12.validate(partial, ".###...##..#", pattern));
      assertTrue(day12.validate(partial, ".###....##.#", pattern));

      assertFalse(day12.validate(partial, "####.##.#...", pattern));
      assertFalse(day12.validate(partial, ".######.#...", pattern));
      assertFalse(day12.validate(partial, "###.##.#....", pattern));
      assertFalse(day12.validate(partial, "..###.##.#..", pattern));
      assertFalse(day12.validate(partial, "....###.##.#", pattern));

      assertFalse(day12.validate(".??..??...?##.", "..#..#..###...", day12.compose(new int[] {1, 1, 3})));
   }

   @Test
   void testGenerateArrangement() {
      int[] groupSizes = { 3, 2, 1 };
      Day12.Record line = new Day12.Record(null, groupSizes);
      int[] numbers = { 0, 2, 3, 1 };
      Day12.Arrangement arrangement = new Day12.Arrangement(line, numbers, 0);

      assertEquals(".###...##..#", arrangement.toString());
   }

   @Test
   void testEnumerateArrangements() {
      String partial = "??????? 1,1,1";
      Day12.Record line = day12.extract(partial);
      int[] numbers = { 2, 1, 1, 0 };
      Day12.Arrangement arrangement = new Day12.Arrangement(line, numbers, numbers.length);
      Pattern pattern = day12.compose(line.groupSizes());

      System.out.println(arrangement.toString());

      List<Day12.Arrangement> resultList = day12.enumerate(line.partial(), pattern, arrangement);

      assertEquals(9L, resultList.size());
   }

   @Test
   void testEnumerateDebug() {
      String partial = ".??..??...?##. 1,1,3";
      Day12.Record line = day12.extract(partial);
      int[] numbers = { 7, 1, 1, 0 };
      Day12.Arrangement arrangement = new Day12.Arrangement(line, numbers, numbers.length);
      Pattern pattern = day12.compose(line.groupSizes());

      System.out.println(arrangement.toString());

      List<Day12.Arrangement> resultList = day12.enumerate(line.partial(), pattern, arrangement);

      assertEquals(4L, resultList.size());
   }

   @Test
   void testExample1() {
      long result = day12.part1("2023/day12/example1.input");

      assertEquals(21L, result);
   }

   @Test
   void testPart1() {
      long result = day12.part1("2023/day12/data.input");

      System.out.println("Day12 - Part1: " + result);
      assertEquals(7236L, result);
   }

   @Test
   void testExample2() {
      BigInteger result = day12.part2("2023/day12/example1.input");

      assertEquals(BigInteger.valueOf(525152L), result);
   }

   @Test
   void testPart2() {
      BigInteger result = day12.part2("2023/day12/data.input");

      System.out.println("Day12 - Part2: " + result);
   }
}
