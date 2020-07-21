package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfirmBackupScreen extends Screen {
   @Nullable
   private final Screen parentScreen;
   protected final ConfirmBackupScreen.ICallback callback;
   private final ITextComponent message;
   private final boolean field_212994_d;
   private final List<ITextProperties> wrappedMessage = Lists.newArrayList();
   private CheckboxButton field_212996_j;

   public ConfirmBackupScreen(@Nullable Screen p_i51122_1_, ConfirmBackupScreen.ICallback p_i51122_2_, ITextComponent p_i51122_3_, ITextComponent p_i51122_4_, boolean p_i51122_5_) {
      super(p_i51122_3_);
      this.parentScreen = p_i51122_1_;
      this.callback = p_i51122_2_;
      this.message = p_i51122_4_;
      this.field_212994_d = p_i51122_5_;
   }

   protected void init() {
      super.init();
      this.wrappedMessage.clear();
      this.wrappedMessage.addAll(this.font.func_238425_b_(this.message, this.width - 50));
      int i = (this.wrappedMessage.size() + 1) * 9;
      this.addButton(new Button(this.width / 2 - 155, 100 + i, 150, 20, new TranslationTextComponent("selectWorld.backupJoinConfirmButton"), (p_212993_1_) -> {
         this.callback.proceed(true, this.field_212996_j.isChecked());
      }));
      this.addButton(new Button(this.width / 2 - 155 + 160, 100 + i, 150, 20, new TranslationTextComponent("selectWorld.backupJoinSkipButton"), (p_212992_1_) -> {
         this.callback.proceed(false, this.field_212996_j.isChecked());
      }));
      this.addButton(new Button(this.width / 2 - 155 + 80, 124 + i, 150, 20, DialogTexts.field_240633_d_, (p_212991_1_) -> {
         this.minecraft.displayGuiScreen(this.parentScreen);
      }));
      this.field_212996_j = new CheckboxButton(this.width / 2 - 155 + 80, 76 + i, 150, 20, new TranslationTextComponent("selectWorld.backupEraseCache"), false);
      if (this.field_212994_d) {
         this.addButton(this.field_212996_j);
      }

   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 50, 16777215);
      int i = 70;

      for(ITextProperties itextproperties : this.wrappedMessage) {
         this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
         i += 9;
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (p_231046_1_ == 256) {
         this.minecraft.displayGuiScreen(this.parentScreen);
         return true;
      } else {
         return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public interface ICallback {
      void proceed(boolean p_proceed_1_, boolean p_proceed_2_);
   }
}