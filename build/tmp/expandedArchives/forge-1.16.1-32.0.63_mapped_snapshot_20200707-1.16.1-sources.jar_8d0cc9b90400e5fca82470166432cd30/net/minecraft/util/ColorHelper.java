package net.minecraft.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColorHelper {
   public static class PackedColor {
      @OnlyIn(Dist.CLIENT)
      public static int getAlpha(int packedColorIn) {
         return packedColorIn >>> 24;
      }

      public static int getRed(int packedColorIn) {
         return packedColorIn >> 16 & 255;
      }

      public static int getGreen(int packedColorIn) {
         return packedColorIn >> 8 & 255;
      }

      public static int getBlue(int packedColorIn) {
         return packedColorIn & 255;
      }

      @OnlyIn(Dist.CLIENT)
      public static int packColor(int alphaIn, int redIn, int greenIn, int blueIn) {
         return alphaIn << 24 | redIn << 16 | greenIn << 8 | blueIn;
      }

      @OnlyIn(Dist.CLIENT)
      public static int blendColors(int packedColourOne, int packedColorTwo) {
         return packColor(getAlpha(packedColourOne) * getAlpha(packedColorTwo) / 255, getRed(packedColourOne) * getRed(packedColorTwo) / 255, getGreen(packedColourOne) * getGreen(packedColorTwo) / 255, getBlue(packedColourOne) * getBlue(packedColorTwo) / 255);
      }
   }
}