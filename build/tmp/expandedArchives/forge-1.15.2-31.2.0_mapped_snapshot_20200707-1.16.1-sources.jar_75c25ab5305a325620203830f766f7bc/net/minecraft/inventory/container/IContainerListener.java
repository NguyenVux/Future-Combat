package net.minecraft.inventory.container;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IContainerListener {
   /**
    * update the crafting window inventory with the items in the list
    */
   void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList);

   /**
    * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
    * contents of that slot.
    */
   void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack);

   /**
    * Sends two ints to the client-side Container. Used for furnace burning time
    */
   void sendWindowProperty(Container containerIn, int varToUpdate, int newValue);
}