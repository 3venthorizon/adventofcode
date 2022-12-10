package com.adventofcode;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

class Day8Test {
   
   Day8 day8 = new Day8();

   @Test
   void test() throws IOException, URISyntaxException {
      System.out.println(day8.part1());
   }

}
