package net.minecraft.network;

import net.minecraft.util.text.ITextComponent;

public interface INetHandler {
   /**
    * Invoked when disconnecting
    */
   void onDisconnect(ITextComponent reason);

   /**
    * Returns this the NetworkManager instance registered with this NetworkHandlerPlayClient
    */
   NetworkManager getNetworkManager();
}