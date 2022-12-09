package com.adventofcode;

import java.io.IOException;
import java.io.InputStream;

public class Day6 {

   public long distinctPosition(int count) throws IOException {
      int position = 0;
      ClassLoader classLoader = getClass().getClassLoader();
      byte[] buffer = new byte[count]; //cyclic buffer
      
      try (InputStream is = classLoader.getResourceAsStream("day6.input")) {
         int read = -1;
         while (position < buffer.length) {
            read = is.read(buffer, position, buffer.length - position);
            if (read < 0) throw new IOException("End of Stream");
            position += read;
         }
         
         while (!isStart(buffer)) {
            read = is.read();
            if (read < 0) throw new IOException("End of Stream");
            buffer[position % buffer.length] = (byte) read;
            position++;
         }
      }
      
      return position;
   }
   
   boolean isStart(byte[] buffer) {
      for (int x = 0; x < buffer.length; x++) {
         for (int y = x + 1; y < buffer.length; y++) {
            if (buffer[x] == buffer[y]) return false;
         }
      }
      
      return true;
   }
}
