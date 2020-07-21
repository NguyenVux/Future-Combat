package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.action.ConnectingToRealmsAction;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class RealmsTermsScreen extends RealmsScreen {
   private static final Logger field_224722_a = LogManager.getLogger();
   private final Screen field_224723_b;
   private final RealmsMainScreen field_224724_c;
   /**
    * The screen to display when OK is clicked on the disconnect screen.
    *  
    * Seems to be either null (integrated server) or an instance of either {@link MultiplayerScreen} (when connecting to
    * a server) or {@link com.mojang.realmsclient.gui.screens.RealmsTermsScreen} (when connecting to MCO server)
    */
   private final RealmsServer guiScreenServer;
   private boolean field_224727_f;
   private final String field_224728_g = "https://minecraft.net/realms/terms";

   public RealmsTermsScreen(Screen p_i232225_1_, RealmsMainScreen p_i232225_2_, RealmsServer p_i232225_3_) {
      this.field_224723_b = p_i232225_1_;
      this.field_224724_c = p_i232225_2_;
      this.guiScreenServer = p_i232225_3_;
   }

   public void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      int i = this.width / 4 - 2;
      this.addButton(new Button(this.width / 4, func_239562_k_(12), i, 20, new TranslationTextComponent("mco.terms.buttons.agree"), (p_238078_1_) -> {
         this.func_224721_a();
      }));
      this.addButton(new Button(this.width / 2 + 4, func_239562_k_(12), i, 20, new TranslationTextComponent("mco.terms.buttons.disagree"), (p_238077_1_) -> {
         this.minecraft.displayGuiScreen(this.field_224723_b);
      }));
   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (p_231046_1_ == 256) {
         this.minecraft.displayGuiScreen(this.field_224723_b);
         return true;
      } else {
         return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
      }
   }

   private void func_224721_a() {
      RealmsClient realmsclient = RealmsClient.func_224911_a();

      try {
         realmsclient.func_224937_l();
         this.minecraft.displayGuiScreen(new RealmsLongRunningMcoTaskScreen(this.field_224723_b, new ConnectingToRealmsAction(this.field_224724_c, this.field_224723_b, this.guiScreenServer, new ReentrantLock())));
      } catch (RealmsServiceException realmsserviceexception) {
         field_224722_a.error("Couldn't agree to TOS");
      }

   }

   public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
      if (this.field_224727_f) {
         this.minecraft.keyboardListener.setClipboardString("https://minecraft.net/realms/terms");
         Util.getOSType().openURI("https://minecraft.net/realms/terms");
         return true;
      } else {
         return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
      }
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, I18n.format("mco.terms.title"), this.width / 2, 17, 16777215);
      this.font.drawString(p_230430_1_, I18n.format("mco.terms.sentence.1"), (float)(this.width / 2 - 120), (float)func_239562_k_(5), 16777215);
      int i = this.font.getStringWidth(I18n.format("mco.terms.sentence.1"));
      int j = this.width / 2 - 121 + i;
      int k = func_239562_k_(5);
      int l = j + this.font.getStringWidth("mco.terms.sentence.2") + 1;
      int i1 = k + 1 + 9;
      if (j <= p_230430_2_ && p_230430_2_ <= l && k <= p_230430_3_ && p_230430_3_ <= i1) {
         this.field_224727_f = true;
         this.font.drawString(p_230430_1_, " " + I18n.format("mco.terms.sentence.2"), (float)(this.width / 2 - 120 + i), (float)func_239562_k_(5), 7107012);
      } else {
         this.field_224727_f = false;
         this.font.drawString(p_230430_1_, " " + I18n.format("mco.terms.sentence.2"), (float)(this.width / 2 - 120 + i), (float)func_239562_k_(5), 3368635);
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}