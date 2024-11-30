package com.adventofcode.y2022;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

class Day16Test {
   Day16Part1 part1 = new Day16Part1();
   Day16Part2 part2 = new Day16Part2();
   
   @Test
   void testPart1() throws IOException, URISyntaxException {
      System.out.println(part1.part1());
   }
   
   @Test
   void testPart2() throws IOException, URISyntaxException {
      System.out.println(part2.part2());
   }
}
