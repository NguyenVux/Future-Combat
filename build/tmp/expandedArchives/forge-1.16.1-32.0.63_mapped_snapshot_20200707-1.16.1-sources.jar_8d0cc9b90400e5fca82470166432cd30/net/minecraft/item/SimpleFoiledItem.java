package net.minecraft.item;

public class SimpleFoiledItem extends Item {
   public SimpleFoiledItem(Item.Properties builder) {
      super(builder);
   }

   /**
    * Returns true if this item has an enchantment glint. By default
    */
   public boolean hasEffect(ItemStack stack) {
      return true;
   }
}