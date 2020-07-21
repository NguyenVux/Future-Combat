package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.List;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfirmScreen extends Screen {
   private final ITextComponent messageLine2;
   private final List<ITextProperties> listLines = Lists.newArrayList();
   /** The text shown for the first button in GuiYesNo */
   protected ITextComponent confirmButtonText;
   /** The text shown for the second button in GuiYesNo */
   protected ITextComponent cancelButtonText;
   private int ticksUntilEnable;
   protected final BooleanConsumer callbackFunction;

   public ConfirmScreen(BooleanConsumer _callbackFunction, ITextComponent _title, ITextComponent _messageLine2) {
      this(_callbackFunction, _title, _messageLine2, DialogTexts.field_240634_e_, DialogTexts.field_240635_f_);
   }

   public ConfirmScreen(BooleanConsumer p_i232270_1_, ITextComponent p_i232270_2_, ITextComponent p_i232270_3_, ITextComponent p_i232270_4_, ITextComponent p_i232270_5_) {
      super(p_i232270_2_);
      this.callbackFunction = p_i232270_1_;
      this.messageLine2 = p_i232270_3_;
      this.confirmButtonText = p_i232270_4_;
      this.cancelButtonText = p_i232270_5_;
   }

   public String getNarrationMessage() {
      return super.getNarrationMessage() + ". " + this.messageLine2.getString();
   }

   protected void init() {
      super.init();
      this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, this.confirmButtonText, (p_213002_1_) -> {
         this.callbackFunction.accept(true);
      }));
      this.addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, this.cancelButtonText, (p_213001_1_) -> {
         this.callbackFunction.accept(false);
      }));
      this.listLines.clear();
      this.listLines.addAll(this.font.func_238425_b_(this.messageLine2, this.width - 50));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 70, 16777215);
      int i = 90;

      for(ITextProperties itextproperties : this.listLines) {
         this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
         i += 9;
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   /**
    * Sets the number of ticks to wait before enabling the buttons.
    */
   public void setButtonDelay(int ticksUntilEnableIn) {
      this.ticksUntilEnable = ticksUntilEnableIn;

      for(Widget widget : this.buttons) {
         widget.active = false;
      }

   }

   public void tick() {
      super.tick();
      if (--this.ticksUntilEnable == 0) {
         for(Widget widget : this.buttons) {
            widget.active = true;
         }
      }

   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (p_231046_1_ == 256) {
         this.callbackFunction.accept(false);
         return true;
      } else {
         return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
      }
   }
}