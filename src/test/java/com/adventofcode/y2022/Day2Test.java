package com.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import com.adventofcode.y2022.Day2;

class Day2Test {
   Day2 test = new Day2();
   
   @Test
   void part1() throws IOException, URISyntaxException {
      System.out.println(test.calculateScore1()); 
   }

   @Test
   void part2() throws IOException, URISyntaxException {
      System.out.println(test.calculateScore2()); 
   }
}
