package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nullable;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WorkingScreen extends Screen implements IProgressUpdate {
   @Nullable
   private ITextComponent field_238648_a_;
   @Nullable
   private ITextComponent stage;
   private int progress;
   private boolean doneWorking;

   public WorkingScreen() {
      super(NarratorChatListener.EMPTY);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void displaySavingString(ITextComponent component) {
      this.resetProgressAndMessage(component);
   }

   public void resetProgressAndMessage(ITextComponent component) {
      this.field_238648_a_ = component;
      this.displayLoadingString(new TranslationTextComponent("progress.working"));
   }

   public void displayLoadingString(ITextComponent component) {
      this.stage = component;
      this.setLoadingProgress(0);
   }

   /**
    * Updates the progress bar on the loading screen to the specified amount.
    */
   public void setLoadingProgress(int progress) {
      this.progress = progress;
   }

   public void setDoneWorking() {
      this.doneWorking = true;
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      if (this.doneWorking) {
         if (!this.minecraft.isConnectedToRealms()) {
            this.minecraft.displayGuiScreen((Screen)null);
         }

      } else {
         this.renderBackground(p_230430_1_);
         if (this.field_238648_a_ != null) {
            this.drawCenteredString(p_230430_1_, this.font, this.field_238648_a_, this.width / 2, 70, 16777215);
         }

         if (this.stage != null && this.progress != 0) {
            this.drawCenteredString(p_230430_1_, this.font, (new StringTextComponent("")).func_230529_a_(this.stage).func_240702_b_(" " + this.progress + "%"), this.width / 2, 90, 16777215);
         }

         super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      }
   }
}