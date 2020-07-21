package net.minecraft.client.gui.screen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SleepInMultiplayerScreen extends ChatScreen {
   public SleepInMultiplayerScreen() {
      super("");
   }

   protected void init() {
      super.init();
      this.addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, new TranslationTextComponent("multiplayer.stopSleeping"), (p_212998_1_) -> {
         this.wakeFromSleep();
      }));
   }

   public void onClose() {
      this.wakeFromSleep();
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (p_231046_1_ == 256) {
         this.wakeFromSleep();
      } else if (p_231046_1_ == 257 || p_231046_1_ == 335) {
         String s = this.inputField.getText().trim();
         if (!s.isEmpty()) {
            this.sendMessage(s);
         }

         this.inputField.setText("");
         this.minecraft.ingameGUI.getChatGUI().resetScroll();
         return true;
      }

      return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
   }

   private void wakeFromSleep() {
      ClientPlayNetHandler clientplaynethandler = this.minecraft.player.connection;
      clientplaynethandler.sendPacket(new CEntityActionPacket(this.minecraft.player, CEntityActionPacket.Action.STOP_SLEEPING));
   }
}