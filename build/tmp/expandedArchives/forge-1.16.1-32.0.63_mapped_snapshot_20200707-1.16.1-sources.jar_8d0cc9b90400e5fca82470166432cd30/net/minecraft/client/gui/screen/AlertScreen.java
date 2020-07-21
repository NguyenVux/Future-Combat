package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlertScreen extends Screen {
   private final Runnable field_201552_h;
   protected final ITextComponent field_201550_f;
   private final List<ITextProperties> field_201553_i = Lists.newArrayList();
   protected final ITextComponent field_201551_g;
   private int field_201549_s;

   public AlertScreen(Runnable p_i48623_1_, ITextComponent p_i48623_2_, ITextComponent p_i48623_3_) {
      this(p_i48623_1_, p_i48623_2_, p_i48623_3_, DialogTexts.field_240637_h_);
   }

   public AlertScreen(Runnable p_i232268_1_, ITextComponent p_i232268_2_, ITextComponent p_i232268_3_, ITextComponent p_i232268_4_) {
      super(p_i232268_2_);
      this.field_201552_h = p_i232268_1_;
      this.field_201550_f = p_i232268_3_;
      this.field_201551_g = p_i232268_4_;
   }

   protected void init() {
      super.init();
      this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, this.field_201551_g, (p_212983_1_) -> {
         this.field_201552_h.run();
      }));
      this.field_201553_i.clear();
      this.field_201553_i.addAll(this.font.func_238425_b_(this.field_201550_f, this.width - 50));
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 70, 16777215);
      int i = 90;

      for(ITextProperties itextproperties : this.field_201553_i) {
         this.drawCenteredString(p_230430_1_, this.font, itextproperties, this.width / 2, i, 16777215);
         i += 9;
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   public void tick() {
      super.tick();
      if (--this.field_201549_s == 0) {
         for(Widget widget : this.buttons) {
            widget.active = true;
         }
      }

   }
}