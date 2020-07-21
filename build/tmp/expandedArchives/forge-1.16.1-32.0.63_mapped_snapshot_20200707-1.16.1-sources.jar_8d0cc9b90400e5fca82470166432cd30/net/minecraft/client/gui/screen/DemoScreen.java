package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DemoScreen extends Screen {
   private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");

   public DemoScreen() {
      super(new TranslationTextComponent("demo.help.title"));
   }

   protected void init() {
      int i = -16;
      this.addButton(new Button(this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20, new TranslationTextComponent("demo.help.buy"), (p_213019_0_) -> {
         p_213019_0_.active = false;
         Util.getOSType().openURI("http://www.minecraft.net/store?source=demo");
      }));
      this.addButton(new Button(this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20, new TranslationTextComponent("demo.help.later"), (p_213018_1_) -> {
         this.minecraft.displayGuiScreen((Screen)null);
         this.minecraft.mouseHelper.grabMouse();
      }));
   }

   public void renderBackground(MatrixStack p_230446_1_) {
      super.renderBackground(p_230446_1_);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.minecraft.getTextureManager().bindTexture(DEMO_BACKGROUND_LOCATION);
      int i = (this.width - 248) / 2;
      int j = (this.height - 166) / 2;
      this.blit(p_230446_1_, i, j, 0, 0, 248, 166);
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      int i = (this.width - 248) / 2 + 10;
      int j = (this.height - 166) / 2 + 8;
      this.font.func_238422_b_(p_230430_1_, this.title, (float)i, (float)j, 2039583);
      j = j + 12;
      GameSettings gamesettings = this.minecraft.gameSettings;
      this.font.func_238422_b_(p_230430_1_, new TranslationTextComponent("demo.help.movementShort", gamesettings.keyBindForward.func_238171_j_(), gamesettings.keyBindLeft.func_238171_j_(), gamesettings.keyBindBack.func_238171_j_(), gamesettings.keyBindRight.func_238171_j_()), (float)i, (float)j, 5197647);
      this.font.func_238422_b_(p_230430_1_, new TranslationTextComponent("demo.help.movementMouse"), (float)i, (float)(j + 12), 5197647);
      this.font.func_238422_b_(p_230430_1_, new TranslationTextComponent("demo.help.jump", gamesettings.keyBindJump.func_238171_j_()), (float)i, (float)(j + 24), 5197647);
      this.font.func_238422_b_(p_230430_1_, new TranslationTextComponent("demo.help.inventory", gamesettings.keyBindInventory.func_238171_j_()), (float)i, (float)(j + 36), 5197647);
      this.font.func_238418_a_(new TranslationTextComponent("demo.help.fullWrapped"), i, j + 68, 218, 2039583);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}