package com.adventofcode.y2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {
   record Options(String left, String right) {}

   static final Pattern PATTERN_SPLITTER = Pattern.compile("(\\s=\\s\\(|,\\s|\\))");
   static final int INDEX_NODE = 0;
   static final int INDEX_LEFT = 1;
   static final int INDEX_RIGTT = 2;

   public long part1(String fileName) {
      String directions = FileLineStreamer.read(fileName).findFirst().orElse("");
      Map<String, Options> optionMap = new HashMap<>();
      FileLineStreamer.read(fileName).skip(2L).forEach(line -> optionLoader(line, optionMap));
      int moves = 0;
      int count = directions.length();
      String node = "AAA";
      String end = "ZZZ";

      do {
         Options options = optionMap.get(node);
         node = directions.charAt(moves % count) == 'L' ? options.left : options.right;
         moves++;
      } while (!end.equals(node));

      return moves;
   }

   public long part2(String fileName) {
      String directions = FileLineStreamer.read(fileName).findFirst().orElse("");
      Map<String, Options> optionMap = new HashMap<>();
      FileLineStreamer.read(fileName).skip(2L).forEach(line -> optionLoader(line, optionMap));
      int count = directions.length();
      String start = "A";
      String end = "Z";
      List<String> nodes = optionMap.keySet().stream()
            .filter(node -> node.endsWith(start))
            .collect(Collectors.toList());
      long[] nodeMoves = new long[nodes.size()];
      
      for (int index = 0, length = nodes.size(); index < length; index++) {
         int moves = 0;
         String node = nodes.get(index);

         do {
            Options options = optionMap.get(node);
            node = directions.charAt(moves % count) == 'L' ? options.left : options.right;
            moves++;
         } while (!node.endsWith(end));
         
         nodeMoves[index] = moves;
      }

      return lcm(nodeMoves[0], nodeMoves);
   }

   void optionLoader(String line, Map<String, Options> optionMap) {
      if (line.isBlank()) return;
      String[] nodeOptions = PATTERN_SPLITTER.split(line);
      if (nodeOptions.length < 3) return;

      optionMap.put(nodeOptions[INDEX_NODE], new Options(nodeOptions[INDEX_LEFT], nodeOptions[INDEX_RIGTT]));
   }

   public static long lcm(long left, long... rights) {
      long[] values = new long[rights.length + 1];
      values[0] = left;
      System.arraycopy(rights, 0, values, 1, rights.length);
      Arrays.sort(values);
      long lcm = values[rights.length];
      boolean divisible = false;

      while (!divisible) {
         divisible = true;

         for (int index = 0; index < rights.length; index++) {
            divisible = lcm % values[index] == 0;

            if (!divisible) {
               lcm += values[rights.length];
               break;
            }
         }
      }

      return lcm;
   }
}
