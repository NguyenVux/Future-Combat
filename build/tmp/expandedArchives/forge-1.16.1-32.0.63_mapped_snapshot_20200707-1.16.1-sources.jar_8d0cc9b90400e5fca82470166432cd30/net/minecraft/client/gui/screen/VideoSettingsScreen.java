package net.minecraft.client.gui.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.renderer.GPUWarning;
import net.minecraft.client.settings.FullscreenResolutionOption;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VideoSettingsScreen extends SettingsScreen {
   private static final ITextComponent field_241598_c_ = (new TranslationTextComponent("options.graphics.fabulous")).func_240699_a_(TextFormatting.ITALIC);
   private static final ITextComponent field_241599_p_ = new TranslationTextComponent("options.graphics.warning.message", field_241598_c_, field_241598_c_);
   private static final ITextComponent field_241600_q_ = (new TranslationTextComponent("options.graphics.warning.title")).func_240699_a_(TextFormatting.RED);
   private static final ITextComponent field_241601_r_ = new TranslationTextComponent("options.graphics.warning.accept");
   private static final ITextComponent field_241602_s_ = new TranslationTextComponent("options.graphics.warning.cancel");
   private static final ITextComponent field_241603_t_ = new StringTextComponent("\n");
   private static final AbstractOption[] OPTIONS = new AbstractOption[]{AbstractOption.GRAPHICS, AbstractOption.RENDER_DISTANCE, AbstractOption.AO, AbstractOption.FRAMERATE_LIMIT, AbstractOption.VSYNC, AbstractOption.VIEW_BOBBING, AbstractOption.GUI_SCALE, AbstractOption.ATTACK_INDICATOR, AbstractOption.GAMMA, AbstractOption.RENDER_CLOUDS, AbstractOption.FULLSCREEN, AbstractOption.PARTICLES, AbstractOption.MIPMAP_LEVELS, AbstractOption.ENTITY_SHADOWS, AbstractOption.field_238237_p_};
   @Nullable
   private List<ITextProperties> field_238662_c_;
   private OptionsRowList optionsRowList;
   private final GPUWarning field_241604_x_;
   private final int mipmapLevels;

   public VideoSettingsScreen(Screen parentScreenIn, GameSettings gameSettingsIn) {
      super(parentScreenIn, gameSettingsIn, new TranslationTextComponent("options.videoTitle"));
      this.field_241604_x_ = parentScreenIn.minecraft.func_241558_U_();
      this.field_241604_x_.func_241702_i_();
      if (gameSettingsIn.field_238330_f_ == GraphicsFanciness.FABULOUS) {
         this.field_241604_x_.func_241698_e_();
      }

      this.mipmapLevels = gameSettingsIn.mipmapLevels;
   }

   protected void init() {
      this.optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
      this.optionsRowList.addOption(new FullscreenResolutionOption(this.minecraft.getMainWindow()));
      this.optionsRowList.addOption(AbstractOption.BIOME_BLEND_RADIUS);
      this.optionsRowList.addOptions(OPTIONS);
      this.children.add(this.optionsRowList);
      this.addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, DialogTexts.field_240632_c_, (p_213106_1_) -> {
         this.minecraft.gameSettings.saveOptions();
         this.minecraft.getMainWindow().update();
         this.minecraft.displayGuiScreen(this.parentScreen);
      }));
   }

   public void removed() {
      if (this.gameSettings.mipmapLevels != this.mipmapLevels) {
         this.minecraft.setMipmapLevels(this.gameSettings.mipmapLevels);
         this.minecraft.scheduleResourcesRefresh();
      }

      super.removed();
   }

   public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
      int i = this.gameSettings.guiScale;
      GraphicsFanciness graphicsfanciness = this.gameSettings.field_238330_f_;
      if (super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
         if (this.gameSettings.guiScale != i) {
            this.minecraft.updateWindowSize();
         }

         if (this.field_241604_x_.func_241700_g_()) {
            List<ITextProperties> list = Lists.newArrayList(field_241599_p_, field_241603_t_);
            String s = this.field_241604_x_.func_241703_j_();
            if (s != null) {
               list.add(field_241603_t_);
               list.add((new TranslationTextComponent("options.graphics.warning.renderer", s)).func_240699_a_(TextFormatting.GRAY));
            }

            String s1 = this.field_241604_x_.func_241705_l_();
            if (s1 != null) {
               list.add(field_241603_t_);
               list.add((new TranslationTextComponent("options.graphics.warning.vendor", s1)).func_240699_a_(TextFormatting.GRAY));
            }

            String s2 = this.field_241604_x_.func_241704_k_();
            if (s2 != null) {
               list.add(field_241603_t_);
               list.add((new TranslationTextComponent("options.graphics.warning.version", s2)).func_240699_a_(TextFormatting.GRAY));
            }

            this.minecraft.displayGuiScreen(new GPUWarningScreen(field_241600_q_, list, ImmutableList.of(new GPUWarningScreen.Option(field_241601_r_, (p_241606_1_) -> {
               this.gameSettings.field_238330_f_ = GraphicsFanciness.FABULOUS;
               Minecraft.getInstance().worldRenderer.loadRenderers();
               this.field_241604_x_.func_241698_e_();
               this.minecraft.displayGuiScreen(this);
            }), new GPUWarningScreen.Option(field_241602_s_, (p_241605_1_) -> {
               this.field_241604_x_.func_241699_f_();
               this.minecraft.displayGuiScreen(this);
            }))));
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
      int i = this.gameSettings.guiScale;
      if (super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_)) {
         return true;
      } else if (this.optionsRowList.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_)) {
         if (this.gameSettings.guiScale != i) {
            this.minecraft.updateWindowSize();
         }

         return true;
      } else {
         return false;
      }
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.field_238662_c_ = null;
      Optional<Widget> optional = this.optionsRowList.func_238518_c_((double)p_230430_2_, (double)p_230430_3_);
      if (optional.isPresent() && optional.get() instanceof OptionButton) {
         Optional<List<ITextProperties>> optional1 = ((OptionButton)optional.get()).func_238517_a_().func_238246_b_();
         optional1.ifPresent((p_241607_1_) -> {
            this.field_238662_c_ = p_241607_1_;
         });
      }

      this.renderBackground(p_230430_1_);
      this.optionsRowList.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 5, 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      if (this.field_238662_c_ != null) {
         this.renderTooltip(p_230430_1_, this.field_238662_c_, p_230430_2_, p_230430_3_);
      }

   }
}