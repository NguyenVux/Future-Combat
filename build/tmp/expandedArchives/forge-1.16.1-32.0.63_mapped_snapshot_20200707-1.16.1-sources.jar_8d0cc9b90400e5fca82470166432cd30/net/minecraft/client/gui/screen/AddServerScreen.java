package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.net.IDN;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AddServerScreen extends Screen {
   private Button buttonAddServer;
   private final BooleanConsumer field_213032_b;
   private final ServerData serverData;
   private TextFieldWidget textFieldServerAddress;
   private TextFieldWidget textFieldServerName;
   private Button buttonResourcePack;
   private final Screen field_228179_g_;
   private final Predicate<String> addressFilter = (p_210141_0_) -> {
      if (StringUtils.isNullOrEmpty(p_210141_0_)) {
         return true;
      } else {
         String[] astring = p_210141_0_.split(":");
         if (astring.length == 0) {
            return true;
         } else {
            try {
               String s = IDN.toASCII(astring[0]);
               return true;
            } catch (IllegalArgumentException illegalargumentexception) {
               return false;
            }
         }
      }
   };

   public AddServerScreen(Screen p_i225927_1_, BooleanConsumer p_i225927_2_, ServerData p_i225927_3_) {
      super(new TranslationTextComponent("addServer.title"));
      this.field_228179_g_ = p_i225927_1_;
      this.field_213032_b = p_i225927_2_;
      this.serverData = p_i225927_3_;
   }

   public void tick() {
      this.textFieldServerName.tick();
      this.textFieldServerAddress.tick();
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.textFieldServerName = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20, new TranslationTextComponent("addServer.enterName"));
      this.textFieldServerName.setFocused2(true);
      this.textFieldServerName.setText(this.serverData.serverName);
      this.textFieldServerName.setResponder(this::func_213028_a);
      this.children.add(this.textFieldServerName);
      this.textFieldServerAddress = new TextFieldWidget(this.font, this.width / 2 - 100, 106, 200, 20, new TranslationTextComponent("addServer.enterIp"));
      this.textFieldServerAddress.setMaxStringLength(128);
      this.textFieldServerAddress.setText(this.serverData.serverIP);
      this.textFieldServerAddress.setValidator(this.addressFilter);
      this.textFieldServerAddress.setResponder(this::func_213028_a);
      this.children.add(this.textFieldServerAddress);
      this.buttonResourcePack = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, func_238624_a_(this.serverData.getResourceMode()), (p_213031_1_) -> {
         this.serverData.setResourceMode(ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]);
         this.buttonResourcePack.setMessage(func_238624_a_(this.serverData.getResourceMode()));
      }));
      this.buttonAddServer = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20, new TranslationTextComponent("addServer.add"), (p_213030_1_) -> {
         this.onButtonServerAddPressed();
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, DialogTexts.field_240633_d_, (p_213029_1_) -> {
         this.field_213032_b.accept(false);
      }));
      this.func_228180_b_();
   }

   private static ITextComponent func_238624_a_(ServerData.ServerResourceMode p_238624_0_) {
      return (new TranslationTextComponent("addServer.resourcePack")).func_240702_b_(": ").func_230529_a_(p_238624_0_.getMotd());
   }

   public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
      String s = this.textFieldServerAddress.getText();
      String s1 = this.textFieldServerName.getText();
      this.init(p_231152_1_, p_231152_2_, p_231152_3_);
      this.textFieldServerAddress.setText(s);
      this.textFieldServerName.setText(s1);
   }

   private void func_213028_a(String p_213028_1_) {
      this.func_228180_b_();
   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
   }

   private void onButtonServerAddPressed() {
      this.serverData.serverName = this.textFieldServerName.getText();
      this.serverData.serverIP = this.textFieldServerAddress.getText();
      this.field_213032_b.accept(true);
   }

   public void onClose() {
      this.func_228180_b_();
      this.minecraft.displayGuiScreen(this.field_228179_g_);
   }

   private void func_228180_b_() {
      String s = this.textFieldServerAddress.getText();
      boolean flag = !s.isEmpty() && s.split(":").length > 0 && s.indexOf(32) == -1;
      this.buttonAddServer.active = flag && !this.textFieldServerName.getText().isEmpty();
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 17, 16777215);
      this.drawString(p_230430_1_, this.font, I18n.format("addServer.enterName"), this.width / 2 - 100, 53, 10526880);
      this.drawString(p_230430_1_, this.font, I18n.format("addServer.enterIp"), this.width / 2 - 100, 94, 10526880);
      this.textFieldServerName.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.textFieldServerAddress.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}