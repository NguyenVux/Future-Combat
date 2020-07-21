package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DatapackFailureScreen extends Screen {
   private final List<ITextProperties> field_238619_a_ = Lists.newArrayList();
   private final Runnable field_238620_b_;

   public DatapackFailureScreen(Runnable p_i232276_1_) {
      super(new TranslationTextComponent("datapackFailure.title"));
      this.field_238620_b_ = p_i232276_1_;
   }

   protected void init() {
      super.init();
      this.field_238619_a_.clear();
      this.field_238619_a_.addAll(this.font.func_238425_b_(this.getTitle(), this.width - 50));
      this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, new TranslationTextComponent("datapackFailure.safeMode"), (p_238622_1_) -> {
         this.field_238620_b_.run();
      }));
      this.addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, new TranslationTextComponent("gui.toTitle"), (p_238621_1_) -> {
         this.minecraft.displayGuiScreen((Screen)null);
      }));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      int i = 70;

      for(ITextProperties itextproperties : this.field_238619_a_) {
         this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
         i += 9;
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}