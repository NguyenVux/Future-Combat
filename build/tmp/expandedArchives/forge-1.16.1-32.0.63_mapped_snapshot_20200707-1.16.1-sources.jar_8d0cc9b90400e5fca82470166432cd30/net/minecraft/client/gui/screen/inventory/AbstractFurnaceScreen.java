package net.minecraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceContainer> extends ContainerScreen<T> implements IRecipeShownListener {
   private static final ResourceLocation field_214089_l = new ResourceLocation("textures/gui/recipe_button.png");
   public final AbstractRecipeBookGui recipeGui;
   private boolean widthTooNarrowIn;
   private final ResourceLocation guiTexture;

   public AbstractFurnaceScreen(T screenContainer, AbstractRecipeBookGui recipeGuiIn, PlayerInventory inv, ITextComponent titleIn, ResourceLocation guiTextureIn) {
      super(screenContainer, inv, titleIn);
      this.recipeGui = recipeGuiIn;
      this.guiTexture = guiTextureIn;
   }

   public void init() {
      super.init();
      this.widthTooNarrowIn = this.width < 379;
      this.recipeGui.init(this.width, this.height, this.minecraft, this.widthTooNarrowIn, this.container);
      this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
      this.addButton(new ImageButton(this.guiLeft + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, field_214089_l, (p_214087_1_) -> {
         this.recipeGui.initSearchBar(this.widthTooNarrowIn);
         this.recipeGui.toggleVisibility();
         this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
         ((ImageButton)p_214087_1_).setPosition(this.guiLeft + 20, this.height / 2 - 49);
      }));
      this.field_238742_p_ = (this.xSize - this.font.func_238414_a_(this.title)) / 2;
   }

   public void tick() {
      super.tick();
      this.recipeGui.tick();
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      if (this.recipeGui.isVisible() && this.widthTooNarrowIn) {
         this.func_230450_a_(p_230430_1_, p_230430_4_, p_230430_2_, p_230430_3_);
         this.recipeGui.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      } else {
         this.recipeGui.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
         super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
         this.recipeGui.func_230477_a_(p_230430_1_, this.guiLeft, this.guiTop, true, p_230430_4_);
      }

      this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
      this.recipeGui.func_238924_c_(p_230430_1_, this.guiLeft, this.guiTop, p_230430_2_, p_230430_3_);
   }

   protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.minecraft.getTextureManager().bindTexture(this.guiTexture);
      int i = this.guiLeft;
      int j = this.guiTop;
      this.blit(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
      if (this.container.isBurning()) {
         int k = this.container.getBurnLeftScaled();
         this.blit(p_230450_1_, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
      }

      int l = this.container.getCookProgressionScaled();
      this.blit(p_230450_1_, i + 79, j + 34, 176, 14, l + 1, 16);
   }

   public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
      if (this.recipeGui.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
         return true;
      } else {
         return this.widthTooNarrowIn && this.recipeGui.isVisible() ? true : super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
      }
   }

   /**
    * Called when the mouse is clicked over a slot or outside the gui.
    */
   protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      super.handleMouseClick(slotIn, slotId, mouseButton, type);
      this.recipeGui.slotClicked(slotIn);
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      return this.recipeGui.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_) ? false : super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
   }

   protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
      boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
      return this.recipeGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
   }

   public boolean charTyped(char p_231042_1_, int p_231042_2_) {
      return this.recipeGui.charTyped(p_231042_1_, p_231042_2_) ? true : super.charTyped(p_231042_1_, p_231042_2_);
   }

   public void recipesUpdated() {
      this.recipeGui.recipesUpdated();
   }

   public RecipeBookGui getRecipeGui() {
      return this.recipeGui;
   }

   public void removed() {
      this.recipeGui.removed();
      super.removed();
   }
}