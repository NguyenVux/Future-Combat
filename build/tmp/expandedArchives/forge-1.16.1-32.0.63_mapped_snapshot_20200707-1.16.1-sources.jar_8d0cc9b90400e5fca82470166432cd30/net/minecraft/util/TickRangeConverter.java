package net.minecraft.util;

public class TickRangeConverter {
   public static RangedInteger convertRange(int minIn, int maxIn) {
      return new RangedInteger(minIn * 20, maxIn * 20);
   }
}