package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeathScreen extends Screen {
   /** The integer value containing the number of ticks that have passed since the player's death */
   private int enableButtonsTimer;
   private final ITextComponent causeOfDeath;
   private final boolean isHardcoreMode;

   public DeathScreen(@Nullable ITextComponent p_i51118_1_, boolean p_i51118_2_) {
      super(new TranslationTextComponent(p_i51118_2_ ? "deathScreen.title.hardcore" : "deathScreen.title"));
      this.causeOfDeath = p_i51118_1_;
      this.isHardcoreMode = p_i51118_2_;
   }

   protected void init() {
      this.enableButtonsTimer = 0;
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.isHardcoreMode ? new TranslationTextComponent("deathScreen.spectate") : new TranslationTextComponent("deathScreen.respawn"), (p_213021_1_) -> {
         this.minecraft.player.respawnPlayer();
         this.minecraft.displayGuiScreen((Screen)null);
      }));
      Button button = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 96, 200, 20, new TranslationTextComponent("deathScreen.titleScreen"), (p_213020_1_) -> {
         if (this.isHardcoreMode) {
            confirmCallback(true);
            this.func_228177_a_();
         } else {
            ConfirmScreen confirmscreen = new ConfirmScreen(this::confirmCallback, new TranslationTextComponent("deathScreen.quit.confirm"), StringTextComponent.EMPTY, new TranslationTextComponent("deathScreen.titleScreen"), new TranslationTextComponent("deathScreen.respawn"));
            this.minecraft.displayGuiScreen(confirmscreen);
            confirmscreen.setButtonDelay(20);
         }
      }));
      if (!this.isHardcoreMode && this.minecraft.getSession() == null) {
         button.active = false;
      }

      for(Widget widget : this.buttons) {
         widget.active = false;
      }

   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   private void confirmCallback(boolean p_213022_1_) {
      if (p_213022_1_) {
         this.func_228177_a_();
      } else {
         this.minecraft.player.respawnPlayer();
         this.minecraft.displayGuiScreen((Screen)null);
      }

   }

   private void func_228177_a_() {
      if (this.minecraft.world != null) {
         this.minecraft.world.sendQuittingDisconnectingPacket();
      }

      this.minecraft.unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
      this.minecraft.displayGuiScreen(new MainMenuScreen());
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.fillGradient(p_230430_1_, 0, 0, this.width, this.height, 1615855616, -1602211792);
      RenderSystem.pushMatrix();
      RenderSystem.scalef(2.0F, 2.0F, 2.0F);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2 / 2, 30, 16777215);
      RenderSystem.popMatrix();
      if (this.causeOfDeath != null) {
         this.drawCenteredString(p_230430_1_, this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
      }

      this.drawCenteredString(p_230430_1_, this.font, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.minecraft.player.getScore(), this.width / 2, 100, 16777215);
      if (this.causeOfDeath != null && p_230430_3_ > 85 && p_230430_3_ < 85 + 9) {
         Style style = this.func_238623_a_(p_230430_2_);
         this.renderComponentHoverEffect(p_230430_1_, style, p_230430_2_, p_230430_3_);
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   @Nullable
   private Style func_238623_a_(int p_238623_1_) {
      if (this.causeOfDeath == null) {
         return null;
      } else {
         int i = this.minecraft.fontRenderer.func_238414_a_(this.causeOfDeath);
         int j = this.width / 2 - i / 2;
         int k = this.width / 2 + i / 2;
         return p_238623_1_ >= j && p_238623_1_ <= k ? this.minecraft.fontRenderer.func_238420_b_().func_238357_a_(this.causeOfDeath, p_238623_1_ - j) : null;
      }
   }

   public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
      if (this.causeOfDeath != null && p_231044_3_ > 85.0D && p_231044_3_ < (double)(85 + 9)) {
         Style style = this.func_238623_a_((int)p_231044_1_);
         if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
            this.handleComponentClicked(style);
            return false;
         }
      }

      return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void tick() {
      super.tick();
      ++this.enableButtonsTimer;
      if (this.enableButtonsTimer == 20) {
         for(Widget widget : this.buttons) {
            widget.active = true;
         }
      }

   }
}