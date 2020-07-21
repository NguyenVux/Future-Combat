package net.minecraft.item;

public class EnchantedGoldenAppleItem extends Item {
   public EnchantedGoldenAppleItem(Item.Properties p_i50045_1_) {
      super(p_i50045_1_);
   }

   /**
    * Returns true if this item has an enchantment glint. By default
    */
   public boolean hasEffect(ItemStack stack) {
      return true;
   }
}