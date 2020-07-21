package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WorldSelectionScreen extends Screen {
   protected final Screen prevScreen;
   private List<ITextProperties> worldVersTooltip;
   private Button deleteButton;
   private Button selectButton;
   private Button renameButton;
   private Button copyButton;
   protected TextFieldWidget field_212352_g;
   private WorldSelectionList selectionList;

   public WorldSelectionScreen(Screen screenIn) {
      super(new TranslationTextComponent("selectWorld.title"));
      this.prevScreen = screenIn;
   }

   public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
      return super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
   }

   public void tick() {
      this.field_212352_g.tick();
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.field_212352_g = new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20, this.field_212352_g, new TranslationTextComponent("selectWorld.search"));
      this.field_212352_g.setResponder((p_214329_1_) -> {
         this.selectionList.func_212330_a(() -> {
            return p_214329_1_;
         }, false);
      });
      this.selectionList = new WorldSelectionList(this, this.minecraft, this.width, this.height, 48, this.height - 64, 36, () -> {
         return this.field_212352_g.getText();
      }, this.selectionList);
      this.children.add(this.field_212352_g);
      this.children.add(this.selectionList);
      this.selectButton = this.addButton(new Button(this.width / 2 - 154, this.height - 52, 150, 20, new TranslationTextComponent("selectWorld.select"), (p_214325_1_) -> {
         this.selectionList.func_214376_a().ifPresent(WorldSelectionList.Entry::func_214438_a);
      }));
      this.addButton(new Button(this.width / 2 + 4, this.height - 52, 150, 20, new TranslationTextComponent("selectWorld.create"), (p_214326_1_) -> {
         this.minecraft.displayGuiScreen(new CreateWorldScreen(this));
      }));
      this.renameButton = this.addButton(new Button(this.width / 2 - 154, this.height - 28, 72, 20, new TranslationTextComponent("selectWorld.edit"), (p_214323_1_) -> {
         this.selectionList.func_214376_a().ifPresent(WorldSelectionList.Entry::func_214444_c);
      }));
      this.deleteButton = this.addButton(new Button(this.width / 2 - 76, this.height - 28, 72, 20, new TranslationTextComponent("selectWorld.delete"), (p_214330_1_) -> {
         this.selectionList.func_214376_a().ifPresent(WorldSelectionList.Entry::func_214442_b);
      }));
      this.copyButton = this.addButton(new Button(this.width / 2 + 4, this.height - 28, 72, 20, new TranslationTextComponent("selectWorld.recreate"), (p_214328_1_) -> {
         this.selectionList.func_214376_a().ifPresent(WorldSelectionList.Entry::func_214445_d);
      }));
      this.addButton(new Button(this.width / 2 + 82, this.height - 28, 72, 20, DialogTexts.field_240633_d_, (p_214327_1_) -> {
         this.minecraft.displayGuiScreen(this.prevScreen);
      }));
      this.func_214324_a(false);
      this.setFocusedDefault(this.field_212352_g);
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_) ? true : this.field_212352_g.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
   }

   public void onClose() {
      this.minecraft.displayGuiScreen(this.prevScreen);
   }

   public boolean charTyped(char p_231042_1_, int p_231042_2_) {
      return this.field_212352_g.charTyped(p_231042_1_, p_231042_2_);
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.worldVersTooltip = null;
      this.selectionList.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.field_212352_g.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      if (this.worldVersTooltip != null) {
         this.renderTooltip(p_230430_1_, this.worldVersTooltip, p_230430_2_, p_230430_3_);
      }

   }

   public void func_239026_b_(List<ITextProperties> p_239026_1_) {
      this.worldVersTooltip = p_239026_1_;
   }

   public void func_214324_a(boolean p_214324_1_) {
      this.selectButton.active = p_214324_1_;
      this.deleteButton.active = p_214324_1_;
      this.renameButton.active = p_214324_1_;
      this.copyButton.active = p_214324_1_;
   }

   public void removed() {
      if (this.selectionList != null) {
         this.selectionList.children().forEach(WorldSelectionList.Entry::close);
      }

   }
}