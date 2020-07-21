package net.minecraft.entity;

public interface IProjectile {
   /**
    * Similar to setArrowHeading
    */
   void shoot(double x, double y, double z, float velocity, float inaccuracy);
}