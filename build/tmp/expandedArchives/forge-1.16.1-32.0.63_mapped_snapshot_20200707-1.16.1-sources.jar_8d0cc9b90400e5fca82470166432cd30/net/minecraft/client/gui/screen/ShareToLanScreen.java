package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.HTTPUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShareToLanScreen extends Screen {
   private final Screen lastScreen;
   private Button allowCheatsButton;
   private Button gameModeButton;
   private String gameMode = "survival";
   private boolean allowCheats;

   public ShareToLanScreen(Screen lastScreenIn) {
      super(new TranslationTextComponent("lanServer.title"));
      this.lastScreen = lastScreenIn;
   }

   protected void init() {
      this.addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, new TranslationTextComponent("lanServer.start"), (p_213082_1_) -> {
         this.minecraft.displayGuiScreen((Screen)null);
         int i = HTTPUtil.getSuitableLanPort();
         ITextComponent itextcomponent;
         if (this.minecraft.getIntegratedServer().shareToLAN(GameType.getByName(this.gameMode), this.allowCheats, i)) {
            itextcomponent = new TranslationTextComponent("commands.publish.started", i);
         } else {
            itextcomponent = new TranslationTextComponent("commands.publish.failed");
         }

         this.minecraft.ingameGUI.getChatGUI().printChatMessage(itextcomponent);
         this.minecraft.func_230150_b_();
      }));
      this.addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, DialogTexts.field_240633_d_, (p_213085_1_) -> {
         this.minecraft.displayGuiScreen(this.lastScreen);
      }));
      this.gameModeButton = this.addButton(new Button(this.width / 2 - 155, 100, 150, 20, new TranslationTextComponent("selectWorld.gameMode"), (p_213084_1_) -> {
         if ("spectator".equals(this.gameMode)) {
            this.gameMode = "creative";
         } else if ("creative".equals(this.gameMode)) {
            this.gameMode = "adventure";
         } else if ("adventure".equals(this.gameMode)) {
            this.gameMode = "survival";
         } else {
            this.gameMode = "spectator";
         }

         this.updateDisplayNames();
      }));
      this.allowCheatsButton = this.addButton(new Button(this.width / 2 + 5, 100, 150, 20, new TranslationTextComponent("selectWorld.allowCommands"), (p_213083_1_) -> {
         this.allowCheats = !this.allowCheats;
         this.updateDisplayNames();
      }));
      this.updateDisplayNames();
   }

   private void updateDisplayNames() {
      this.gameModeButton.setMessage((new TranslationTextComponent("selectWorld.gameMode")).func_240702_b_(": ").func_230529_a_(new TranslationTextComponent("selectWorld.gameMode." + this.gameMode)));
      this.allowCheatsButton.setMessage((new TranslationTextComponent("selectWorld.allowCommands")).func_240702_b_(" ").func_230529_a_(DialogTexts.func_240638_a_(this.allowCheats)));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 50, 16777215);
      this.drawCenteredString(p_230430_1_, this.font, I18n.format("lanServer.otherPlayers"), this.width / 2, 82, 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}