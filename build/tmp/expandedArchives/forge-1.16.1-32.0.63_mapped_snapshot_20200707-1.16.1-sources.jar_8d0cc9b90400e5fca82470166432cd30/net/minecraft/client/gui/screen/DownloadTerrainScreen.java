package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DownloadTerrainScreen extends Screen {
   public DownloadTerrainScreen() {
      super(NarratorChatListener.EMPTY);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderDirtBackground(0);
      this.drawCenteredString(p_230430_1_, this.font, I18n.format("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public boolean isPauseScreen() {
      return false;
   }
}