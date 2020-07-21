package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MultiplayerWarningScreen extends Screen {
   private final Screen field_230156_a_;
   private static final ITextComponent field_230157_b_ = (new TranslationTextComponent("multiplayerWarning.header")).func_240699_a_(TextFormatting.BOLD);
   private static final ITextComponent field_230158_c_ = new TranslationTextComponent("multiplayerWarning.message");
   private static final ITextComponent field_230159_d_ = new TranslationTextComponent("multiplayerWarning.check");
   private static final ITextComponent field_238858_q_ = field_230157_b_.deepCopy().func_240702_b_("\n").func_230529_a_(field_230158_c_);
   private CheckboxButton field_230162_g_;
   private final List<ITextProperties> field_230163_h_ = Lists.newArrayList();

   public MultiplayerWarningScreen(Screen p_i230052_1_) {
      super(NarratorChatListener.EMPTY);
      this.field_230156_a_ = p_i230052_1_;
   }

   protected void init() {
      super.init();
      this.field_230163_h_.clear();
      this.field_230163_h_.addAll(this.font.func_238425_b_(field_230158_c_, this.width - 50));
      int i = (this.field_230163_h_.size() + 1) * 9;
      this.addButton(new Button(this.width / 2 - 155, 100 + i, 150, 20, DialogTexts.field_240636_g_, (p_230165_1_) -> {
         if (this.field_230162_g_.isChecked()) {
            this.minecraft.gameSettings.field_230152_Z_ = true;
            this.minecraft.gameSettings.saveOptions();
         }

         this.minecraft.displayGuiScreen(new MultiplayerScreen(this.field_230156_a_));
      }));
      this.addButton(new Button(this.width / 2 - 155 + 160, 100 + i, 150, 20, DialogTexts.field_240637_h_, (p_230164_1_) -> {
         this.minecraft.displayGuiScreen(this.field_230156_a_);
      }));
      this.field_230162_g_ = new CheckboxButton(this.width / 2 - 155 + 80, 76 + i, 150, 20, field_230159_d_, false);
      this.addButton(this.field_230162_g_);
   }

   public String getNarrationMessage() {
      return field_238858_q_.getString();
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderDirtBackground(0);
      this.drawCenteredString(p_230430_1_, this.font, field_230157_b_, this.width / 2, 30, 16777215);
      int i = 70;

      for(ITextProperties itextproperties : this.field_230163_h_) {
         this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
         i += 9;
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}