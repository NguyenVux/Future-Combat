package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DisconnectedScreen extends Screen {
   private final ITextComponent message;
   @Nullable
   private List<ITextProperties> multilineMessage;
   private final Screen nextScreen;
   private int textHeight;

   public DisconnectedScreen(Screen screen, String reasonLocalizationKey, ITextComponent chatComp) {
      super(new TranslationTextComponent(reasonLocalizationKey));
      this.nextScreen = screen;
      this.message = chatComp;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   protected void init() {
      this.multilineMessage = this.font.func_238425_b_(this.message, this.width - 50);
      this.textHeight = this.multilineMessage.size() * 9;
      this.addButton(new Button(this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + 9, this.height - 30), 200, 20, new TranslationTextComponent("gui.toMenu"), (p_213033_1_) -> {
         this.minecraft.displayGuiScreen(this.nextScreen);
      }));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, this.height / 2 - this.textHeight / 2 - 9 * 2, 11184810);
      int i = this.height / 2 - this.textHeight / 2;
      if (this.multilineMessage != null) {
         for(ITextProperties itextproperties : this.multilineMessage) {
            this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
            i += 9;
         }
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}