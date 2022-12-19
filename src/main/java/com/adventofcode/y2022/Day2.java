package com.adventofcode.y2022;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.opencsv.CSVReader;

public class Day2 {
   
   static final Long POINTS_ROCK = 1L;
   static final Long POINTS_PAPER = 2L;
   static final Long POINTS_SCISSORS = 3L;
   static final Long POINTS_LOSS = 0L;
   static final Long POINTS_DRAW = 3L;
   static final Long POINTS_WIN = 6L;
   
   static final Map<String, Long> MAP_OUTCOME1;
   static final Map<String, Long> MAP_POINTS1;
   static final Map<String, Long> MAP_OUTCOME2;
   static final Map<String, Long> MAP_POINTS2;
   
   static {
      {
         Map<String, Long> map = new HashMap<>();
         
         map.put("A Y", POINTS_WIN);
         map.put("B Z", POINTS_WIN);
         map.put("C X", POINTS_WIN);
         map.put("A X", POINTS_DRAW);
         map.put("B Y", POINTS_DRAW);
         map.put("C Z", POINTS_DRAW);
         map.put("A Z", POINTS_LOSS);
         map.put("B X", POINTS_LOSS);
         map.put("C Y", POINTS_LOSS);
         
         MAP_OUTCOME1 = Collections.unmodifiableMap(map);
      }
      
      {
         Map<String, Long> map = new HashMap<>();
         
         map.put("A Y", POINTS_PAPER);
         map.put("B Z", POINTS_SCISSORS);
         map.put("C X", POINTS_ROCK);
         map.put("A X", POINTS_ROCK);
         map.put("B Y", POINTS_PAPER);
         map.put("C Z", POINTS_SCISSORS);
         map.put("A Z", POINTS_SCISSORS);
         map.put("B X", POINTS_ROCK);
         map.put("C Y", POINTS_PAPER);
         
         MAP_POINTS1 = Collections.unmodifiableMap(map);
      }
      
      {
         Map<String, Long> map = new HashMap<>();
         
         map.put("A X", POINTS_LOSS);
         map.put("B X", POINTS_LOSS);
         map.put("C X", POINTS_LOSS);
         map.put("A Y", POINTS_DRAW);
         map.put("B Y", POINTS_DRAW);
         map.put("C Y", POINTS_DRAW);
         map.put("A Z", POINTS_WIN);
         map.put("B Z", POINTS_WIN);
         map.put("C Z", POINTS_WIN);
         
         MAP_OUTCOME2 = Collections.unmodifiableMap(map);
      }
      
      {
         Map<String, Long> map = new HashMap<>();
         
         map.put("A X", POINTS_SCISSORS);
         map.put("B X", POINTS_ROCK);
         map.put("C X", POINTS_PAPER);
         map.put("A Y", POINTS_ROCK);
         map.put("B Y", POINTS_PAPER);
         map.put("C Y", POINTS_SCISSORS);
         map.put("A Z", POINTS_PAPER);
         map.put("B Z", POINTS_SCISSORS);
         map.put("C Z", POINTS_ROCK);
         
         MAP_POINTS2 = Collections.unmodifiableMap(map);
      }
   }

   CSVReader createCsvReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day2.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new CSVReader(reader);
   }
   
   public long calculateScore1() throws IOException, URISyntaxException {
      return calculateScore(MAP_OUTCOME1, MAP_POINTS1);
   }
   
   public long calculateScore2() throws IOException, URISyntaxException {
      return calculateScore(MAP_OUTCOME2, MAP_POINTS2);
   }
   
   long calculateScore(Map<String, Long> outcomeMap, Map<String, Long> pointsMap) 
         throws IOException, URISyntaxException {
      long points = 0L;
      
      try (CSVReader csvReader = createCsvReader()) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 1) continue;
            
            points += calculatePoints(outcomeMap, pointsMap, fields[0]);
         }
      }
      
      return points;
   }
   
   long calculatePoints(Map<String, Long> outcomeMap, Map<String, Long> pointsMap, String game) {
      Long outcome = outcomeMap.get(game);
      Long points = pointsMap.get(game);
      
      if (outcome == null || points == null) return 0L;
      
      return outcome + points;
   }
}
