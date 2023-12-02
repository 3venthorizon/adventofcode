package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link https://adventofcode.com/2023/day/2}
 */
public class Day2 {
   record Subset(int red, int green, int blue) {}

   record Game(int number, List<Subset> subsets) {}

   static final Pattern PATTERN_DIGITS = Pattern.compile("\\d+");

   public int part1(String fileName) {
      Subset totals = new Subset(12, 13, 14);
      Predicate<Subset> predicate = subset -> subset.red <= totals.red 
            && subset.green <= totals.green
            && subset.blue <= totals.blue;
            
      return FileLineStreamer.read(fileName)
            .map(this::gameMapper)
            .filter(game -> game.subsets.stream().allMatch(predicate))
            .map(Game::number)
            .reduce(Integer::sum)
            .orElse(0);
   }

   public int part2(String fileName) {
      return FileLineStreamer.read(fileName)
            .map(this::gameMapper)
            .map(this::maximum)
            .mapToInt(subset -> subset.red * subset.green * subset.blue)
            .reduce(Integer::sum)
            .orElse(0);
   }

   Game gameMapper(String line) {
      int colonIndex = line.indexOf(':');
      int number = Integer.parseInt(line.substring(5, colonIndex));
      String[] sets = line.trim().substring(colonIndex + 2).split("; ");
      List<Subset> subsets = new ArrayList<>(sets.length);
      Game game = new Game(number, subsets);

      for (String set : sets) {
         int red = 0, green = 0, blue = 0;
         String[] cubes = set.trim().split(", ");

         for (String cube : cubes) {
            if (cube.contains("red")) red = colourCount(cube);
            if (cube.contains("green")) green = colourCount(cube);
            if (cube.contains("blue")) blue = colourCount(cube);
         }

         subsets.add(new Subset(red, green, blue));
      }

      return game;
   }

   int colourCount(String cube) {
      Matcher matcher = PATTERN_DIGITS.matcher(cube);
      return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
   }

   Subset maximum(Game game) {
      int red = 0, green = 0, blue = 0;

      for (Subset subset : game.subsets) {
         red = Math.max(red, subset.red);
         green = Math.max(green, subset.green);
         blue = Math.max(blue, subset.blue);
      }

      return new Subset(red, green, blue);
   }
}
