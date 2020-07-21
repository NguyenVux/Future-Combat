package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RealmsConfirmScreen extends RealmsScreen {
   protected BooleanConsumer field_237824_a_;
   private final ITextComponent field_224142_b;
   private final ITextComponent field_224146_f;
   private int field_224147_g;

   public RealmsConfirmScreen(BooleanConsumer p_i232202_1_, ITextComponent p_i232202_2_, ITextComponent p_i232202_3_) {
      this.field_237824_a_ = p_i232202_1_;
      this.field_224142_b = p_i232202_2_;
      this.field_224146_f = p_i232202_3_;
   }

   public void init() {
      this.addButton(new Button(this.width / 2 - 105, func_239562_k_(9), 100, 20, DialogTexts.field_240634_e_, (p_237826_1_) -> {
         this.field_237824_a_.accept(true);
      }));
      this.addButton(new Button(this.width / 2 + 5, func_239562_k_(9), 100, 20, DialogTexts.field_240635_f_, (p_237825_1_) -> {
         this.field_237824_a_.accept(false);
      }));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.field_224142_b, this.width / 2, func_239562_k_(3), 16777215);
      this.drawCenteredString(p_230430_1_, this.font, this.field_224146_f, this.width / 2, func_239562_k_(5), 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public void tick() {
      super.tick();
      if (--this.field_224147_g == 0) {
         for(Widget widget : this.buttons) {
            widget.active = true;
         }
      }

   }
}