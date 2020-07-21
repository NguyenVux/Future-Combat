package net.minecraft.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImageButton extends Button {
   private final ResourceLocation resourceLocation;
   private final int xTexStart;
   private final int yTexStart;
   private final int yDiffText;
   private final int textureWidth;
   private final int textureHeight;

   public ImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.IPressable onPressIn) {
      this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);
   }

   public ImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, Button.IPressable onPressIn) {
      this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn, StringTextComponent.EMPTY);
   }

   public ImageButton(int p_i232261_1_, int p_i232261_2_, int p_i232261_3_, int p_i232261_4_, int p_i232261_5_, int p_i232261_6_, int p_i232261_7_, ResourceLocation p_i232261_8_, int p_i232261_9_, int p_i232261_10_, Button.IPressable p_i232261_11_, ITextComponent p_i232261_12_) {
      super(p_i232261_1_, p_i232261_2_, p_i232261_3_, p_i232261_4_, p_i232261_12_, p_i232261_11_);
      this.textureWidth = p_i232261_9_;
      this.textureHeight = p_i232261_10_;
      this.xTexStart = p_i232261_5_;
      this.yTexStart = p_i232261_6_;
      this.yDiffText = p_i232261_7_;
      this.resourceLocation = p_i232261_8_;
   }

   public void setPosition(int xIn, int yIn) {
      this.x = xIn;
      this.y = yIn;
   }

   public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.getTextureManager().bindTexture(this.resourceLocation);
      int i = this.yTexStart;
      if (this.isHovered()) {
         i += this.yDiffText;
      }

      RenderSystem.enableDepthTest();
      blit(p_230431_1_, this.x, this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
   }
}