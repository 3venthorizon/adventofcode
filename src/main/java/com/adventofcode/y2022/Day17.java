package com.adventofcode.y2022;

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
   
   int depth = 0;
   long rockIndex = -1L;
   long height = -1;
   List<Integer> rock = new ArrayList<>(ROCKS.get(0));
   List<Integer> column = new ArrayList<>();
   
   public long part1() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day17.input");
      byte[] input = Files.readAllBytes(Paths.get(resource.toURI()));
      int index = 0;
         
      nextRock();
      column.add(-1); //floor
      
      while (rockIndex < 2022) {
         int direction = input[index];
         shiftNdrop(direction);
         index = (index + 1) % input.length;
      }
      
      return height + column.size() - rock.size() - SPACE3.size();
   }
   
   public long part2() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day17.input");
      byte[] input = Files.readAllBytes(Paths.get(resource.toURI()));
      int index = 0;
         
      nextRock();
      column.add(-1); //floor
      long limit = 1_000_000_000_000L;
      
      while (rockIndex < limit) {
         int direction = input[index];
         shiftNdrop(direction);
         index = (index + 1) % input.length;
         
         if (Math.floorMod(rockIndex, ROCKS.size()) == 0 && index == 0) break;
      }
      
      System.out.println("Rocks # " + rockIndex + 1);
      
      return height;
   }
   
   void shiftNdrop(int direction) {
      shift(direction);
      drop();
   }
   
   void shift(int direction) {
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
   
   void drop() {
      List<Integer> dropColumn = new ArrayList<>(column.subList(depth, depth + rock.size() + 1));
      
      for (int count = rock.size(), index = count - 1; index >= 0; index--) {
         int element = rock.get(index);
         int chamber = dropColumn.get(index) ^ element; //clear chamber
         int drop = dropColumn.get(index + 1);
         int dropped = drop ^ element;
         
         dropColumn.set(index, chamber);
         dropColumn.set(index + 1, dropped);
         
         if ((dropped & drop) == drop) continue;
         
         nextRock();
         return;
      }
      
      int depthIndex = depth;
      
      for (Integer chamber : dropColumn) {
         column.set(depthIndex++, chamber);
      }
      
      if (column.get(0) == 0) column.remove(0); //remove leading empty space from column
      else depth++;
   }
   
   void nextRock() {
      int blockedIndex = column.indexOf(MASK_WIDTH);
      
      if (blockedIndex > 0) {
         height += column.size() - (blockedIndex + 1);
         column = new ArrayList<>(column.subList(0, blockedIndex + 1));
      }
      
      depth = 0;
      rockIndex++;
      column.addAll(0, SPACE3);
      rock = new ArrayList<>(ROCKS.get((int) (rockIndex % ROCKS.size())));
      column.addAll(0, rock);
   }
   
   void print() {
      column.stream()
            .map(Integer::toBinaryString)
            .map(binaryStr -> String.format("%7s", binaryStr)
                  .replace(' ', '.')
                  .replace('0', '.')
                  .replace('1', '#'))
            .forEach(System.out::println);
      System.out.println();
   }
}
