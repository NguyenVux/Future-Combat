package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nullable;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LanguageScreen extends SettingsScreen {
   /** The List GuiSlot object reference. */
   private LanguageScreen.List list;
   /** Reference to the LanguageManager object. */
   private final LanguageManager languageManager;
   private OptionButton field_211832_i;
   /** The button to confirm the current settings. */
   private Button confirmSettingsBtn;

   public LanguageScreen(Screen screen, GameSettings gameSettingsObj, LanguageManager manager) {
      super(screen, gameSettingsObj, new TranslationTextComponent("options.language"));
      this.languageManager = manager;
   }

   protected void init() {
      this.list = new LanguageScreen.List(this.minecraft);
      this.children.add(this.list);
      this.field_211832_i = this.addButton(new OptionButton(this.width / 2 - 155, this.height - 38, 150, 20, AbstractOption.FORCE_UNICODE_FONT, AbstractOption.FORCE_UNICODE_FONT.func_238152_c_(this.gameSettings), (p_213037_1_) -> {
         AbstractOption.FORCE_UNICODE_FONT.nextValue(this.gameSettings);
         this.gameSettings.saveOptions();
         p_213037_1_.setMessage(AbstractOption.FORCE_UNICODE_FONT.func_238152_c_(this.gameSettings));
         this.minecraft.updateWindowSize();
      }));
      this.confirmSettingsBtn = this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 38, 150, 20, DialogTexts.field_240632_c_, (p_213036_1_) -> {
         LanguageScreen.List.LanguageEntry languagescreen$list$languageentry = this.list.getSelected();
         if (languagescreen$list$languageentry != null && !languagescreen$list$languageentry.field_214398_b.getCode().equals(this.languageManager.getCurrentLanguage().getCode())) {
            this.languageManager.setCurrentLanguage(languagescreen$list$languageentry.field_214398_b);
            this.gameSettings.language = languagescreen$list$languageentry.field_214398_b.getCode();
            net.minecraftforge.client.ForgeHooksClient.refreshResources(this.minecraft, net.minecraftforge.resource.VanillaResourceType.LANGUAGES);
            this.confirmSettingsBtn.setMessage(DialogTexts.field_240632_c_);
            this.field_211832_i.setMessage(AbstractOption.FORCE_UNICODE_FONT.func_238152_c_(this.gameSettings));
            this.gameSettings.saveOptions();
         }

         this.minecraft.displayGuiScreen(this.parentScreen);
      }));
      super.init();
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.list.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 16, 16777215);
      this.drawCenteredString(p_230430_1_, this.font, "(" + I18n.format("options.languageWarning") + ")", this.width / 2, this.height - 56, 8421504);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   @OnlyIn(Dist.CLIENT)
   class List extends ExtendedList<LanguageScreen.List.LanguageEntry> {
      public List(Minecraft mcIn) {
         super(mcIn, LanguageScreen.this.width, LanguageScreen.this.height, 32, LanguageScreen.this.height - 65 + 4, 18);

         for(Language language : LanguageScreen.this.languageManager.getLanguages()) {
            LanguageScreen.List.LanguageEntry languagescreen$list$languageentry = new LanguageScreen.List.LanguageEntry(language);
            this.addEntry(languagescreen$list$languageentry);
            if (LanguageScreen.this.languageManager.getCurrentLanguage().getCode().equals(language.getCode())) {
               this.setSelected(languagescreen$list$languageentry);
            }
         }

         if (this.getSelected() != null) {
            this.centerScrollOn(this.getSelected());
         }

      }

      protected int getScrollbarPosition() {
         return super.getScrollbarPosition() + 20;
      }

      public int getRowWidth() {
         return super.getRowWidth() + 50;
      }

      public void setSelected(@Nullable LanguageScreen.List.LanguageEntry p_241215_1_) {
         super.setSelected(p_241215_1_);
         if (p_241215_1_ != null) {
            NarratorChatListener.INSTANCE.say((new TranslationTextComponent("narrator.select", p_241215_1_.field_214398_b)).getString());
         }

      }

      protected void renderBackground(MatrixStack p_230433_1_) {
         LanguageScreen.this.renderBackground(p_230433_1_);
      }

      protected boolean isFocused() {
         return LanguageScreen.this.getFocused() == this;
      }

      @OnlyIn(Dist.CLIENT)
      public class LanguageEntry extends ExtendedList.AbstractListEntry<LanguageScreen.List.LanguageEntry> {
         private final Language field_214398_b;

         public LanguageEntry(Language p_i50494_2_) {
            this.field_214398_b = p_i50494_2_;
         }

         public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            String s = this.field_214398_b.toString();
            LanguageScreen.this.font.func_238406_a_(p_230432_1_, s, (float)(List.this.width / 2 - LanguageScreen.this.font.getStringWidth(s) / 2), (float)(p_230432_3_ + 1), 16777215, true);
         }

         public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
            if (p_231044_5_ == 0) {
               this.func_214395_a();
               return true;
            } else {
               return false;
            }
         }

         private void func_214395_a() {
            List.this.setSelected(this);
         }
      }
   }
}