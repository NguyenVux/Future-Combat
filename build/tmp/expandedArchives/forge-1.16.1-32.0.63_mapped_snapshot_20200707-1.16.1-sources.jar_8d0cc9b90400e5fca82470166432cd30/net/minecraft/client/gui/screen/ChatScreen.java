package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.CommandSuggestionHelper;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChatScreen extends Screen {
   private String historyBuffer = "";
   /** keeps position of which chat message you will select when you press up */
   private int sentHistoryCursor = -1;
   /** Chat entry field */
   protected TextFieldWidget inputField;
   /** is the text that appears when you press the chat key and the input box appears pre-filled */
   private String defaultInputFieldText = "";
   private CommandSuggestionHelper commandSuggestionHelper;

   public ChatScreen(String defaultText) {
      super(NarratorChatListener.EMPTY);
      this.defaultInputFieldText = defaultText;
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.sentHistoryCursor = this.minecraft.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new TextFieldWidget(this.font, 4, this.height - 12, this.width - 4, 12, new TranslationTextComponent("chat.editBox")) {
         protected IFormattableTextComponent getNarrationMessage() {
            return super.getNarrationMessage().func_240702_b_(ChatScreen.this.commandSuggestionHelper.func_228129_c_());
         }
      };
      this.inputField.setMaxStringLength(256);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setResponder(this::func_212997_a);
      this.children.add(this.inputField);
      this.commandSuggestionHelper = new CommandSuggestionHelper(this.minecraft, this, this.inputField, this.font, false, false, 1, 10, true, -805306368);
      this.commandSuggestionHelper.init();
      this.setFocusedDefault(this.inputField);
   }

   public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
      String s = this.inputField.getText();
      this.init(p_231152_1_, p_231152_2_, p_231152_3_);
      this.setChatLine(s);
      this.commandSuggestionHelper.init();
   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
      this.minecraft.ingameGUI.getChatGUI().resetScroll();
   }

   public void tick() {
      this.inputField.tick();
   }

   private void func_212997_a(String p_212997_1_) {
      String s = this.inputField.getText();
      this.commandSuggestionHelper.func_228124_a_(!s.equals(this.defaultInputFieldText));
      this.commandSuggestionHelper.init();
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (this.commandSuggestionHelper.onKeyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
         return true;
      } else if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
         return true;
      } else if (p_231046_1_ == 256) {
         this.minecraft.displayGuiScreen((Screen)null);
         return true;
      } else if (p_231046_1_ != 257 && p_231046_1_ != 335) {
         if (p_231046_1_ == 265) {
            this.getSentHistory(-1);
            return true;
         } else if (p_231046_1_ == 264) {
            this.getSentHistory(1);
            return true;
         } else if (p_231046_1_ == 266) {
            this.minecraft.ingameGUI.getChatGUI().addScrollPos((double)(this.minecraft.ingameGUI.getChatGUI().getLineCount() - 1));
            return true;
         } else if (p_231046_1_ == 267) {
            this.minecraft.ingameGUI.getChatGUI().addScrollPos((double)(-this.minecraft.ingameGUI.getChatGUI().getLineCount() + 1));
            return true;
         } else {
            return false;
         }
      } else {
         String s = this.inputField.getText().trim();
         if (!s.isEmpty()) {
            this.sendMessage(s);
         }

         this.minecraft.displayGuiScreen((Screen)null);
         return true;
      }
   }

   public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
      if (p_231043_5_ > 1.0D) {
         p_231043_5_ = 1.0D;
      }

      if (p_231043_5_ < -1.0D) {
         p_231043_5_ = -1.0D;
      }

      if (this.commandSuggestionHelper.onScroll(p_231043_5_)) {
         return true;
      } else {
         if (!hasShiftDown()) {
            p_231043_5_ *= 7.0D;
         }

         this.minecraft.ingameGUI.getChatGUI().addScrollPos(p_231043_5_);
         return true;
      }
   }

   public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
      if (this.commandSuggestionHelper.onClick((double)((int)p_231044_1_), (double)((int)p_231044_3_), p_231044_5_)) {
         return true;
      } else {
         if (p_231044_5_ == 0) {
            NewChatGui newchatgui = this.minecraft.ingameGUI.getChatGUI();
            if (newchatgui.func_238491_a_(p_231044_1_, p_231044_3_)) {
               return true;
            }

            Style style = newchatgui.func_238494_b_(p_231044_1_, p_231044_3_);
            if (style != null && this.handleComponentClicked(style)) {
               return true;
            }
         }

         return this.inputField.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_) ? true : super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
      }
   }

   protected void insertText(String p_231155_1_, boolean p_231155_2_) {
      if (p_231155_2_) {
         this.inputField.setText(p_231155_1_);
      } else {
         this.inputField.writeText(p_231155_1_);
      }

   }

   /**
    * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message
    */
   public void getSentHistory(int msgPos) {
      int i = this.sentHistoryCursor + msgPos;
      int j = this.minecraft.ingameGUI.getChatGUI().getSentMessages().size();
      i = MathHelper.clamp(i, 0, j);
      if (i != this.sentHistoryCursor) {
         if (i == j) {
            this.sentHistoryCursor = j;
            this.inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == j) {
               this.historyBuffer = this.inputField.getText();
            }

            this.inputField.setText(this.minecraft.ingameGUI.getChatGUI().getSentMessages().get(i));
            this.commandSuggestionHelper.func_228124_a_(false);
            this.sentHistoryCursor = i;
         }
      }
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.setFocused(this.inputField);
      this.inputField.setFocused2(true);
      fill(p_230430_1_, 2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.gameSettings.getChatBackgroundColor(Integer.MIN_VALUE));
      this.inputField.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.commandSuggestionHelper.func_238500_a_(p_230430_1_, p_230430_2_, p_230430_3_);
      Style style = this.minecraft.ingameGUI.getChatGUI().func_238494_b_((double)p_230430_2_, (double)p_230430_3_);
      if (style != null && style.getHoverEvent() != null) {
         this.renderComponentHoverEffect(p_230430_1_, style, p_230430_2_, p_230430_3_);
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private void setChatLine(String p_208604_1_) {
      this.inputField.setText(p_208604_1_);
   }
}