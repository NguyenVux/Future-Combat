package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ServerListScreen extends Screen {
   private Button field_195170_a;
   private final ServerData serverData;
   private TextFieldWidget ipEdit;
   private final BooleanConsumer field_213027_d;
   private final Screen field_228178_e_;

   public ServerListScreen(Screen p_i225926_1_, BooleanConsumer p_i225926_2_, ServerData p_i225926_3_) {
      super(new TranslationTextComponent("selectServer.direct"));
      this.field_228178_e_ = p_i225926_1_;
      this.serverData = p_i225926_3_;
      this.field_213027_d = p_i225926_2_;
   }

   public void tick() {
      this.ipEdit.tick();
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (this.getFocused() != this.ipEdit || p_231046_1_ != 257 && p_231046_1_ != 335) {
         return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
      } else {
         this.func_195167_h();
         return true;
      }
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.field_195170_a = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20, new TranslationTextComponent("selectServer.select"), (p_213026_1_) -> {
         this.func_195167_h();
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, DialogTexts.field_240633_d_, (p_213025_1_) -> {
         this.field_213027_d.accept(false);
      }));
      this.ipEdit = new TextFieldWidget(this.font, this.width / 2 - 100, 116, 200, 20, new TranslationTextComponent("addServer.enterIp"));
      this.ipEdit.setMaxStringLength(128);
      this.ipEdit.setFocused2(true);
      this.ipEdit.setText(this.minecraft.gameSettings.lastServer);
      this.ipEdit.setResponder((p_213024_1_) -> {
         this.func_195168_i();
      });
      this.children.add(this.ipEdit);
      this.setFocusedDefault(this.ipEdit);
      this.func_195168_i();
   }

   public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
      String s = this.ipEdit.getText();
      this.init(p_231152_1_, p_231152_2_, p_231152_3_);
      this.ipEdit.setText(s);
   }

   private void func_195167_h() {
      this.serverData.serverIP = this.ipEdit.getText();
      this.field_213027_d.accept(true);
   }

   public void onClose() {
      this.minecraft.displayGuiScreen(this.field_228178_e_);
   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
      this.minecraft.gameSettings.lastServer = this.ipEdit.getText();
      this.minecraft.gameSettings.saveOptions();
   }

   private void func_195168_i() {
      String s = this.ipEdit.getText();
      this.field_195170_a.active = !s.isEmpty() && s.split(":").length > 0 && s.indexOf(32) == -1;
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 20, 16777215);
      this.drawString(p_230430_1_, this.font, I18n.format("addServer.enterIp"), this.width / 2 - 100, 100, 10526880);
      this.ipEdit.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}