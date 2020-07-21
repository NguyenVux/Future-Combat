package net.minecraft.client.gui.toasts;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RecipeToast implements IToast {
   private final List<IRecipe<?>> recipes = Lists.newArrayList();
   private long firstDrawTime;
   private boolean hasNewOutputs;

   public RecipeToast(IRecipe<?> recipeIn) {
      this.recipes.add(recipeIn);
   }

   public IToast.Visibility func_230444_a_(MatrixStack p_230444_1_, ToastGui p_230444_2_, long p_230444_3_) {
      if (this.hasNewOutputs) {
         this.firstDrawTime = p_230444_3_;
         this.hasNewOutputs = false;
      }

      if (this.recipes.isEmpty()) {
         return IToast.Visibility.HIDE;
      } else {
         p_230444_2_.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
         RenderSystem.color3f(1.0F, 1.0F, 1.0F);
         p_230444_2_.blit(p_230444_1_, 0, 0, 0, 32, this.func_230445_a_(), this.func_238540_d_());
         p_230444_2_.getMinecraft().fontRenderer.drawString(p_230444_1_, I18n.format("recipe.toast.title"), 30.0F, 7.0F, -11534256);
         p_230444_2_.getMinecraft().fontRenderer.drawString(p_230444_1_, I18n.format("recipe.toast.description"), 30.0F, 18.0F, -16777216);
         IRecipe<?> irecipe = this.recipes.get((int)(p_230444_3_ / Math.max(1L, 5000L / (long)this.recipes.size()) % (long)this.recipes.size()));
         ItemStack itemstack = irecipe.getIcon();
         RenderSystem.pushMatrix();
         RenderSystem.scalef(0.6F, 0.6F, 1.0F);
         p_230444_2_.getMinecraft().getItemRenderer().func_239390_c_(itemstack, 3, 3);
         RenderSystem.popMatrix();
         p_230444_2_.getMinecraft().getItemRenderer().func_239390_c_(irecipe.getRecipeOutput(), 8, 8);
         return p_230444_3_ - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
      }
   }

   public void addRecipe(IRecipe<?> recipeIn) {
      if (this.recipes.add(recipeIn)) {
         this.hasNewOutputs = true;
      }

   }

   public static void addOrUpdate(ToastGui toastGui, IRecipe<?> recipeIn) {
      RecipeToast recipetoast = toastGui.getToast(RecipeToast.class, NO_TOKEN);
      if (recipetoast == null) {
         toastGui.add(new RecipeToast(recipeIn));
      } else {
         recipetoast.addRecipe(recipeIn);
      }

   }
}