package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreateBuffetWorldScreen extends Screen {
   private final Screen parent;
   private final Consumer<Biome> field_238592_b_;
   private CreateBuffetWorldScreen.BiomeList biomeList;
   private Biome field_238593_p_;
   private Button field_205313_u;

   public CreateBuffetWorldScreen(Screen p_i232271_1_, Consumer<Biome> p_i232271_2_, Biome p_i232271_3_) {
      super(new TranslationTextComponent("createWorld.customize.buffet.title"));
      this.parent = p_i232271_1_;
      this.field_238592_b_ = p_i232271_2_;
      this.field_238593_p_ = p_i232271_3_;
   }

   public void onClose() {
      this.minecraft.displayGuiScreen(this.parent);
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.biomeList = new CreateBuffetWorldScreen.BiomeList();
      this.children.add(this.biomeList);
      this.field_205313_u = this.addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, DialogTexts.field_240632_c_, (p_241579_1_) -> {
         this.field_238592_b_.accept(this.field_238593_p_);
         this.minecraft.displayGuiScreen(this.parent);
      }));
      this.addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, DialogTexts.field_240633_d_, (p_213015_1_) -> {
         this.minecraft.displayGuiScreen(this.parent);
      }));
      this.biomeList.setSelected(this.biomeList.children().stream().filter((p_241578_1_) -> {
         return Objects.equals(p_241578_1_.field_238599_b_, this.field_238593_p_);
      }).findFirst().orElse((CreateBuffetWorldScreen.BiomeList.BiomeEntry)null));
   }

   private void func_205306_h() {
      this.field_205313_u.active = this.biomeList.getSelected() != null;
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderDirtBackground(0);
      this.biomeList.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 8, 16777215);
      this.drawCenteredString(p_230430_1_, this.font, I18n.format("createWorld.customize.buffet.biome"), this.width / 2, 28, 10526880);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   @OnlyIn(Dist.CLIENT)
   class BiomeList extends ExtendedList<CreateBuffetWorldScreen.BiomeList.BiomeEntry> {
      private BiomeList() {
         super(CreateBuffetWorldScreen.this.minecraft, CreateBuffetWorldScreen.this.width, CreateBuffetWorldScreen.this.height, 40, CreateBuffetWorldScreen.this.height - 37, 16);
         Registry.BIOME.stream().sorted(Comparator.comparing((p_238598_0_) -> {
            return p_238598_0_.getDisplayName().getString();
         })).forEach((p_238597_1_) -> {
            this.addEntry(new CreateBuffetWorldScreen.BiomeList.BiomeEntry(p_238597_1_));
         });
      }

      protected boolean isFocused() {
         return CreateBuffetWorldScreen.this.getFocused() == this;
      }

      public void setSelected(@Nullable CreateBuffetWorldScreen.BiomeList.BiomeEntry p_241215_1_) {
         super.setSelected(p_241215_1_);
         if (p_241215_1_ != null) {
            CreateBuffetWorldScreen.this.field_238593_p_ = p_241215_1_.field_238599_b_;
            NarratorChatListener.INSTANCE.say((new TranslationTextComponent("narrator.select", p_241215_1_.field_238599_b_.getDisplayName().getString())).getString());
         }

         CreateBuffetWorldScreen.this.func_205306_h();
      }

      @OnlyIn(Dist.CLIENT)
      class BiomeEntry extends ExtendedList.AbstractListEntry<CreateBuffetWorldScreen.BiomeList.BiomeEntry> {
         private final Biome field_238599_b_;

         public BiomeEntry(Biome p_i232272_2_) {
            this.field_238599_b_ = p_i232272_2_;
         }

         public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            BiomeList.this.drawString(p_230432_1_, CreateBuffetWorldScreen.this.font, this.field_238599_b_.getDisplayName().getString(), p_230432_4_ + 5, p_230432_3_ + 2, 16777215);
         }

         public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
            if (p_231044_5_ == 0) {
               BiomeList.this.setSelected(this);
               return true;
            } else {
               return false;
            }
         }
      }
   }
}