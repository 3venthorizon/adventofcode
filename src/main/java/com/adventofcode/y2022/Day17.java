package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class Day17 {
   static final int LEFT = '<';
   static final int RIGHT = '>';
   static final int MASK_WIDTH = 0b1111111;
   
   static final UnaryOperator<Integer> SHIFT_LEFT = state -> (state << 1) & MASK_WIDTH;
   static final UnaryOperator<Integer> SHIFT_RIGHT = state -> state >> 1;
   
   static final List<Integer> ROCK1 = List.of(0b0011110);
   static final List<Integer> ROCK2 = List.of(
         0b0001000, 
         0b0011100, 
         0b0001000);
   static final List<Integer> ROCK3 = List.of(
         0b0000100,
         0b0000100,
         0b0011100);
   static final List<Integer> ROCK4 = List.of(
         0b0010000,
         0b0010000,
         0b0010000,
         0b0010000);
   static final List<Integer> ROCK5 = List.of(
         0b0011000,
         0b0011000);
   static final List<List<Integer>> ROCKS = List.of(ROCK1, ROCK2, ROCK3, ROCK4, ROCK5);
   static final List<Integer> SPACE3 = List.of(0, 0, 0);
   
   public int part1() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day17.input");
      BufferedReader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      List<Integer> column = new ArrayList<>();
      int depth = 0;
      int rockIndex = 0;
      List<Integer> rock = new ArrayList<>(ROCKS.get(rockIndex));
      
      column.addAll(rock);
      column.addAll(SPACE3);
      column.add(-1); //floor
      
      int direction = reader.read();
      
      while (direction > -1) {
         if (direction != LEFT && direction != RIGHT) continue;
         shiftNdrop(direction, depth, rockIndex, rock, column);
         
         direction = reader.read();
      }
      
      return 0;
   }
   
   void shiftNdrop(int direction, int depth, int rockIndex, List<Integer> rock, List<Integer> column) {
      shift(direction, depth, rock, column);
      drop(depth, rock, column);
      
   }
   
   void shift(int direction, int depth, List<Integer> rock, List<Integer> column) {
      UnaryOperator<Integer> shifter = direction == LEFT ? SHIFT_LEFT : SHIFT_RIGHT;
      List<Integer> shiftRock = new ArrayList<>();
      List<Integer> shiftColumn = new ArrayList<>();
      int depthIndex = depth;
      
      for (Integer element : rock) {
         int shifted = shifter.apply(element);
         if (Integer.bitCount(shifted) != Integer.bitCount(element)) return;
         
         shiftRock.add(shifted);
         int chamber = column.get(depthIndex++) ^ element; //clear chamber
         shifted = chamber ^ shifted;
         
         if ((shifted & chamber) != chamber) return;
         shiftColumn.add(shifted);
      }
      
      rock.clear();
      rock.addAll(shiftRock);
      depthIndex = depth;
      
      for (Integer chamber : shiftColumn) {
         column.set(depthIndex++, chamber);
      }
   }
   
   void drop(int depth, List<Integer> rock, List<Integer> column) {
      
   }
   
}
