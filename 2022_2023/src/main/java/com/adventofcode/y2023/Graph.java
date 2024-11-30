package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Graph {
   static final int BFS_LOAD_FACTOR = Runtime.getRuntime().availableProcessors() * 16;
   
   private Graph() {}
   
   public static <T, R extends Collection<T>> List<T> breadthFirstSearch(T start, 
         Function<T, R> routes,
         Predicate<T> destination) {
      Map<T, T> routeMap = new HashMap<>();
      routeMap.put(start, start);
      
      return bfs(routes, destination, Set.of(start), routeMap);
   }
   
   static <T, R extends Collection<T>> List<T> bfs(Function<T, R> routes, Predicate<T> destination, 
         Set<T> optionSet, Map<T, T> routeMap) {
      Stream<T> stream = optionSet.size() < BFS_LOAD_FACTOR ? optionSet.stream() : optionSet.parallelStream();
      Set<T> nextSet = stream
            .map(location -> bfsNext(location, routes, destination, routeMap))
            .flatMap(List::stream)
            .collect(Collectors.toSet());
      stream = nextSet.size() < BFS_LOAD_FACTOR ? nextSet.stream() : nextSet.parallelStream();
      Optional<T> found = stream.filter(destination).findFirst();
      
      if (found.isPresent()) return traceRoute(routeMap, found.get()); 
      if (nextSet.isEmpty()) return new ArrayList<>(routeMap.keySet());
      return bfs(routes, destination, nextSet, routeMap);
   }
   
   static <T, R extends Collection<T>> List<T> bfsNext(T location, Function<T, R> routes, Predicate<T> destination, 
         Map<T, T> routeMap) {
      List<T> nextList = new ArrayList<>();
      
      for (T next : routes.apply(location)) {
         T existing = routeMap.putIfAbsent(next, location);
         if (existing != null) continue;
         
         nextList.add(next);
         if (destination.test(next)) break;
      }
      
      return nextList;
   }
   
   static <T> List<T> traceRoute(Map<T, T> routeMap, T location) {
      T source = routeMap.get(location);
      List<T> traceRoute = new ArrayList<>();
      traceRoute.add(location);

      while (source != null && source != location) {
         traceRoute.add(0, source);
         location = source;
         source = routeMap.get(location);
      }
      
      return traceRoute;
   }
}
