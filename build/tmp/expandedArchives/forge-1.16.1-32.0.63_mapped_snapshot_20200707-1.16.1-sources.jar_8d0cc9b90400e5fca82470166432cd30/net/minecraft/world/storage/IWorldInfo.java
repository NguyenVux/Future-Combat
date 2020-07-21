package net.minecraft.world.storage;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public interface IWorldInfo {
   /**
    * Returns the x spawn position
    */
   int getSpawnX();

   /**
    * Return the Y axis spawning point of the player.
    */
   int getSpawnY();

   /**
    * Returns the z spawn position
    */
   int getSpawnZ();

   long getGameTime();

   /**
    * Get current world time
    */
   long getDayTime();

   /**
    * Returns true if it is thundering
    */
   boolean isThundering();

   /**
    * Returns true if it is raining
    */
   boolean isRaining();

   /**
    * Sets whether it is raining or not.
    */
   void setRaining(boolean isRaining);

   /**
    * Returns true if hardcore mode is enabled
    */
   boolean isHardcore();

   /**
    * Gets the GameRules class Instance.
    */
   GameRules getGameRulesInstance();

   Difficulty getDifficulty();

   boolean isDifficultyLocked();

   /**
    * Adds this WorldInfo instance to the crash report.
    */
   default void addToCrashReport(CrashReportCategory category) {
      category.addDetail("Level spawn location", () -> {
         return CrashReportCategory.getCoordinateInfo(this.getSpawnX(), this.getSpawnY(), this.getSpawnZ());
      });
      category.addDetail("Level time", () -> {
         return String.format("%d game time, %d day time", this.getGameTime(), this.getDayTime());
      });
   }
}