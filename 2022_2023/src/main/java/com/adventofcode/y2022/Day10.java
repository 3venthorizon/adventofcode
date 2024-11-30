package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10 {
   static final String SPACE = " ";
   static final String COMMAND_NOOP = "noop";
   static final String COMMAND_ADDX = "addx ";
   
   static class State {
      int cycles = 1;
      int x = 1;
      int nextCylce = 20;
      int accumelator = 0;
   }

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day10.input");
      return Files.newBufferedReader(Paths.get(resource.toURI()));
   }
   
   public int part1() throws IOException, URISyntaxException {
      State state = new State();
      
      try (BufferedReader reader = createReader()) {
         String line = reader.readLine();
         
         while (line != null) {
            process(line, state);
            line = reader.readLine();
         }
      }
      
      return state.accumelator;
   }
   
   void process(String instruction, State state) {
      if (COMMAND_NOOP.equals(instruction)) {
         noop(state);
         return;
      }
      if (!instruction.startsWith(COMMAND_ADDX)) return;
      
      int value = Integer.parseInt(instruction.substring(COMMAND_ADDX.length()));
      addx(state, value);
   }
   
   void noop(State state) {
      accumulate(state);
      state.cycles += 1;
   }
   
   void addx(State state, int value) {
      noop(state);
      noop(state);
      state.x += value;
   }
   
   void accumulate(State state) {
      crt(state);
      if (state.cycles != state.nextCylce) return;
      
      state.accumelator += state.cycles * state.x;
      state.nextCylce += 40;
   }
   
   void crt(State state) {
      char pixel = (Math.abs(((state.cycles - 1) % 40) - state.x) <= 1) ? '#' : ' ';
      System.out.print(pixel);
      if (state.cycles % 40 == 0) System.out.println();
   }
}
