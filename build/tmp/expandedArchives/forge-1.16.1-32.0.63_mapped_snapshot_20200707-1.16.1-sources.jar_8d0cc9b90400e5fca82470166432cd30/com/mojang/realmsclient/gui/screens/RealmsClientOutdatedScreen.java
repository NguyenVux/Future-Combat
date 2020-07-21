package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RealmsClientOutdatedScreen extends RealmsScreen {
   private final Screen field_224129_a;
   private final boolean field_224130_b;

   public RealmsClientOutdatedScreen(Screen p_i232201_1_, boolean p_i232201_2_) {
      this.field_224129_a = p_i232201_1_;
      this.field_224130_b = p_i232201_2_;
   }

   public void init() {
      this.addButton(new Button(this.width / 2 - 100, func_239562_k_(12), 200, 20, DialogTexts.field_240637_h_, (p_237786_1_) -> {
         this.minecraft.displayGuiScreen(this.field_224129_a);
      }));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      ITextComponent itextcomponent = new TranslationTextComponent(this.field_224130_b ? "mco.client.outdated.title" : "mco.client.incompatible.title");
      this.drawCenteredString(p_230430_1_, this.font, itextcomponent, this.width / 2, func_239562_k_(3), 16711680);
      int i = this.field_224130_b ? 2 : 3;

      for(int j = 0; j < i; ++j) {
         String s = (this.field_224130_b ? "mco.client.outdated.msg.line" : "mco.client.incompatible.msg.line") + (j + 1);
         this.drawCenteredString(p_230430_1_, this.font, new TranslationTextComponent(s), this.width / 2, func_239562_k_(5) + j * 12, 16777215);
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (p_231046_1_ != 257 && p_231046_1_ != 335 && p_231046_1_ != 256) {
         return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
      } else {
         this.minecraft.displayGuiScreen(this.field_224129_a);
         return true;
      }
   }
}